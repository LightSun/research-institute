//
// Created by Administrator on 2019/7/11.
//

#ifndef ANDROIDCHORDINO_SND_MEDIA_H
#define ANDROIDCHORDINO_SND_MEDIA_H

#include "chordinomedia.h"
#include "sndfile.h"

void* snd_openMedia(const char* file, MediaData* out);

void snd_logError(const char* file, void * openResult);

/**
 * read the media data for target count. every is a float
 * @param openResult
 * @param filebuf
 * @param count
 * @return the count of read
 */
int snd_readMediaData(void* openResult, float *filebuf, int count);

void snd_releaseMedia(void* openResult);


#endif //ANDROIDCHORDINO_SND_MEDIA_H
