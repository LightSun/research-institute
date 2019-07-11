//
// Created by Administrator on 2019/7/11.
//

#include "formats.h"
#include "sndmedia.h"
#include "string"

MediaFormat* createSndMediaFormat(){
    MediaFormat* mf = new MediaFormat();
    mf->formats[0] = "wav";
    mf->formatCount = 1;
    mf->openMedia = snd_openMedia;
    mf->logError = snd_logError;
    mf->readMediaData = snd_readMediaData;
    mf->releaseMedia = snd_releaseMedia;
    return mf;
}
