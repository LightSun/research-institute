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

#include "sndfile.h"
#include "depend/PluginInputDomainAdapter.h"
#include "depend/PluginBufferingAdapter.h"

#include "chordextract.h"

#define TAG "ChordinoExtract"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__)


using namespace std;
using namespace Vamp;
using namespace HostExt;

//俩个参数： 0是简单的名称，1 是完整的音乐文件名
int main(int argc, char **argv)
{
    const char *myname = argv[0];

    if (argc != 2) {
        //cerr << "usage: " << myname << " file.wav" << endl;
        LOGD("usage: %s file.wav", myname);
        return 2;
    }

    const char *infile = argv[1];

    SF_INFO sfinfo;
    SNDFILE *sndfile = sf_open(infile, SFM_READ, &sfinfo);

    if (!sndfile) { //some wav is wrong which is from ffmpeg convert,"Error in WAV/W64/RF64 file. Malformed 'fmt ' chunk."
        const char * error = sf_strerror(sndfile);
        //cerr << myname << ": Failed to open input file " << infile<< ": " << sf_strerror(sndfile) << endl;
        LOGD("usage: %s : Failed to open input file. file = %s. error is %s", myname, infile, error);
        return 1;
    }

    /**
     * 需要解码后的数据： 采样率. 通道数, 总的帧数
     * sf_readf_float(sndfile, filebuf, blocksize)读取多少帧的数据到缓冲区 float* 类型
     */
    Chordino *chordino = new Chordino(sfinfo.samplerate);
    PluginInputDomainAdapter *ia = new PluginInputDomainAdapter(chordino);
    ia->setProcessTimestampMethod(PluginInputDomainAdapter::ShiftData);
    PluginBufferingAdapter *adapter = new PluginBufferingAdapter(ia);

    int blocksize = adapter->getPreferredBlockSize();
    LOGD("frames = %d, blocksize = %d", sfinfo.frames, blocksize);

    // Plugin requires 1 channel (we will mix down)
    if (!adapter->initialise(1, blocksize, blocksize)) {
        //cerr << myname << ": Failed to initialise Chordino adapter!" << endl;
        LOGD("%s : Failed to initialise Chordino adapter!", myname);
        return 1;
    }

    float *filebuf = new float[sfinfo.channels * blocksize];
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
        LOGD("%s : Failed to identify chords output!", myname);
        return 1;
    }
    
    int frame = 0;
    while (frame < sfinfo.frames) {
        int count = -1;
        if ((count = sf_readf_float(sndfile, filebuf, blocksize)) <= 0)
            break;
        LOGD("sf_readf_float >> count = %d", count);

        // mix down 取中值
        for (int i = 0; i < blocksize; ++i) {
            mixbuf[i] = 0.f;
            if (i < count) {
                for (int c = 0; c < sfinfo.channels; ++c) {
                    mixbuf[i] += filebuf[i * sfinfo.channels + c] / sfinfo.channels;
                }
            }
        }

        RealTime timestamp = RealTime::frame2RealTime(frame, sfinfo.samplerate);

        // feed to plugin: can just take address of buffer, as only one channel
        fs = adapter->process(&mixbuf, timestamp);

        chordFeatures.insert(chordFeatures.end(),
                     fs[chordFeatureNo].begin(),
                     fs[chordFeatureNo].end());

        frame += count;
    }

    sf_close(sndfile);

    // features at end of processing (actually Chordino does all its work here)
    fs = adapter->getRemainingFeatures();

    // chord output is output index 0
    chordFeatures.insert(chordFeatures.end(),
			 fs[chordFeatureNo].begin(),
			 fs[chordFeatureNo].end());

    for (int i = 0; i < (int)chordFeatures.size(); ++i) {
        string timestamp = chordFeatures[i].timestamp.toString();
        string label = chordFeatures[i].label;
        LOGD("chords is %s, label is %s", timestamp.c_str(), label.c_str());
        //cout << chordFeatures[i].timestamp.toString() << ": " << chordFeatures[i].label << endl;
    }

    delete[] filebuf;
    delete[] mixbuf;
    
    delete adapter;
}

