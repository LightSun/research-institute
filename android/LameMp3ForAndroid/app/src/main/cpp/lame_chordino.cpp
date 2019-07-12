//
// Created by Administrator on 2019/7/11.
//

#include "lame_chordino.h"
#include "lame.h"
#include <util.h>

#include "stdio.h"
#include "vector"
#include "audio_process.h"

static mp3data_struct *mp3data;
static hip_t hip_context = nullptr;
static int enc_delay, enc_padding;
bool readed = false;


int lame_config(char* buf, int size);

void __log(const char * str){
    Log log = getLog();
    if(log != nullptr){
        log(str);
    }
}

void __log(const char * tem, const char * str){
    Log log = getLog();
    if(log != nullptr){
        log(tem, str);
    }
}

static bool isId3Header(char*buf){
    return buf[0] == 'I' && buf[1] == 'D' && buf[2] == '3';
}

static bool isAIDHeader(char*buf){
    return buf[0] == 'A' && buf[1] == 'I' && buf[2] == 'D' && buf[3] == '\1';
}

static bool isMp123SyncWord(char* buf) {
    // function taken from LAME to identify MP3 syncword
    char abl2[] = { 0, 7, 7, 7, 0, 7, 0, 0, 0, 0, 0, 8, 8, 8, 8, 8 };
    if ((buf[0] & 0xFF) != 0xFF) {
        return false;
    }
    if ((buf[1] & 0xE0) != 0xE0) {
        return false;
    }
    if ((buf[1] & 0x18) == 0x08) {
        return false;
    }
    if ((buf[1] & 0x06) == 0x00) {
        // not layer I/II/III
        return false;
    }
    if ((buf[2] & 0xF0) == 0xF0) {
        // bad bitrate
        return false;
    }
    if ((buf[2] & 0x0C) == 0x0C) {
        // bad sample frequency
        return false;
    }
    if ((buf[1] & 0x18) == 0x18 &&
        (buf[1] & 0x06) == 0x04 &&
        (abl2[buf[2] >> 4] & (1 << (buf[3] >> 6))) != 0) {
        return false;
    }
    if ((buf[3] & 0x03) == 2) {
        // reserved emphasis mode (?)
        return false;
    }
    return true;
}

void *lame_OpenMedia(const char *filename, MediaData *out) {
    readed = false;
    if (!hip_context) {
        hip_context = hip_decode_init();
        if (hip_context) {
            mp3data = (mp3data_struct *) malloc(sizeof(mp3data_struct));
            memset(mp3data, 0, sizeof(mp3data_struct));
            enc_delay = -1;
            enc_padding = -1;
        } else{
            return nullptr;
        }
    }
//========================================
    size_t count = 100;
    int id3Length, aidLength;

    FILE *file = fopen(filename, "rb");
    if (file == nullptr) {
        __log("lame_OpenMedia failed (may not exist). for file = '%s'", filename);
        return nullptr;
    }
    char buf[count];

    if (fread(buf, 1, 4, file) != 4) {
        fclose(file);
        return nullptr;
    }
    if (isId3Header(buf)) {
        // ID3 header found, skip past it
        if (fread(buf, 1, 6, file) != 6) {
            __log("read id3 header error.");
            fclose(file);
            return nullptr;
        }
        buf[2] &= 0x7F;
        buf[3] &= 0x7F;
        buf[4] &= 0x7F;
        buf[5] &= 0x7F;
        id3Length = (((((buf[2] << 7) + buf[3]) << 7) + buf[4]) << 7) + buf[5];
        //skip id3Length
        if (fread(buf, 1, id3Length, file) != id3Length) {
            __log("skip id3Length error 1.");
            fclose(file);
            return nullptr;
        }
        if (fread(buf, 1, 4, file) != 4) {
            __log("skip id3Length error 2.");
            fclose(file);
            return nullptr;
        }
    }
    if (isAIDHeader(buf)) {
        //aid header
        if (fread(buf, 1, 2, file) != 2) {
            __log("read aid header error.");
            fclose(file);
            return nullptr;
        }
        aidLength = buf[0] + 256 * buf[1];
        //skip adiLength
        if (fread(buf, 1, aidLength, file) != aidLength) {
            __log("skip adiLength error 1.");
            fclose(file);
            return nullptr;
        }
        if (fread(buf, 1, 4, file) != 4) {
            __log("skip adiLength error 2.");
            fclose(file);
            return nullptr;
        }
    }
    //----------------
    char tmpBuf[1];
    while (!isMp123SyncWord(buf)) {
        // search for MP3 syncword one byte at a time
        for (int i = 0; i < 3; i++) {
            buf[i] = buf[i + 1];
        }
        int val = fread(tmpBuf, 1, 1, file);
        if (val == -1) {
            fclose(file);
            return nullptr;
        }
        buf[3] = tmpBuf[0];
    }

    int size;
    do {
        size = fread(buf, 1, count, file);
        if (lame_config(buf, size) == 0) {
            break;
        }
    } while (size > 0);

    out->sampleRate = mp3data->samplerate;
    out->channelCount = mp3data->stereo;
    return file;
}

//nativeConfigureDecoder
int lame_config(char *mp3_buf, int size) {
    int ret = -1;
    short left_buf[1152], right_buf[1152];

    ret = hip_decode1_headersB(hip_context, reinterpret_cast<unsigned char *>(mp3_buf), size,
                               left_buf, right_buf, mp3data, &enc_delay, &enc_padding);
    if (mp3data->header_parsed) {
        mp3data->totalframes = mp3data->nsamp / mp3data->framesize;
    }
    return ret;
}

void lame_LogError(const char *file, void *openResult) {
    Log log = getLog();
    if (log != nullptr) {
        log("can't open file = %s", file);
    }
}

/**
 * read the media data for target count. every is a float
 * @param openResult
 * @param filebuf
 * @param count
 * @return the count of read
 */
int lame_ReadMediaData(void *openResult, float *filebuf, int blockSize) {
    Log log = getLog();

    if (!readed) {
        startPreProcessAudio(blockSize);
        int bufferSize = 1024;

        FILE *file = static_cast<FILE *>(openResult);
        char buf[bufferSize];
        float pcms[mp3data->framesize * mp3data->stereo];
        size_t readSize;

        //read all data to queue.
        while ((readSize = fread(buf, 1, bufferSize, file)) > 0) {
            // check for buffered data
            int samples_read = hip_decode1_unclipped3(hip_context, (unsigned char *) buf, readSize,
                                                      pcms, mp3data);
            if (log != nullptr) {
                log("hip_decode1_unclipped3 >>> read sample data. samples_read = %d, bufSize = %d.",
                    samples_read, bufferSize);
            }
            if (samples_read > 0) {
                addAudioData(pcms, 0, samples_read);
            } else if (samples_read < 0) {
                break;
            }
            /*else{
                do{
                    samples_read = hip_decode1_unclipped3(hip_context, (unsigned char *)buf, readSize, pcms, mp3data);
                } while (samples_read == 0);
            }*/
        }
        //close
        fclose(file);
        readed = true;
        endPreProcessAudio();
    }
    return nextBlockedAudioData(filebuf);
}

void lame_ReleaseMedia(void *openResult) {
    releaseAudioData();
    readed = false;
    if (hip_context) {
        int ret = hip_decode_exit(hip_context);
        hip_context = NULL;
        free(mp3data);
        mp3data = NULL;
        enc_delay = -1;
        enc_padding = -1;
    }
}