//
// Created by Administrator on 2019/7/11.
//

#include <sstream>
#include "formats.h"
#include "string"

#include "sndmedia.h"
#include "lame_chordino.h"
#include "mp3ext/mp3_upperlayyer.h"

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


MediaFormat* createMp3MediaFormat(){
    MediaFormat* mf = new MediaFormat();
    mf->formats[0] = "mp3";
    mf->formatCount = 1;

   /* mf->openMedia = up_openMedia;
    mf->logError = up_logError;
    mf->readMediaData = up_readMediaData;
    mf->releaseMedia = up_releaseMedia;*/
    mf->openMedia = lame_OpenMedia;
    mf->logError = lame_LogError;
    mf->readMediaData = lame_ReadMediaData;
    mf->releaseMedia = lame_ReleaseMedia;
    return mf;
}
