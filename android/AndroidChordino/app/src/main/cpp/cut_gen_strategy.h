//
// Created by Administrator on 2019/7/25.
//

#ifndef ANDROIDCHORDINO_CUT_GEN_STRATEGY_H
#define ANDROIDCHORDINO_CUT_GEN_STRATEGY_H

#include "cut_generator.h"
#include "list.h"

using namespace CUT_GEN;

CutContext* createCutContext();

int SplitStrategyImpl(CutContext *rp, ChordInfo* start, ChordInfo* end, List<ChordInfo*>* out);

int MergeStrategyImpl(CutContext *rp, List<ChordInfo*>* in, List<ChordInfo*>* out);

#endif //ANDROIDCHORDINO_CUT_GEN_STRATEGY_H
