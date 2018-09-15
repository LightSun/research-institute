package com.heaven7.ve.colorgap.impl;


import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.CutInfo;
import com.heaven7.ve.colorgap.MusicShader;
import com.heaven7.ve.template.TransferDelegate;
import com.heaven7.ve.template.VETemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by heaven7 on 2018/3/16 0016.
 */

public class MusicShaderImpl implements MusicShader {

    private static final boolean DEBUG = false;
    private static final String TAG = "MusicShaderImpl";

    @Override
    public VETemplate tint(Context context, @Nullable VETemplate template, List<CutInfo.PlaidInfo> plaids, int tintFlags) {

        /*
         * if no template: we just regard the plaids as one logic-sentence
         */
        VETemplate resultTem = new VETemplate();
        /*
         * 根据模版tint or 根据 镜头类型，人脸识别信息，目录 等去tint.
         */
         if(template != null){
             resultTem.setChapterFillType(template.getChapterFillType());
             //template.computePercent();
             float scale = plaids.size() * 1f / template.getTotalPlaidCount(); //target-plaid.size / template-plaid.size
             template.setTotalScale(scale);
             if(!template.isReleaseSentence()){
                 tintFlags |= FLAG_PROTECT_SENTENCE;
             }
             /*
              * 1, 计算各个新的逻辑语句 格子个数。使得格子数目相当.
              * 2, 着色
              * 3, 特效，滤镜，转场。
              */
             List<VETemplate.LogicSentence> sentences = template.getLogicSentences();
             PlaidCountAdjustDelegate adjustDelegate = new PlaidCountAdjustDelegate(scale, sentences,  plaids.size());
             List<CountInfo> countInfoes = adjustDelegate.adjustPlaidCount(tintFlags);
             //at last curPlaidCount = targetPlaidCount.
             TransferDelegate delegate = new TransferDelegate();
             int lastIndex = 0;
             for(int i = 0, size = countInfoes.size(); i < size ; i ++){
                 //fill sentence.
                 CountInfo info = countInfoes.get(i);
                 final int actualCount = info.getRoundWithOffset();

                 VETemplate.LogicSentence ls = new VETemplate.LogicSentence();
                 ls.setPlaids(plaids.subList(lastIndex, lastIndex + actualCount));
                 ls.setDir(info.getSourceDir());
                 lastIndex += actualCount;
                 resultTem.addLogicSentence(ls);
                 //tint plaid/transfer by weight of sentence
                 VETemplate.LogicSentence from = findMaxWeightSentence(sentences, info);
                 delegate.transferAll(from, ls);
                 ls.setShotCategoryFlags(from.getShotCategoryFlags());
             }
             assert resultTem.getTotalPlaidCount() == plaids.size();
         }else{
             //one logic-sentence
             VETemplate.LogicSentence ls = new VETemplate.LogicSentence();
             ls.setPlaids(plaids);
             resultTem.addLogicSentence(ls);
         }
         return resultTem;
    }

    private static VETemplate.LogicSentence findMaxWeightSentence(List<VETemplate.LogicSentence> sentences, CountInfo info) {
        if(Predicates.isEmpty(info.getMergedMergeCountInfoes())){
            return sentences.get(info.sentenceIndex);
        }
        //start from core sentence
        VETemplate.LogicSentence result = sentences.get(info.sentenceIndex);
        int weight = result.getWeight();
        for (CountInfo ci : info.getMergedMergeCountInfoes()){
            VETemplate.LogicSentence temp_ls = sentences.get(ci.sentenceIndex);
            if(temp_ls.getWeight() > weight){
                weight = temp_ls.getWeight();
                result = temp_ls;
            }
        }
        return result;
    }

    private static class PlaidCountAdjustDelegate{

        final List<VETemplate.LogicSentence> sentences;
        final int expectCount;
        final float scale;
        /** the pre release count (when plaid count < sentence.size) */
        int mPreReleasedCount;

        final Comparator<CountInfo> mValueCMP = new Comparator<CountInfo>() {
            @Override
            public int compare(CountInfo o1, CountInfo o2) {
                //按照价值从低到高排序.
                int value1 = sentences.get(o1.sentenceIndex).getWeight();
                int value2 = sentences.get(o2.sentenceIndex).getWeight();
                return Integer.compare(value1, value2);
            }
        };

        public PlaidCountAdjustDelegate(float scale, List<VETemplate.LogicSentence> sentences, int expectCount) {
            if(expectCount <= 0){
                throw new IllegalArgumentException();
            }
            this.scale = scale;
            this.sentences = sentences;
            this.expectCount = expectCount;
        }

        public List<CountInfo> adjustPlaidCount(int tintFlags){
            final boolean allow_release_head_tail = (tintFlags & FLAG_ALLOW_RELEASE_HEAD_TAIL) == FLAG_ALLOW_RELEASE_HEAD_TAIL;
            final boolean protect_sentence = (tintFlags & FLAG_PROTECT_SENTENCE) == FLAG_PROTECT_SENTENCE;
            final int size = sentences.size();
            //一个逻辑语句对应一个CountInfo
            final List<CountInfo> list = new ArrayList<>();
            int count = 0;
            for (int i = 0 ; i < size ; i ++){
                VETemplate.LogicSentence ls = sentences.get(i);
                float plaidCount = ls.getPlaidCount() * scale;
                CountInfo info = new CountInfo(i, ls.getPlaidCount(), plaidCount);
                info.setSourceDir(ls.getDir());
                list.add(info);
                count += info.getRoundMinOne();
            }
            final boolean canReleaseHeadOrTail = allow_release_head_tail || canReleaseHeadOrTail(size, list);
            //next step should release or not
            boolean shouldReleaseNextTime = true;
            if(expectCount == 1 || expectCount == 2){
                //强制合并成1个
                mergeToOne(list);
                count = computeCount(list);
                shouldReleaseNextTime = false;
            }else if(expectCount == 3){
                if(canReleaseHeadOrTail){
                    mergeToOne(list);
                }else{
                    //中间的合成1个. 头和尾保持1个.
                    final List<CountInfo> tmpList = new ArrayList<>(list);
                    tmpList.remove(0);
                    tmpList.remove(tmpList.size() - 1);
                    mergeToOne(tmpList);
                }
                count = computeCount(list);
                shouldReleaseNextTime = false;
            }else if(expectCount < size){
                final int expectReleaseCount = size - expectCount;
                //格子数 < 语句数, 需要先释放
                releaseIfNeed(size, list, expectReleaseCount, canReleaseHeadOrTail);
                //有可能没有释放完。需要强制释放(note head and tail)。
                if(mPreReleasedCount < expectReleaseCount){
                    //按照合并个数来排序。AESC. 升序
                    Collections.sort(list, new Comparator<CountInfo>() {
                        @Override
                        public int compare(CountInfo o1, CountInfo o2) {
                            return Integer.compare(o1.getRoundWithOffset(), o2.getRoundWithOffset());
                        }
                    });
                    //只能释放给相邻的
                    out:
                     for(CountInfo info : list){
                         if(info.isReleased()){
                             continue;
                         }
                         if(!canReleaseHeadOrTail && (info.sentenceIndex == 0 || info.sentenceIndex == size - 1)){
                             continue;
                         }
                         //start --- force release
                         int targetIndex = info.sentenceIndex - 1;
                         if(targetIndex < 0){
                             targetIndex = 0;
                         }
                         if(!canReleaseHeadOrTail && targetIndex == 0){
                             targetIndex ++;
                         }
                         //can't release to self
                         if(targetIndex == info.sentenceIndex){
                             targetIndex ++;
                         }
                         CountInfo target = findCountInfo(list, targetIndex);
                         if(target == null){
                             break;
                         }
                         //may be self
                         while (target == info){
                             targetIndex ++;
                             target = findCountInfo(list, targetIndex);
                             if(target == null) {
                                 break out;
                             }
                         }
                         merge0(target, info);
                         mPreReleasedCount ++;
                         if(mPreReleasedCount == expectReleaseCount){
                             break;
                         }
                     }
                }
                if(mPreReleasedCount < expectReleaseCount){
                    throw new IllegalStateException("pre release failed. expect = " + expectReleaseCount
                            + " ,real released = " + mPreReleasedCount);
                }
                //重新计算个数
                count = computeCount(list);
            }
            //adjust plaid count in target sentence.
            if(count < expectCount){
                int offsetCount = expectCount - count;
                final int len = list.size();
                //the main step.
                final int mainStep = offsetCount / len;
                if(mainStep > 0) {
                    for (int i = 0; i < len; i++) {
                        CountInfo info = list.get(i);
                        if (info.isReleased()) {
                            continue;
                        }
                        info.offset += mainStep;
                        offsetCount -= mainStep;
                        if (offsetCount == 0) {
                            break;
                        }
                    }
                }
                if(offsetCount > 0) {
                    //may have remainder(余数)
                    for (int i = 0; i < len; i++) {
                        CountInfo info = list.get(i);
                        if (info.isReleased()) {
                            continue;
                        }
                        info.offset += 1;
                        offsetCount -= 1;
                        if (offsetCount == 0) {
                            break;
                        }
                    }
                }
            }else if(count > expectCount){
                int offsetCount = count - expectCount;
                for(int i = 0, len = list.size() ; i < len ; i++ ){
                    CountInfo info = list.get(i);
                    if(info.isReleased()){
                        continue;
                    }
                    //prefer handle round up
                    if(!info.isRawInt() && info.isRoundUp() && info.canDecrease()){
                        info.offset -= 1;
                        offsetCount --;
                        if(offsetCount == 0){
                            break;
                        }
                    }
                }
                //if not handle done. handle raw int.
                if(offsetCount > 0){
                    for(int i = 0, len = list.size() ; i < len ; i++ ){
                        CountInfo info = list.get(i);
                        if(info.isReleased()){
                            continue;
                        }
                        if(!info.hasOffset() && info.canDecrease()){
                            info.offset -= 1;
                            offsetCount --;
                            if(offsetCount == 0){
                                break;
                            }
                        }
                    }
                }
                //already not done
                //sort as round size . desc
                Collections.sort(list, (o1, o2) ->
                        Integer.compare(o2.getRoundWithOffset(), o1.getRoundWithOffset())
                );
                while (offsetCount > 0) {
                    for (int i = 0, len = list.size(); i < len; i++) {
                        CountInfo info = list.get(i);
                        if(info.isReleased()){
                            continue;
                        }
                        if (info.canDecrease()) {
                            info.offset -= 1;
                            offsetCount--;
                            if (offsetCount == 0) {
                                break;
                            }
                        }
                    }
                }
            }
            Logger.d(TAG, "adjustPlaidCount", "after adjust >>> count = " + count + " ,expected count = "
                    + expectCount + ", " + list.toString());
            Logger.d(TAG, "adjust", "dump adjust info: " + list);
            if(shouldReleaseNextTime) {
                //normal release if not protect sentence.
                if(!protect_sentence) {
                    releaseIfNeed(size, list, 0, canReleaseHeadOrTail);
                }
                //release air at last, need sort by sentence index order
                Collections.sort(list, (o1, o2) -> Integer.compare(o1.sentenceIndex, o2.sentenceIndex));
                releaseAirSentence(size, list, canReleaseHeadOrTail);
            }else{
                Collections.sort(list, (o1, o2) -> Integer.compare(o1.sentenceIndex, o2.sentenceIndex));
            }

            //remove released
            removeReleased(list);
            return list;
        }

        private void mergeToOne(List<CountInfo> list) {
            Collections.sort(list, mValueCMP);
            //直接合成1个
            CountInfo owner = list.get(list.size() - 1);
            for(CountInfo info : list){
                if(info != owner){
                    merge0(owner, info);
                }
            }
        }

        private void releaseAirSentence(int sentenceSize, List<CountInfo> list, boolean canReleaseHeadOrtail) {
            final String TAG_AIR = "AirSentence";
            final String method = "AirSentence__" + expectCount;
            //create new list .and remove released.
            final ArrayList<CountInfo> tempList = new ArrayList<>(list);
            for(Iterator<CountInfo> it = tempList.iterator(); it.hasNext();){
                if(it.next().isReleased()){
                    it.remove();
                }
            }
            final int temp_size = tempList.size();

            out:
            for(int i = 0, len = list.size() ; i < len ; i++ ) {
                CountInfo info = list.get(i);
                //不能合并/不支持合并了 ---
                if(!info.isEnableMerge()){
                    continue;
                }
                if(info.isReleased()){
                    continue;
                }
                VETemplate.LogicSentence sentence = sentences.get(info.sentenceIndex);
                if(!sentence.isAir()){
                    continue;
                }
                //should release or not.
                final boolean shouldRelease;
                if(i == 0 || i == len - 1){
                    throw new IllegalStateException("air sentence can't appeared as head or tail. ");
                }
                //空镜头 不能出现在头和尾 及其相邻的地方 ?
                final int index_info = tempList.indexOf(info);
                if(index_info == 0 || index_info == temp_size - 1){
                    shouldRelease = false;
                    Logger.v(TAG_AIR, method, "is Adjacent with head or tail(after) .sentence_index = "
                            + info.sentenceIndex);
                }else {
                    CountInfo info_pre = tempList.get(index_info - 1);
                    CountInfo info_after = tempList.get(index_info + 1);
                    shouldRelease = info.getRoundWithOffset() * 10 > (info_pre.getRoundWithOffset() + info_after.getRoundWithOffset());
                    Logger.v(TAG_AIR, method, "air count = " + info.getRoundWithOffset()
                            + " ,pre_count = " + info_pre.getRoundWithOffset() + " ,after_count = " + info_after.getRoundWithOffset());
                }
                if(shouldRelease){
                    Logger.d(TAG_AIR, method, "start release air. sentence_index = " + info.sentenceIndex);
                    boolean usePre = true;
                    int index = info.sentenceIndex - 1;
                    if(index <= 0){
                        index = info.sentenceIndex + 1;
                        usePre = false;
                    }
                    CountInfo target = findCountInfo(list, index);
                    if(target == null){
                        break;
                    }
                    //exclude self
                    if(target == info){
                        index = usePre ? info.sentenceIndex + 1 : index + 1;
                        target = findCountInfo(list, index);
                        if(target == null){
                            break;
                        }
                        while(target == info){
                            index ++;
                            target = findCountInfo(list, index);
                            if(target == null){
                                break out;
                            }
                        }
                    }
                    //头尾重要，则不能释放到头尾上
                    if(!canReleaseHeadOrtail){
                        if(target.sentenceIndex == 0) {
                            index += 2;
                            try {
                                target = findCountInfo(list, index);
                                if(target == null){
                                    break;
                                }
                            } catch (IllegalStateException e) {
                                throw new RuntimeException("VE-Template error, air-shot can't released.");
                            }
                        }else if(target.sentenceIndex == sentenceSize - 1){
                            throw new RuntimeException("VE-Template error, air-shot can't released.");
                        }
                    }
                    //不相邻, 不合并
                    if(!target.isAdjacent(info)){
                        info.enableMerge(false);
                        Logger.d(TAG_AIR, method, "不相邻，不合并。info.sentenceIndex = " + info.sentenceIndex);
                        continue;
                    }
                    merge0(target, info);
                }
            }
        }

        private void merge0(CountInfo owner, CountInfo other) {
            //when merged other, mark/reset enable true.
            owner.enableMerge(true);
            owner.addMergeCountInfo(other);
            //compute the additional weight of the owner
            VETemplate.LogicSentence ls_owner = sentences.get(owner.sentenceIndex);
            ls_owner.addAdditionalWeight(sentences.get(other.sentenceIndex).getActualWeight());
        }

        /** expectReleaseCount 期望释放的个数，如果为0，则按照比例来决定是否释放
         * 即是分2种工作模式： 按照个数来释放， 按照比例来释放。
         * */
        private void releaseIfNeed(int sentenceSize, List<CountInfo> list, int expectReleaseCount, boolean canReleaseHeadOrTail) {
            //check merge.
            Collections.sort(list, mValueCMP);
            //do release
            int wheel = 1;
            boolean noNeedRelease;
            do{
                noNeedRelease = doReleaseSentence(sentenceSize, list, wheel, expectReleaseCount, canReleaseHeadOrTail);
                wheel++;
            }while (!noNeedRelease);

            if(DEBUG) {
                Logger.i(TAG, "releaseIfNeed", "after normal release( expectReleaseCount = " + expectReleaseCount + ") >>> ");
                for (CountInfo info : list) {
                    info.dumpSentenceIndexes();
                }
                Logger.i(TAG, "releaseIfNeed", "after normal release( expectReleaseCount = " + expectReleaseCount + ") <<< ");
            }
        }

        private boolean canReleaseHeadOrTail(int sentenceSize, List<CountInfo> list) {
            final boolean canReleaseHeadOrtail;
            //judge the head and tail can release or not.
            int weight_headtail = 0;
            int weight_core = 0;
            for (CountInfo info : list) {
                int weight = sentences.get(info.sentenceIndex).getWeight();
                if (info.sentenceIndex == 0 || info.sentenceIndex == sentenceSize - 1) {
                    //head or tail
                    weight_headtail += weight;
                } else {
                    weight_core += weight;
                }
            }
            canReleaseHeadOrtail = weight_core < 2 * weight_headtail;
            return canReleaseHeadOrtail;
        }

        private void removeReleased(List<CountInfo> list) {
            // remove released info.
            for(Iterator<CountInfo> it = list.iterator(); it.hasNext(); ){
                final CountInfo info = it.next();
                if(info.isReleased()){
                    it.remove();
                    //clear/format effects
                    //sentences.get(info.sentenceIndex).clearEffects();
                    info.dumpSentenceIndexes();
               }else{ //head and tail may only have one
                    info.dumpSentenceIndexes();
                }
            }
        }

        /** return true, if no need release */
        private boolean doReleaseSentence(int sentenceSize, List<CountInfo> list, int wheel,
                                          int expectReleaseCount, boolean canReleaseHeadOrtail) {
            //expectReleaseCount == 0 means normal释放模式， 否则 为释放次数模式。
            final boolean modeIsCount = expectReleaseCount > 0;
            boolean noNeedRelease = true;
            out:
            for(int i = 0, len = list.size() ; i < len ; i++ ) {
                CountInfo info = list.get(i);
                //maybe头尾不释放
                if(!canReleaseHeadOrtail && (info.sentenceIndex == 0 || info.sentenceIndex == sentenceSize -1) ){
                    continue;
                }
                //每次合并（释放）的次数不能超过wheel(模式按照比例来)
                if(!modeIsCount && info.getMergedCount() == wheel){
                    continue;
                }
                //不能合并/不支持合并了 ---
                if(!info.isEnableMerge()){
                    continue;
                }
                if(info.isReleased()){
                    continue;
                }
                //air sentence will merged at last.
                if(!modeIsCount && sentences.get(info.sentenceIndex).isAir()){
                    continue;
                }
                //should release or not.
                final boolean shouldRelease = info.shouldReleasePlaid();
                //孤镜头(非空镜头)应该合并(but not in release count mode)
                final boolean single_plaid = (info.getRoundMinOne() + info.offset) == 1;

                if(shouldRelease || (!modeIsCount && single_plaid)){
                    noNeedRelease = false;
                    boolean usePre = true;
                    int index = info.sentenceIndex - 1;
                    if(index <= 0){
                        index = info.sentenceIndex + 1;
                        usePre = false;
                    }
                    CountInfo target = findCountInfo(list, index);
                    if(target == null){
                        noNeedRelease = true;
                        break;
                    }
                    //exclude self
                    if(target == info){
                        index = usePre ? info.sentenceIndex + 1 : index + 1;
                        target = findCountInfo(list, index);
                        if(target == null){
                            noNeedRelease = true;
                            break;
                        }
                        usePre = false;
                        while(target == info){
                            index ++;
                            target = findCountInfo(list, index);
                            if(target == null){
                                noNeedRelease = true;
                                break out;
                            }
                        }
                    }
                    //头尾重要，则不能释放到头尾上
                    if(!canReleaseHeadOrtail){
                        if(target.sentenceIndex == 0) {
                            index += 2;
                            try {
                                target = findCountInfo(list, index);
                                if(target == null){
                                    noNeedRelease = true;
                                    break;
                                }
                            } catch (IllegalStateException e) {
                                throw new RuntimeException("VE-Template error, release failed. index = " + index);
                            }
                        }else if(target.sentenceIndex == sentenceSize - 1){
                            throw new RuntimeException("VE-Template error, can't released to tail.");
                        }
                    }
                    //如果单个格子，则不检查合并次数。
                    if(!modeIsCount && !single_plaid) {
                        //限制合并次数,保证合并的差异不要太大(air 也要排除-- 正常模式下)
                        while (target.getMergedCount() > info.getMergedCount() ||
                                (sentences.get(target.sentenceIndex).isAir()) ) {
                            if (usePre) {
                                usePre = false;
                                index = info.sentenceIndex + 1;
                            } else {
                                index++;
                            }
                            if (index == sentenceSize - 1) {
                                Logger.w(TAG, "doReleaseSentence", "adjust merge index failed. sentence index = "
                                        + info.sentenceIndex);
                                break;
                            }
                            target = findCountInfo(list, index);
                            if(target == null){
                                noNeedRelease = true;
                                break out;
                            }
                        }
                    }
                    //不相邻, 不合并
                    if(!target.isAdjacent(info)){
                        info.enableMerge(false);
                        Logger.d(TAG, "doReleaseSentence", "不相邻，不合并。info.sentenceIndex = " + info.sentenceIndex);
                        continue;
                    }
                    merge0(target, info);
                    //如果释放模式是个数。
                    if(expectReleaseCount > 0){
                        mPreReleasedCount++;
                        if(mPreReleasedCount == expectReleaseCount){
                            return true;
                        }
                    }
                }
            }
            return noNeedRelease;
        }
        /** return null. if can't find */
        CountInfo findCountInfo(List<CountInfo> list, int sentenceIndex)throws IllegalStateException{
            if(sentenceIndex < 0) throw new IllegalArgumentException();
            for(CountInfo info : list){
                if(info.sentenceIndex == sentenceIndex){
                    if(info.isReleased()){
                        return findCountInfo(list, info.getOwnerSentenceIndex());
                    }
                    return info;
                }
            }
            Logger.d(TAG, "findCountInfo", "can't find for sentenceIndex = " + sentenceIndex);
            //throw new IllegalStateException("expect sentence index = " + sentenceIndex);
            return null;
        }
    }

    private static int computeCount(List<CountInfo> list) {
        int count = 0;
        for (CountInfo info : list){
            if(info.isReleased()){
                continue;
            }
            //after release, offset may have.
            count += info.getRoundWithOffset();
        }
        return count;
    }

    /**
     * the count info of shader.
     */
    public static class CountInfo{
        /**  the raw count of the sentence */
        final int rawCount;
        /** scaled count in sentence , may be changed by merged*/
        private float scaledCount;
        int offset;

        /** the core sentence index */
        final int sentenceIndex;
        /** the merged count infoes */
        private List<CountInfo> mergedSentenceInfoes;
        /** if released to neighbour  */
        private boolean released;
        /** indicate this sentence index is merged to target sentence index */
        private int ownerSentenceIndex = -1;
        /** the source dir from {@linkplain com.heaven7.ve.colorgap.RawScriptItem} */
        private  String sourceDir;

        private int mergedCount;
        private boolean enableMerge = true;


        public CountInfo(int sentenceIndex, int rawCount, float scaledCount) {
            this.sentenceIndex = sentenceIndex;
            this.rawCount = rawCount;
            this.scaledCount = scaledCount;
        }

        public int getOwnerSentenceIndex() {
            return ownerSentenceIndex;
        }
        public void setOwnerSentenceIndex(int ownerSentenceIndex) {
            this.ownerSentenceIndex = ownerSentenceIndex;
        }

        public void addMergeCountInfo(CountInfo info){
            if(info.isReleased()){
                throw new IllegalStateException();
            }
            info.setReleased(true);
            info.setOwnerSentenceIndex(sentenceIndex);

            if(mergedSentenceInfoes == null){
                mergedSentenceInfoes = new ArrayList<>();
            }
            mergedSentenceInfoes.add(info);
            if(info.getMergedMergeCountInfoes() != null && info.getMergedMergeCountInfoes().size() > 0){
                for (CountInfo info1 : info.getMergedMergeCountInfoes()){
                    info1.setOwnerSentenceIndex(sentenceIndex);
                }
                mergedSentenceInfoes.addAll(info.getMergedMergeCountInfoes());
            }
            this.offset += (info.getRoundMinOne() + info.offset);
            this.mergedCount += (info.mergedCount + 1);
        }
        public int getMergedCount(){
            return mergedCount;
        }
        public List<CountInfo> getMergedMergeCountInfoes(){
            return mergedSentenceInfoes;
        }
        public int getFloor(){
            return (int) scaledCount;
        }
        public int getCell(){
            return (int) Math.ceil(scaledCount);
        }
        public int getRound(){
            return Math.round(scaledCount);
        }

        public int getRoundWithOffset(){
            return getRoundMinOne() + offset;
        }
        public int getRoundMinOne(){
            int count = Math.round(scaledCount);
            return count >= 1 ? count : 1;
        }
        public boolean isRawInt(){
            return getFloor() == getCell();
        }
        //eg: round(4.6) > 4.6
        public boolean isRoundUp(){
            return getRound() > getFloor();
        }

        public boolean hasOffset() {
            return offset != 0;
        }
        public boolean canDecrease() {
            return (getRoundMinOne() + offset) > 1;
        }

        public void setReleased(boolean released) {
            this.released = released;
        }
        public boolean isReleased() {
            return released;
        }
        /** the wheel of release */
        public boolean shouldReleasePlaid() {
           // Logger.d(TAG, "shouldReleasePlaid", "merge percent = 1 / " + (mergedCount + 3 ));
            //TODO release sentence have bugs . when raw sentence 30,40,30 -> 11(total)
            return getRound() + offset < rawCount * 1f / (mergedCount + 3);
        }
        /** indicate the count info is adjacent with current or not. */
        public boolean isAdjacent(CountInfo info) {
            int[] arr = this.computeExtremeValueOfSentenceIndex();
            int[] arr2 = info.computeExtremeValueOfSentenceIndex();
            //1-3  4-5
            return arr[1] == arr2[0] - 1 || arr2[1] == arr[0] - 1;
        }
        //[0] is min, [1] is max
        private int[] computeExtremeValueOfSentenceIndex() {
            int[] result = new int[2];
            result[0] = sentenceIndex;
            result[1] = sentenceIndex;
            if(mergedSentenceInfoes != null){
                for(CountInfo info : mergedSentenceInfoes){
                    if(info.sentenceIndex < result[0]){
                        result[0] = info.sentenceIndex;
                    }else if(info.sentenceIndex > result[1]){
                        result[1] = info.sentenceIndex;
                    }
                }
            }
            return result;
        }

        public void enableMerge(boolean enableMerge) {
            this.enableMerge = enableMerge;
        }

        public boolean isEnableMerge() {
            return enableMerge;
        }

        public void setSourceDir(String dir) {
            this.sourceDir = dir;
        }
        public String getSourceDir() {
            return sourceDir;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CountInfo countInfo = (CountInfo) o;
            return sentenceIndex == countInfo.sentenceIndex;
        }

        @Override
        public String toString() {
            return "CountInfo{" +
                    "sentenceIndex =" + sentenceIndex +
                    ", scaledCount =" + scaledCount +
                    ", offset =" + offset +
                    ", merged =" + mergedSentenceInfoes +
                    '}';
        }

        public void dumpSentenceIndexes() {
            StringBuilder sb = new StringBuilder();
            sb.append("core sentence_index = ")
                    .append(sentenceIndex).append(" ,");
            sb.append("released = ").append(isReleased()).append(", ");
            if(!isReleased()){
                if (mergedSentenceInfoes != null && mergedSentenceInfoes.size() > 0 ){
                    sb.append("merged { ");
                    for (CountInfo info : mergedSentenceInfoes){
                        sb.append(info.sentenceIndex).append(", ");
                    }
                    sb.append("} ");
                }
            }else{
                sb.append("owner = ").append(ownerSentenceIndex);
            }
            Logger.d(TAG, "dumpSentenceIndexes", "" + sb.toString());
        }
    }
}
