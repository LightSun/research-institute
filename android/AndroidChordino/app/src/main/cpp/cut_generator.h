//
// Created by Administrator on 2019/7/24.
//

#ifndef ANDROIDCHORDINO_CUT_GENERATOR_H
#define ANDROIDCHORDINO_CUT_GENERATOR_H

#include <sstream>
#include "vector"
#include "string"
#include "list.h"

namespace CUT_GEN {

    class ChordInfo {
    public:
        int timemsec;
        std::string label;
        std::string extras; // extra label.

        ChordInfo(){
            label = "";
            extras = "";
        }
        ~ChordInfo(){}

        const std::string toString() const{
            std::stringstream out;
            out << "time = " << timemsec;
            out << ", "  << "label = " << label;
            return out.str();
        };

        // like java .toString
        friend ostream& operator<<(ostream& ost, const ChordInfo& ci){
            //ost << ci << endl;
           // ost << ci.toString();
            ost << ci.timemsec;
            return ost;
        }

        ChordInfo &operator=(const ChordInfo &right) {
            if(this != &right){
                this->timemsec = right.timemsec;
                this->label = right.label;
                this->extras = right.extras;
            }
            return *this;
        }
    };

    class MusicInfo {
    public:
        List<ChordInfo*> chordInfos;
        MusicInfo(List<ChordInfo*>& chordInfos){
            this->chordInfos = chordInfos;
        }
        ~MusicInfo(){}
    };

    /**
     * a pool which create T* from context(C)
     * @tparam T the type of T
     * @tparam C the context
     */
    template <class T, typename C>
    class Pool{
        List<T*> ts;
    public:
        typedef T* (*Creator)(C context);
        Pool(){}
        ~Pool(){
            int size = (int)ts.size();
            for (int i= size -1; i >=0 ; i--) {
                T* e = ts.getAt(i);
                delete e;
            }
            ts.clear();
        }
        /**
         * create chord info which is pooled. that means it will be recycled. after pool object is de-constructor.
         * @return the object
         */
        T* create(C context, Creator creator){
            T* info = creator(context);
            ts.add(info);
            return info;
        }
        T* create(){
            T* info = new T();
            ts.add(info);
            return info;
        }
    };

    class CutContext {
    public:
        int shotCount;
        /** the min shot time. */
        int minShotTimeMsec;
        /** the max shot time. */
        int maxShotTimeMsec;

        /** the pool of ChordInfo */
        Pool<ChordInfo, int> pool;
        std::string msg;

        CutContext(){
            shotCount = 0;
            minShotTimeMsec = 0;
            maxShotTimeMsec = 0;
            msg = "";
        }
    };

    /**
     * split the the start chord and end chord to the out list.
     * @return the count of cut-areas
     */
    typedef int (*SplitStrategy)(CutContext *rp, ChordInfo* start, ChordInfo* end, List<ChordInfo*>* out);
    /**
    * merge the the input chords to out list.
    * @return the count of cut-areas
    */
    typedef int (*MergeStrategy)(CutContext *rp, List<ChordInfo*>* in, List<ChordInfo*>* out);

    class CutGenerator {
    public:
        SplitStrategy splitStrategy;
        MergeStrategy mergeStrategy;

        CutGenerator(){}
        ~CutGenerator(){
            splitStrategy = nullptr;
            mergeStrategy = nullptr;
        }
    };
    /**
     * generate cuts
     * @param rp the context
     * @param mi the music info
     * @param cg the cut generator
     * @param out the out cuts
     * @return the size of out cut-range.
     */
    int generateCuts(CutContext *rp, MusicInfo *mi, CutGenerator* cg ,List<ChordInfo*>* out);

}
#endif //ANDROIDCHORDINO_CUT_GENERATOR_H
