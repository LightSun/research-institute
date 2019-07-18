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


BlockList __blockList;
/** the size of audio date. if sample is 100. and stereo is 2. the block size is '100* 2' */
int __blockSize;
int __blockIndex;

unsigned int totalCount;

FD* newBlockData(){
    FD *fd = new FD();
    fd->data = new float[__blockSize];
    fd->size = __blockSize;
    fd->used = 0;
    return fd;
}

void startPreProcessAudio(int blockSize) {
    if(__blockList.isEmpty()){
        Log log = getLog();
        if(log != nullptr){
            log("startPreProcessAudio >>> blockSize = %d", blockSize);
        }
        totalCount = 0;
        __blockIndex = -1;
        __blockSize = blockSize;
        __blockList.add(newBlockData());
    }
}

//add size of audio data from start.
void addAudioData(short *data, int start, int size) {
    const size_t blockSize = __blockList.size();
    FD * tail = __blockList.getTail();
    Log log = getLog();
    if(start == 0 ){
        totalCount += size;
       /* if(log != nullptr){
            log("addAudioData >>> size is %d.", size);
        }*/
    }
    if(!tail->isFull()){
        const int left = tail->getLeft();

        if(size <= left){
            copyFloatArray(data, start, tail->data, tail->used, size);
            tail->used += size;
           /* if(log != nullptr){
                log("addAudioData [ size <= left ] >>> on The %d audio-block( >=1 ), from %d to %d. blockSize is %d.",
                        blockSize, tail->used - size, tail->used, tail->size);
            }*/
            if(tail->isFull()){
                __blockList.add(newBlockData());
            }
        }if(size > left){
            //not enough to copy all data once
            copyFloatArray(data, start, tail->data, tail->used, left);
            tail->used = tail->size;
            /*if(log != nullptr){
                log("addAudioData [ size > left ] >>> on The %d audio-block( >=1 ), from %d to %d. blockSize is %d.",
                    blockSize, tail->used - size, tail->used, tail->size);
            }*/
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
    Log const log = getLog();
    if(log != nullptr){
        log("total read counts = %d", totalCount);
    }
    totalCount = 0;
}

bool isNextBlockDataPrepared(){
    FD* fd = __blockList.getAt(__blockIndex + 1);
    return fd != nullptr && fd->isFull();
}

int testNextBlockedSampleSize(){
    FD* fd = __blockList.getAt(__blockIndex + 1);
    return fd != nullptr ? fd->used / 2 : -1;
}

int nextBlockedAudioData(float *out) {
    __blockIndex ++;
    FD* fd = __blockList.getAt(__blockIndex);
    if(fd == nullptr){
        return -1;
    }
   /* Log const log = getLog();
    if(log != nullptr){
        log("nextBlockedAudioData >>> index = %d, sample_size = %d", __blockIndex - 1, fd->used / 2);
    }*/
    copyFloatArray(fd->data, 0, out, 0, fd->used);
    return fd->used;
}

void releaseAudioData(){
    int size = __blockList.size();
    for (int i = size - 1; i >= 0; i--) {
        FD *const fd = __blockList.getAt(i);
        delete(fd);
    }
    __blockList.clear();
}