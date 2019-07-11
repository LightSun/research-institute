//
// Created by Administrator on 2019/7/11.
//

#ifndef ANDROIDCHORDINO_CHORDINOMEDIA_H
#define ANDROIDCHORDINO_CHORDINOMEDIA_H

#include <stdarg.h>
#include "string"

typedef struct{
    int sampleRate;
    int channelCount;
} MediaData;

extern void log(const char *tem, ...);

//typedef void (*__log)(const char* str,...);
typedef void *(*OpenMedia)(const char *file, MediaData *out);
typedef void (*LogError)(const char *file, void *openResult);
/**
 * read the media data for target count. every is a float
 * @param openResult
 * @param filebuf
 * @param count
 * @return the count of read
 */
typedef int (*ReadMediaData)(void *openResult, float *filebuf, int count);
typedef void (*ReleaseMedia)(void *openResult);
struct MediaFormat {
    OpenMedia openMedia;
    LogError logError;
    ReadMediaData readMediaData;
    ReleaseMedia releaseMedia;
    std::string formats[2];
    int formatCount;
};

bool travelStrings(std::string* formats, int c, void* param, bool (*travel)(int,const std::string*, void*));

void registerMediaFormats(MediaFormat** formats, int count);
void releaseMediaFormats();
MediaFormat* getMediaFormat(const char* filename);


#endif //ANDROIDCHORDINO_CHORDINOMEDIA_H
