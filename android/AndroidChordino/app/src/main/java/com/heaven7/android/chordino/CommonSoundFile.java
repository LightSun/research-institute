/*
 * Copyright (C) 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.heaven7.android.chordino;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * CheapSoundFile is the parent class of several subclasses that each
 * do a "cheap" scan of various sound file formats, parsing as little
 * as possible in order to understand the high-level frame structure
 * and get a rough estimate of the volume level of each frame.  Each
 * subclass is able to:
 *  - open a sound file
 *  - return the sample rate and number of frames
 *  - return an approximation of the volume level of each frame
 *
 * A frame should represent no less than 1 ms and no more than 100 ms of
 * audio.  This is compatible with the native frame sizes of most audio
 * file formats already, but if not, this class should expose virtual
 * frames in that size range.
 *
 * Modified by Anna Stępień <anna.stepien@semantive.com>
 *             heaven7
 */
public class CommonSoundFile implements ISoundFile{
    private ProgressListener mProgressListener = null;
    private File mInputFile = null;

    // Member variables representing frame data
    private String mFileType;
    private int mFileSize;
    private int mAvgBitRate;  // Average bit rate in kbps.
    private int mSampleRate;
    private int mChannels;
    private int mNumSamples;  // total number of samples per channel in audio file
    private ByteBuffer mDecodedBytes;  // Raw audio data
    private ShortBuffer mDecodedSamples;  // shared buffer with mDecodedBytes.
    // mDecodedSamples has the following format:
    // {s1c1, s1c2, ..., s1cM, s2c1, ..., s2cM, ..., sNc1, ..., sNcM}
    // where sicj is the ith sample of the jth channel (a sample is a signed short)
    // M is the number of channels (e.g. 2 for stereo) and N is the number of samples per channel.

    // Member variables for hack (making it work with old version, until app just uses the samples).
    private int mNumFrames;
    private float[] mFrameGains;
    private int[] mFrameLens;
    private int[] mFrameOffsets;

    public String getFiletype() {
        return mFileType;
    }

    public int getFileSizeBytes() {
        return mFileSize;
    }

    public int getAvgBitrateKbps() {
        return mAvgBitRate;
    }

    public int getSampleRate() {
        return mSampleRate;
    }

    public int getChannels() {
        return mChannels;
    }

    public int getNumSamples() {
        return mNumSamples;  // Number of samples per channel.
    }

    // Should be removed when the app will use directly the samples instead of the frames.
    public int getNumFrames() {
        return mNumFrames;
    }

    // Should be removed when the app will use directly the samples instead of the frames.
    public int getSamplesPerFrame() {
        return 1024;  // just a fixed value here...
    }

    // Should be removed when the app will use directly the samples instead of the frames.
    public float[] getFrameGains() {
        return mFrameGains;
    }

    public ShortBuffer getSamples() {
        if (mDecodedSamples != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
                Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
                // Hack for Nougat where asReadOnlyBuffer fails to respect byte ordering.
                // See https://code.google.com/p/android/issues/detail?id=223824
                return mDecodedSamples;
            } else {
                return mDecodedSamples.asReadOnlyBuffer();
            }
        } else {
            return null;
        }
    }

    // A SoundFile object should only be created using the static methods create() and record().
    /*private*/ CommonSoundFile() {
    }

    public void setProgressListener(ProgressListener progressListener) {
        mProgressListener = progressListener;
    }

    public void ReadFile(File inputFile)
        throws java.io.FileNotFoundException,
               IOException {
        MediaExtractor extractor = new MediaExtractor();
        MediaFormat format = null;
        int i;

        mInputFile = inputFile;
        String[] components = mInputFile.getPath().split("\\.");
        mFileType = components[components.length - 1];
        mFileSize = (int)mInputFile.length();
        extractor.setDataSource(mInputFile.getPath());
        int numTracks = extractor.getTrackCount();
        // find and select the first audio track present in the file.
        for (i=0; i<numTracks; i++) {
            format = extractor.getTrackFormat(i);
            if (format.getString(MediaFormat.KEY_MIME).startsWith("audio/")) {
                extractor.selectTrack(i);
                break;
            }
        }
        if (i == numTracks) {
            throw new IOException("No audio track found in " + mInputFile);
        }
        mChannels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
        mSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        // Expected total number of samples per channel.
        int expectedNumSamples =
            (int)((format.getLong(MediaFormat.KEY_DURATION) / 1000000.f) * mSampleRate + 0.5f);

        MediaCodec codec = MediaCodec.createDecoderByType(format.getString(MediaFormat.KEY_MIME));
        codec.configure(format, null, null, 0);
        codec.start();

        int decodedSamplesSize = 0;  // size of the output buffer containing decoded samples.
        byte[] decodedSamples = null;
        ByteBuffer[] inputBuffers = codec.getInputBuffers();
        ByteBuffer[] outputBuffers = codec.getOutputBuffers();
        int sample_size;
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        long presentation_time;
        int tot_size_read = 0;
        boolean done_reading = false;

        // Set the size of the decoded samples buffer to 1MB (~6sec of a stereo stream at 44.1kHz).
        // For longer streams, the buffer size will be increased later on, calculating a rough
        // estimate of the total size needed to store all the samples in order to resize the buffer
        // only once.
        mDecodedBytes = ByteBuffer.allocate(1<<20);
        Boolean firstSampleData = true;
        while (true) {
            // read data from file and feed it to the decoder input buffers.
            int inputBufferIndex = codec.dequeueInputBuffer(100);
            if (!done_reading && inputBufferIndex >= 0) {
                sample_size = extractor.readSampleData(inputBuffers[inputBufferIndex], 0);
                if (firstSampleData
                        && format.getString(MediaFormat.KEY_MIME).equals("audio/mp4a-latm")
                        && sample_size == 2) {
                    // For some reasons on some devices (e.g. the Samsung S3) you should not
                    // provide the first two bytes of an AAC stream, otherwise the MediaCodec will
                    // crash. These two bytes do not contain music data but basic info on the
                    // stream (e.g. channel configuration and sampling frequency), and skipping them
                    // seems OK with other devices (MediaCodec has already been configured and
                    // already knows these parameters).
                    extractor.advance();
                    tot_size_read += sample_size;
                } else if (sample_size < 0) {
                    // All samples have been read.
                    codec.queueInputBuffer(
                            inputBufferIndex, 0, 0, -1, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                    done_reading = true;
                } else {
                    presentation_time = extractor.getSampleTime();
                    codec.queueInputBuffer(inputBufferIndex, 0, sample_size, presentation_time, 0);
                    extractor.advance();
                    tot_size_read += sample_size;
                    if (mProgressListener != null) {
                        if (!mProgressListener.reportProgress((float)(tot_size_read) / mFileSize)) {
                            // We are asked to stop reading the file. Returning immediately. The
                            // SoundFile object is invalid and should NOT be used afterward!
                            extractor.release();
                            extractor = null;
                            codec.stop();
                            codec.release();
                            codec = null;
                            return;
                        }
                    }
                }
                firstSampleData = false;
            }

            // Get decoded stream from the decoder output buffers.
            int outputBufferIndex = codec.dequeueOutputBuffer(info, 100);
            if (outputBufferIndex >= 0 && info.size > 0) {
                if (decodedSamplesSize < info.size) {
                    decodedSamplesSize = info.size;
                    decodedSamples = new byte[decodedSamplesSize];
                }
                outputBuffers[outputBufferIndex].get(decodedSamples, 0, info.size);
                outputBuffers[outputBufferIndex].clear();
                // Check if buffer is big enough. Resize it if it's too small.
                if (mDecodedBytes.remaining() < info.size) {
                    // Getting a rough estimate of the total size, allocate 20% more, and
                    // make sure to allocate at least 5MB more than the initial size.
                    int position = mDecodedBytes.position();
                    int newSize = (int)((position * (1.0 * mFileSize / tot_size_read)) * 1.2);
                    if (newSize - position < info.size + 5 * (1<<20)) {
                        newSize = position + info.size + 5 * (1<<20);
                    }
                    ByteBuffer newDecodedBytes = null;
                    // Try to allocate memory. If we are OOM, try to run the garbage collector.
                    int retry = 10;
                    while(retry > 0) {
                        try {
                            newDecodedBytes = ByteBuffer.allocate(newSize);
                            break;
                        } catch (OutOfMemoryError oome) {
                            // setting android:largeHeap="true" in <application> seem to help not
                            // reaching this section.
                            retry--;
                        }
                    }
                    if (retry == 0) {
                        // Failed to allocate memory... Stop reading more data and finalize the
                        // instance with the data decoded so far.
                        break;
                    }
                    //ByteBuffer newDecodedBytes = ByteBuffer.allocate(newSize);
                    mDecodedBytes.rewind();
                    newDecodedBytes.put(mDecodedBytes);
                    mDecodedBytes = newDecodedBytes;
                    mDecodedBytes.position(position);
                }
                mDecodedBytes.put(decodedSamples, 0, info.size);
                codec.releaseOutputBuffer(outputBufferIndex, false);
            } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                outputBuffers = codec.getOutputBuffers();
            } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                // Subsequent data will conform to new format.
                // We could check that codec.getOutputFormat(), which is the new output format,
                // is what we expect.
            }
            if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0
                    || (mDecodedBytes.position() / (2 * mChannels)) >= expectedNumSamples) {
                // We got all the decoded data from the decoder. Stop here.
                // Theoretically dequeueOutputBuffer(info, ...) should have set info.flags to
                // MediaCodec.BUFFER_FLAG_END_OF_STREAM. However some phones (e.g. Samsung S3)
                // won't do that for some files (e.g. with mono AAC files), in which case subsequent
                // calls to dequeueOutputBuffer may result in the application crashing, without
                // even an exception being thrown... Hence the second check.
                // (for mono AAC files, the S3 will actually double each sample, as if the stream
                // was stereo. The resulting stream is half what it's supposed to be and with a much
                // lower pitch.)
                break;
            }
        }
        mNumSamples = mDecodedBytes.position() / (mChannels * 2);  // One sample = 2 bytes.
        mDecodedBytes.rewind();
        mDecodedBytes.order(ByteOrder.LITTLE_ENDIAN);
        mDecodedSamples = mDecodedBytes.asShortBuffer();
        mAvgBitRate = (int)((mFileSize * 8) * ((float)mSampleRate / mNumSamples) / 1000);

        extractor.release();
        extractor = null;
        codec.stop();
        codec.release();
        codec = null;

        // Temporary hack to make it work with the old version.
        mNumFrames = mNumSamples / getSamplesPerFrame();
        if (mNumSamples % getSamplesPerFrame() != 0){
            mNumFrames++;
        }
        mFrameGains = new float[mNumSamples * 2];
        /**
         * public float[] Convert16BitToFloat(byte[] input)
         * {
         *     int inputSamples = input.Length / 2; // 16 bit input, so 2 bytes per sample
         *     float[] output = new float[inputSamples];
         *     int outputIndex = 0;
         *     for(int n = 0; n < inputSamples; n++)
         *     {
         *         short sample = BitConverter.ToInt16(input,n*2);
         *         output[outputIndex++] = sample / 32768f;
         *     }
         *     return output;
         * }
         */
        for (i = 0 ; i < mNumSamples; i ++){
            mFrameGains[i * 2] = mDecodedSamples.get() / 32768f;
            mFrameGains[i * 2 + 1 ] = mDecodedSamples.get() / 32768f;
        }
        mDecodedSamples.rewind();
        // DumpSamples();  // Uncomment this line to dump the samples in a TSV file.
    }

    // Debugging method dumping all the samples in mDecodedSamples in a TSV file.
    // Each row describes one sample and has the following format:
    // "<presentation time in seconds>\t<channel 1>\t...\t<channel N>\n"
    // File will be written on the SDCard under media/audio/debug/
    // If fileName is null or empty, then the default file name (samples.tsv) is used.
    private void DumpSamples(String fileName) {
        String externalRootDir = Environment.getExternalStorageDirectory().getPath();
        if (!externalRootDir.endsWith("/")) {
            externalRootDir += "/";
        }
        String parentDir = externalRootDir + "media/audio/debug/";
        // Create the parent directory
        File parentDirFile = new File(parentDir);
        parentDirFile.mkdirs();
        // If we can't write to that special path, try just writing directly to the SDCard.
        if (!parentDirFile.isDirectory()) {
            parentDir = externalRootDir;
        }
        if (fileName == null || fileName.isEmpty()) {
            fileName = "samples.tsv";
        }
        File outFile = new File(parentDir + fileName);

        // Start dumping the samples.
        BufferedWriter writer = null;
        float presentationTime = 0;
        mDecodedSamples.rewind();
        String row;
        try {
            writer = new BufferedWriter(new FileWriter(outFile));
            for (int sampleIndex = 0; sampleIndex < mNumSamples; sampleIndex++) {
                presentationTime = (float)(sampleIndex) / mSampleRate;
                row = Float.toString(presentationTime);
                for (int channelIndex = 0; channelIndex < mChannels; channelIndex++) {
                    row += "\t" + mDecodedSamples.get();
                }
                row += "\n";
                writer.write(row);
            }
        } catch (IOException e) {
            Log.w("Ringdroid", "Failed to create the sample TSV file.");
            Log.w("Ringdroid", getStackTrace(e));
        }
        // We are done here. Close the file and rewind the buffer.
        try {
            writer.close();
        } catch (Exception e) {
            Log.w("Ringdroid", "Failed to close sample TSV file.");
            Log.w("Ringdroid", getStackTrace(e));
        }
        mDecodedSamples.rewind();
    }

    // Helper method (samples will be dumped in media/audio/debug/samples.tsv).
    private void DumpSamples() {
        DumpSamples(null);
    }

    // Return the stack trace of a given exception.
    private String getStackTrace(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
