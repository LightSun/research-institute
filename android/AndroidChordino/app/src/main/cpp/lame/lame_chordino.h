//
// Created by Administrator on 2019/7/11.
//

#ifndef LAMEMP3FORANDROID_LAME_CHORDINO_H
#define LAMEMP3FORANDROID_LAME_CHORDINO_H

#include "../chordinomedia/chordinomedia.h"

void *lame_OpenMedia(const char *file, MediaData *out);

void lame_LogError(const char *file, void *openResult);

/**
 * read the media data for target count. every is a float
 * @param openResult
 * @param filebuf
 * @param count
 * @return the count of read
 */
int lame_ReadMediaData(void *openResult, float *filebuf, int count);

void lame_ReleaseMedia(void *openResult);

#endif //LAMEMP3FORANDROID_LAME_CHORDINO_H
