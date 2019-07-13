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

bool _isFull(FD* fd){
    return fd->size == fd->used;
}
/** get left size which not used */
int _getLeft(FD* fd){
    return fd->size - fd->used;
}


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

void startPreProcessAudio(int blockSize) {
    __blockSize = blockSize;
    __blockList.clear();
    __blockList.add(newBlockData());
}

//add size of audio data from start.
void addAudioData(float *data, int start, int size) {
    const size_t blockSize = __blockList.size();
    FD * tail = __blockList.getTail();
    Log log = getLog();
    if(!_isFull(tail)){
        const int left = _getLeft(tail);

        if(size <= left){
            copyFloatArray(data, start, tail->data, tail->used, size);
            tail->used += size;
            if(log != nullptr){
                log("addAudioData [ size <= left ] >>> on The %d audio-block( >=1 ), from %d to %d. blockSize is %d.",
                        blockSize, tail->used - size, tail->used, tail->size);
            }
            if(_isFull(tail)){
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

void endPreProcessAudio() {
    __blockIndex = -1;
}

int nextBlockedAudioData(float *out) {
    __blockIndex ++;
    FD* fd = __blockList.getAt(__blockIndex);
    if(fd == nullptr){
        return -1;
    }
    copyFloatArray(fd->data, 0, out, 0, fd->used);
    return fd->used;
}

void releaseAudioData(){
    int size = __blockList.size();
    for (int i = size - 1; i >= 0; i--) {
        FD *const fd = __blockList.getAt(i);
        delete[] fd->data;
        delete(fd);
    }
    __blockList.clear();
}