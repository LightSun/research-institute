//
// Created by Administrator on 2019/7/11.
//

#ifndef ANDROIDCHORDINO_CHORDINOMEDIA_H
#define ANDROIDCHORDINO_CHORDINOMEDIA_H

#include <stdarg.h>
#include "string"

#pragma pack(push)
#pragma pack(8) //4字节对齐
typedef struct{
    int sampleRate;
    int channelCount;
} MediaData;

typedef void (*Log)(const char *tem, ...);

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
#pragma pack(pop)

typedef void (*LogOnLevel)(int level, const char *tem, ...);

void setLogEnable(int enable);
void setLog(Log log);
Log getLog();

void setLogOnLevel(LogOnLevel ll);
LogOnLevel getLogOnLevel();


#endif //ANDROIDCHORDINO_CHORDINOMEDIA_H
