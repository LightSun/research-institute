//
// Created by Administrator on 2019/7/18.
//

#ifndef ANDROIDCHORDINO_MP3_UPPERLAYYER_H
#define ANDROIDCHORDINO_MP3_UPPERLAYYER_H

#include "../chordinomedia/chordinomedia.h"

void* up_openMedia(const char* file, MediaData* out);

void up_logError(const char* file, void * openResult);

/**
 * read the media data for target count. every is a float
 * @param openResult
 * @param filebuf
 * @param count
 * @return the count of read
 */
int up_readMediaData(void* openResult, float *filebuf, int count);

void up_releaseMedia(void* openResult);

#endif //ANDROIDCHORDINO_MP3_UPPERLAYYER_H
