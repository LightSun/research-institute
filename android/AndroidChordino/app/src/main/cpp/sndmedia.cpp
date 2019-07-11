//
// Created by Administrator on 2019/7/11.
//

#include "sndmedia.h"

void* snd_openMedia(const char* file, MediaData* out){
    SF_INFO sfinfo;
    SNDFILE *sndfile = sf_open(file, SFM_READ, &sfinfo);
    out->channelCount = sfinfo.channels;
    out->sampleRate = sfinfo.samplerate;

    Log log = getLog();
    if(log != nullptr) {
        log("frames = %d", sfinfo.frames);
    }
    return sndfile;
}

void snd_logError(const char* file, void * openResult){

    SNDFILE *sndfile = static_cast<SNDFILE *>(openResult);
    const char * error = sf_strerror(sndfile);
    //cerr << myname << ": Failed to open input file " << infile<< ": " << sf_strerror(sndfile) << endl;
    Log log = getLog();
    if(log != nullptr) {
        log("Failed to open input file. file = %s. error is %s", file, error);
    }
}

/**
 * read the media data for target count. every is a float
 * @param openResult
 * @param filebuf
 * @param count
 * @return the count of read
 */
int snd_readMediaData(void* openResult, float *filebuf, int count){
    SNDFILE *sndfile = static_cast<SNDFILE *>(openResult);
    return sf_readf_float(sndfile, filebuf, count);
}

void snd_releaseMedia(void* openResult){
    SNDFILE *sndfile = static_cast<SNDFILE *>(openResult);
    sf_close(sndfile);
}

