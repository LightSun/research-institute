//
// Created by Administrator on 2019/7/25.
//

#include "cut_gen_strategy.h"
#include "cut_generator.h"
#include "../chordinomedia/chordinomedia.h"


CutContext* createCutContext(){
    CutContext* context = new CutContext();
    context->minShotTimeMsec = 300;
    context->maxShotTimeMsec = 5000;
    return context;
}

int SplitStrategyImpl(CutContext *rp, ChordInfo* start, ChordInfo* end, List<ChordInfo*>* out){
    List<ChordInfo*> temp;
    temp.add(start);
    temp.add(end);
    int index = 1;

    ChordInfo* st = start; ChordInfo* en = end;

    int delta = en->timemsec - st->timemsec;
    int count = delta / rp->maxShotTimeMsec;
    if(count >= 1){
        int average = delta / (count + 1);
        for (int i = 0; i < count; ++i) {
            ChordInfo *const pInfo = rp->pool.create();
            pInfo->timemsec = st->timemsec + (i + 1) * average;
            temp.add(index, pInfo);
            index ++;
            rp->msg += " --insert = ";
            rp->msg += pInfo->toString();
            rp->msg += "\n";
        }
        Log const log = getLog();
        if(log != nullptr){
            log("on [ SplitStrategy() ]: st = %s, end = %s, result = %s", start->toString().c_str(),
                end->toString().c_str(), temp.toString().c_str());
        }
    }
    *out = temp;
    return out->size() - 1;
}

int MergeStrategyImpl(CutContext *rp, List<ChordInfo*>* in, List<ChordInfo*>* out){
    ChordInfo *const &endInfo = in->getEnd();
    List<ChordInfo*> temp;

    Log const log = getLog();
    while(true){
        temp = *in;
        int count = 0;
        bool merged = false;
        for (int i = 0, len = (int)in->size(); i < len - 1; ++i) {
            ChordInfo *const &info1 = in->getAt(i);
            ChordInfo *const &info2 = in->getAt(i + 1);
            if(info2->timemsec - info1->timemsec < rp->minShotTimeMsec ){
                int index = i + 1 - count;
                ChordInfo *const &pInfo = temp.getAt(index);
                //not the end .need merge
                if(endInfo->timemsec != pInfo->timemsec){
                    temp.removeAt(index);
                    count ++;
                    rp->msg += " --removed =";
                    rp->msg += pInfo->toString();
                    rp->msg += "\n";
                    merged = true;

                    i++; // i+1 is already removed
                }
            }
        }
        if(log != nullptr){
            log("after MergeStrategyImpl >>> temp = %s", temp.toString().c_str());
        }
        //until no merges
        if(!merged){
            break;
        }
        *in = temp;
        temp.clear();
    }
    *out = temp;
    return (int)out->size() - 1;
}
