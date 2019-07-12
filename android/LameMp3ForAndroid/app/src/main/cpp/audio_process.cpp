//
// Created by Administrator on 2019/7/12.
//

#include "jni.h"
#include <sstream>
#include <iostream>
#include <vector>
#include "audio_process.h"
#include "array.h"
#include "chordinomedia.h"

using namespace std;

//when define the struct or class we often should align . or else may cause crash.
//see: https://www.cnblogs.com/mlj318/p/6089001.html
#pragma pack(push) //keep align
#pragma pack(8)   //align with 4 b

typedef struct FloatData {
    float *data;
    /** total size of data */
    int size;
    /** the used count of data. */
    int used;

    bool isFull(){
        return size == used;
    }
    /** get left size which not used */
    int getLeft(){
        return size - used;
    }
} FD;

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
            __throw_invalid_argument("start or end index is wrong");
        }
        list.erase(list.begin() + startIndex, list.begin() + count);
    }

    /*  void travel(VectorFDraveller<FD> &vt) {
          long size = list.size();
          for (int i = 0; i < size; i++) {
              vt.travel(i, list[i]);
          }
      }*/
};
#pragma pack(pop)

BlockList __blockList;
int __blockSize;
int __blockIndex;

FD* newBlockData(){
    FD *fd = new FD();
    fd->data = new float[__blockSize];
    fd->size = __blockSize;
    fd->used = 0;
    return fd;
}

extern "C" void startPreProcessAudio(int blockSize) {
    __blockSize = blockSize;
    __blockList.clear();
    __blockList.add(newBlockData());
}

//add size of audio data from start.
extern "C"  void addAudioData(float *data, int start, int size) {
    const size_t blockSize = __blockList.size();
    FD * tail = __blockList.getTail();
    if(!tail->isFull()){
        const int left = tail->getLeft();
        Log log = getLog();
        if(size <= left){
            copyFloatArray(data, start, tail->data, tail->used, size);
            tail->used += size;
            if(log != nullptr){
                log("addAudioData [ size <= left ] >>> on The %d audio-block( >=1 ), from %d to %d. blockSize is %d.",
                        blockSize, tail->used - size, tail->used, tail->size);
            }
            if(tail->isFull()){
                __blockList.add(newBlockData());
            }
        }if(size > left){
            //not enough to copy all data once
            copyFloatArray(data, start, tail->data, tail->used, left);
            tail->used = tail->size;
            if(log != nullptr){
                log("addAudioData [ size > left ] >>> on The %d audio-block( >=1 ), from %d to %d. blockSize is %d.",
                    blockSize, tail->used - size, tail->used, tail->size);
            }
            __blockList.add(newBlockData());
            //to handle the more data
            addAudioData(data, start + left, size - left);
        }
    } else{
        //should not reach here.
        __blockList.add(newBlockData());
        addAudioData(data, start, size);
    }
}

extern "C"  void endPreProcessAudio() {
    __blockIndex = -1;
}

extern "C"  int nextBlockedAudioData(float *out) {
    __blockIndex ++;
    FD* fd = __blockList.getAt(__blockIndex);
    if(fd == nullptr){
        return -1;
    }
    copyFloatArray(fd->data, 0, out, 0, fd->used);
    return fd->used;
}

extern "C"  void releaseAudioData(){
    int size = __blockList.size();
    for (int i = size - 1; i >= 0; i--) {
        FD *const fd = __blockList.getAt(i);
        delete[] fd->data;
        delete(fd);
    }
    __blockList.clear();
}