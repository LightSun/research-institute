/* -*- c-basic-offset: 4 indent-tabs-mode: nil -*-  vi:set ts=8 sts=4 sw=4: */

/*
  NNLS-Chroma / Chordino

  Audio feature extraction plugins for chromagram and chord
  estimation.

  Centre for Digital Music, Queen Mary University of London.
  This file copyright 2014 QMUL.
    
  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License as
  published by the Free Software Foundation; either version 2 of the
  License, or (at your option) any later version.  See the file
  COPYING included with this distribution for more information.
*/

/*
  Extract chords from an audio file, read using libsndfile.  Works by
  constructing the plugin as a C++ class directly, and using plugin
  adapters from the Vamp Host SDK to provide input.

  You can compile this with e.g. the following (Linux example):

  $ g++ -D_VAMP_PLUGIN_IN_HOST_NAMESPACE -O2 -ffast-math chordextract.cpp Chordino.cpp NNLSBase.cpp chromamethods.cpp viterbi.cpp nnls.c -o chordextract -lsndfile -lvamp-hostsdk -ldl

  But the same idea should work on any platform, so long as the Boost
  Tokenizer headers and the Vamp Host SDK library are available and
  the _VAMP_PLUGIN_IN_HOST_NAMESPACE preprocessor symbol is defined
  throughout.
*/

#define _VAMP_PLUGIN_IN_HOST_NAMESPACE 1

#include <android/log.h>
#include <iostream>
#include <string>

#include "depend/RealTime.h"
#include "Chordino.h"
#include "NNLSChroma.h"
#include "Tuning.h"

#include "depend/PluginInputDomainAdapter.h"
#include "depend/PluginBufferingAdapter.h"

#include "chordextract.h"

#include "chordinomedia/chordinomedia.h"
#include "mediamanager.h"
#include "formats.h"

#include "cut_generator.h"
#include "number.h"

using namespace std;
using namespace Vamp;
using namespace HostExt;
using namespace CUT_GEN;

int extractChords(int argc, void **argv)
{
    const char *myname = static_cast<const char *>(argv[0]);
    Log const log = getLog();
    if(log == nullptr){
        return STATE_ERROR;
    }
    if (argc < 2) {
        //cerr << "usage: " << myname << " file.wav" << endl;
        log("usage: %s file.wav", myname);
        return PARAM_ERROR;
    }
    const char *infile = static_cast<const char *>(argv[1]);

    CutContext* context = nullptr;
    List<ChordInfo*>* list = nullptr;
    if(argc >= 4){
        context = static_cast<CutContext *>(argv[2]);
        list = static_cast<List<ChordInfo *> *>(argv[3]);
    }

    //================================
    
    int count = 2;
    MediaFormat* *formats = new MediaFormat*[count];
    formats[0] = createSndMediaFormat();
    formats[1] = createMp3MediaFormat();
    registerMediaFormats(formats, count);

    MediaFormat *mediaFormat = getMediaFormat(infile);
    if(mediaFormat == nullptr){
        releaseMediaFormats();
        log("can't find media format for target file = %s", infile);
        return NO_MEDIA_FORMAT;
    }
    //open
    MediaData mediaData;
    void * openResult = mediaFormat->openMedia(infile, &mediaData);
    if(openResult == nullptr){
        mediaFormat->logError(infile, openResult);
        return OPEN_FAILED;
    }

    /**
     * 需要解码后的数据： 采样率. 通道数, 总的帧数
     * sf_readf_float(sndfile, filebuf, blocksize)读取多少帧的数据到缓冲区 float* 类型
     */
    Chordino *chordino = new Chordino(mediaData.sampleRate);
    PluginInputDomainAdapter *ia = new PluginInputDomainAdapter(chordino);
    ia->setProcessTimestampMethod(PluginInputDomainAdapter::ShiftData);
    PluginBufferingAdapter *adapter = new PluginBufferingAdapter(ia);

    int blocksize = adapter->getPreferredBlockSize();
    //log("frames = %d, blocksize = %d", sfinfo.frames, blocksize);

    // Plugin requires 1 channel (we will mix down)
    if (!adapter->initialise(1, blocksize, blocksize)) {
        //cerr << myname << ": Failed to initialise Chordino adapter!" << endl;
        log("%s : Failed to initialise Chordino adapter!", myname);
        return READ_ERROR;
    }

    float *filebuf = new float[mediaData.channelCount * blocksize];
    float *mixbuf = new float[blocksize];

    Plugin::FeatureList chordFeatures;
    Plugin::FeatureSet fs;

    int chordFeatureNo = -1;
    Plugin::OutputList* outputs = adapter->getOutputDescriptors();
    for (int i = 0; i < int(outputs->size()); ++i) {
        if (outputs->at(i).identifier == "simplechord") {
            chordFeatureNo = i;
        }
    }
    if (chordFeatureNo < 0) {
        //cerr << myname << ": Failed to identify chords output!" << endl;
        log("%s : Failed to identify chords output!", myname);
        return READ_ERROR;
    }
    
    int frame = 0;
  //  while (frame < sfinfo.frames) {
    while (true) {
        int count = -1;
        if ((count = mediaFormat->readMediaData(openResult, filebuf, blocksize)) <= 0)
            break;
        log("readMediaData >> count = %d", count);

        // mix down 取中值
        for (int i = 0; i < blocksize; ++i) {
            mixbuf[i] = 0.f;
            if (i < count) {
                for (int c = 0; c < mediaData.channelCount; ++c) {
                    mixbuf[i] += filebuf[i * mediaData.channelCount + c] / mediaData.channelCount;
                }
            }
        }

        RealTime timestamp = RealTime::frame2RealTime(frame, mediaData.sampleRate);
        //log("timestamp: %d, %d", timestamp.msec(), timestamp.nsec);

        // feed to plugin: can just take address of buffer, as only one channel
        fs = adapter->process(&mixbuf, timestamp);

        chordFeatures.insert(chordFeatures.end(),
                     fs[chordFeatureNo].begin(),
                     fs[chordFeatureNo].end());

        frame += count;
    }
    RealTime timestamp = RealTime::frame2RealTime(frame, mediaData.sampleRate);
    int maxTimeMsec = (int)(strToFloat(timestamp.toString()) * 1000);
    log("total samples = %d, maxTime(msec) = %d", frame, maxTimeMsec);

    mediaFormat->releaseMedia(openResult);
    //releaseMediaFormats();
    delete[](formats);

    // features at end of processing (actually Chordino does all its work here)
    fs = adapter->getRemainingFeatures();

    // chord output is output index 0
    chordFeatures.insert(chordFeatures.end(),
			 fs[chordFeatureNo].begin(),
			 fs[chordFeatureNo].end());

    for (int i = 0; i < (int)chordFeatures.size(); ++i) {
        string timestamp = chordFeatures[i].timestamp.toString();
        string label = chordFeatures[i].label;
        log("chords is %s, label is %s. time = %d.%d", timestamp.c_str(), label.c_str(),
             chordFeatures[i].timestamp.sec, chordFeatures[i].timestamp.msec());
        //cout << chordFeatures[i].timestamp.toString() << ": " << chordFeatures[i].label << endl;
        if(argc >= 4){
            ChordInfo* pInfo = context->pool.create();
            pInfo->timemsec = (int)(strToFloat(timestamp) * 1000);
            pInfo->label = chordFeatures[i].label;
            list->add(pInfo);
        }
    }
    if(argc >= 4){
        if(list->size() < 2){
            log("chord info is not enough.");
            return READ_ERROR;
        }
        log("chord head and tail time: (%d, %d)", list->getStart()->timemsec, list->getEnd()->timemsec);
        //add start time
        if(list->getStart()->timemsec > 0){
            ChordInfo *const pInfo = context->pool.create();
            pInfo->timemsec = 0;
            list->add(0, pInfo);
        }
        //add end time
        if(list->getEnd()->timemsec < maxTimeMsec){
            ChordInfo *const pInfo = context->pool.create();
            pInfo->timemsec = maxTimeMsec;
            list->add(pInfo);
        }
    }
    delete[] filebuf;
    delete[] mixbuf;
    delete adapter;
    return 0;
}

