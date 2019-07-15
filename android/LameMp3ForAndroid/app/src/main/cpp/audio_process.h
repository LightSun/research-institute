//
// Created by Administrator on 2019/7/12.
//

#ifndef LAMEMP3FORANDROID_AUDIO_PROCESS_H
#define LAMEMP3FORANDROID_AUDIO_PROCESS_H

#include <sstream>
#include <iostream>
#include <vector>

using namespace std;

// this file used to build blocked audio data and wait latter get
//when define the struct or class we often should align . or else may cause crash.
//see: https://www.cnblogs.com/mlj318/p/6089001.html
//#pragma pack(push) //keep align
//#pragma pack(8)   //align with 4 b

class FD{
public:
    float *data;
    /** total size of data */
    int size;
    /** the used count of data. */
    int used;

    FD(){}
    ~FD(){
        delete[] data;
    }

    bool isFull(){
        return FD::size == FD::used;
    }
/** get left size which not used */
    int getLeft(){
        return FD::size - FD::used;
    }
};

class BlockList {
private:
    vector<FD *> list;

public:

    inline FD *getTail() {
        return getAt(size() - 1);
    }

    inline size_t size() {
        return list.size();
    }

    inline vector<FD *> &getVector() {
        return list;
    }

    FD *getAt(int index) {
        if (index >= size()) {
            return nullptr;
        }
        return list[index];
    }

    void add(FD *t) {
        list.push_back(t);
    }

    void add(int index, FD *t) {
        list.insert(list.begin() + index, t);
    }

    bool isEmpty() {
        return size() == 0;
    }

    inline void set(int index, FD *newFD) {
        if (index >= size()) {
            return;
        }
        list[index] = newFD;
    }

    inline void clear() {
        size_t size = BlockList::size();
        if (size > 0) {
            remove(0, (int) size);
        }
    }

    inline void removeAt(int index) {
        remove(index, 1);
    }

    inline void remove(int startIndex, int count) {
        long size = list.size();
        if (count <= 0 || startIndex < 0) {
            return;
        }
        list.erase(list.begin() + startIndex, list.begin() + count);
    }
};
//#pragma pack(pop)
/**
 * start pre process audio
 * @param blockSize the expect block size of audio data
 */
void startPreProcessAudio(int blockSize);

/**
 * add audio data
 * @param data the audio data
 * @param start the start position
 * @param size the length of data
 */
void addAudioData(float* data, int start,int size);

void endPreProcessAudio();

/** check next block data is enough or not */
bool isNextBlockDataPrepared();

/** return the next blocked sample size */
int testNextBlockedSampleSize();
/**
 * get the next block data.
 * @param out the out data
 * @return the sample size of out data.
 */
int nextBlockedAudioData(float * out);

 void releaseAudioData();

#endif //LAMEMP3FORANDROID_AUDIO_PROCESS_H
