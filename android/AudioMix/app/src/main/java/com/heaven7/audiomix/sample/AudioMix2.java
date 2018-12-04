package com.heaven7.audiomix.sample;

import android.util.Log;

import com.heaven7.core.util.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import static com.heaven7.audiomix.sample.AudioMixer.normalizationMix;

/**
 * Created by heaven7 on 2018/3/29 0029.
 */

public class AudioMix2 {
    public void mixAudios(File[] rawAudioFiles) throws Exception{

        final int fileSize = rawAudioFiles.length;

        FileInputStream[] audioFileStreams = new FileInputStream[fileSize];
        File audioFile = null;

        FileInputStream inputStream;
        byte[][] allAudioBytes = new byte[fileSize][];
        boolean[] streamDoneArray = new boolean[fileSize];
        byte[] buffer = new byte[512];
        int offset = -1;

        try {

            for (int fileIndex = 0; fileIndex < fileSize; ++fileIndex) {
                audioFile = rawAudioFiles[fileIndex];
                audioFileStreams[fileIndex] = new FileInputStream(audioFile);
            }

            while(true){
                for(int streamIndex = 0 ; streamIndex < fileSize ; ++streamIndex){
                    inputStream = audioFileStreams[streamIndex];
                    if(!streamDoneArray[streamIndex] && (offset = inputStream.read(buffer)) != -1){
                        allAudioBytes[streamIndex] = Arrays.copyOf(buffer, buffer.length);
                    }else{
                        streamDoneArray[streamIndex] = true;
                        allAudioBytes[streamIndex] = new byte[512];
                    }
                }

                byte[] mixBytes = normalizationMix(allAudioBytes);

                //mixBytes 就是混合后的数据
                boolean done = true;
                for(boolean streamEnd : streamDoneArray){
                    if(!streamEnd){
                        done = false;
                    }
                }
                if(done){
                    Logger.i("AudioMix2", "mixAudios", "mix ok");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                for(FileInputStream in : audioFileStreams){
                    if(in != null)
                        in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 每一行是一个音频的数据
     */
   public static byte[] averageMix(byte[][] bMulRoadAudioes) {

        if (bMulRoadAudioes == null || bMulRoadAudioes.length == 0)
            return null;
        byte[] realMixAudio = bMulRoadAudioes[0];

        if(bMulRoadAudioes.length == 1)
            return realMixAudio;

        for(int rw = 0 ; rw < bMulRoadAudioes.length ; ++rw){
            if(bMulRoadAudioes[rw].length != realMixAudio.length){
                Log.e("app", "column of the road of audio + " + rw +" is diffrent.");
                return null;
            }
        }

        int row = bMulRoadAudioes.length;
        int coloum = realMixAudio.length / 2;
        short[][] sMulRoadAudioes = new short[row][coloum];

        for (int r = 0; r < row; ++r) {
            for (int c = 0; c < coloum; ++c) {
                sMulRoadAudioes[r][c] = (short) ((bMulRoadAudioes[r][c * 2] & 0xff) | (bMulRoadAudioes[r][c * 2 + 1] & 0xff) << 8);
            }
        }

        short[] sMixAudio = new short[coloum];
        int mixVal;
        int sr = 0;
        for (int sc = 0; sc < coloum; ++sc) {
            mixVal = 0;
            sr = 0;
            for (; sr < row; ++sr) {
                mixVal += sMulRoadAudioes[sr][sc];
            }
            sMixAudio[sc] = (short) (mixVal / row);
        }

        for (sr = 0; sr < coloum; ++sr) {
            realMixAudio[sr * 2] = (byte) (sMixAudio[sr] & 0x00FF);
            realMixAudio[sr * 2 + 1] = (byte) ((sMixAudio[sr] & 0xFF00) >> 8);
        }

        return realMixAudio;
    }
}
