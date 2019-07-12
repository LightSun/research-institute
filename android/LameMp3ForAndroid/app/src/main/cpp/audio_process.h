//
// Created by Administrator on 2019/7/12.
//

#ifndef LAMEMP3FORANDROID_AUDIO_PROCESS_H
#define LAMEMP3FORANDROID_AUDIO_PROCESS_H

// this file used to build blocked audio data and wait latter get

/**
 * start pre process audio
 * @param blockSize the expect block size of audio data
 */
extern "C"  void startPreProcessAudio(int blockSize);

/**
 * add audio data
 * @param data the audio data
 * @param start the start position
 * @param size the length of data
 */
extern "C" void addAudioData(float* data, int start,int size);

extern "C" void endPreProcessAudio();

/**
 * get the next block data.
 * @param out the out data
 * @return the size of out data.
 */
extern "C" int nextBlockedAudioData(float * out);

extern "C" void releaseAudioData();

#endif //LAMEMP3FORANDROID_AUDIO_PROCESS_H
