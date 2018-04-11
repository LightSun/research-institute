package com.heaven7.vida.research.sample;

import android.media.MediaPlayer;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by heaven7 on 2018/4/10 0010.
 */

public class AudioMix {

    private final ExecutorService mServise;
    private final String[] mMediaFiles;

    public AudioMix(String[] mMediaFiles) {
        this.mMediaFiles = mMediaFiles;
        this.mServise = Executors.newFixedThreadPool(mMediaFiles.length);
    }

    public void play(){
        Runnable[] tasks = convertTasks(mMediaFiles);
        for(Runnable r : tasks){
            mServise.submit(r);
        }
    }

    private static Runnable[] convertTasks(String[] files) {
        Runnable[] tasks = new Runnable[files.length];
        for (int i =0, size = files.length ; i < size ; i ++){
            tasks[i] = createTask(files[i]);
        }
        return tasks;
    }

    private static Runnable createTask(final String file) {
        return new Runnable() {
            @Override
            public void run() {
                MediaPlayer player = new MediaPlayer();
                try {
                    player.setDataSource(file);
                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    player.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
