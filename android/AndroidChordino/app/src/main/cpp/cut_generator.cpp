//
// Created by Administrator on 2019/7/24.
//

#include "cut_generator.h"
#include "chordinomedia/chordinomedia.h"

/*
    typedef int (*SplitStrategy)(RequireParameter *rp, ChordInfo* start, ChordInfo* end, List<ChordInfo*>* out);
    typedef int (*MergeStrategy)(RequireParameter *rp, List<ChordInfo*> in, List<ChordInfo*>* out);
 */
namespace CUT_GEN {

    int generateCuts(CutContext *rp, MusicInfo *mi, CutGenerator* cg, List<ChordInfo*>* out){
        Log const log = getLog();
        if(cg->splitStrategy == nullptr){
             if(log != nullptr){
                 log("CutGenerator.splitStrategy can't be null.");
             }
             return 0;
         }
        if(cg->mergeStrategy == nullptr){
            if(log != nullptr){
                log("CutGenerator.mergeStrategy can't be null.");
            }
            return 0;
        }
         if(rp->shotCount <= 0){
             List<ChordInfo*> temp, list;
             //merge then split
             int count = cg->mergeStrategy(rp, &mi->chordInfos, &list);
             if(log != nullptr){
                 log("generateCuts >>> after merge. cut-area count is %d.", count);
             }
             List<ChordInfo*> result;
             result = list;
             list.clear();

             //split
             for (int i = 0, size = (int)result.size(); i < size - 1; ++i) {
                // *out[i]
                 ChordInfo* start = result.getAt(i);
                 ChordInfo* end = result.getAt(i + 1);

                 temp.clear();
                 count = cg->splitStrategy(rp, start, end, &temp);
                 if(log != nullptr){
                     log("generateCuts >>> after split, cut-area count is %d, start-end = (%s)-(%s).", count,
                         start->toString().c_str(), end->toString().c_str());
                     log("generateCuts >>> tmp = %s", temp.toString().c_str());
                 }
                 if(temp.size() > 0){
                     if(!list.isEmpty()){
                         ChordInfo *const &pInfo = list.getAt(list.size() - 1);
                         ChordInfo *const &outHead = temp.getAt(0);
                         if(pInfo->timemsec == outHead->timemsec){
                             list.removeEnd();
                         }
                     }
                     list.addAll(temp);
                     if(log != nullptr){
                         log("generateCuts -- addAll >>> list datas is %s", list.toString().c_str());
                     }
                 }
             }
             *out = list;
             return (int)list.size() - 1;
         } else{
             //todo latter will support this.
             return 0;
         }
    }
    //c++ use dynamic_cast like java instanceOf
}