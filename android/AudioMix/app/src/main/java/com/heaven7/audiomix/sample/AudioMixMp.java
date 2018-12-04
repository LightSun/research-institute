package com.heaven7.audiomix.sample;

import android.media.MediaPlayer;

import com.heaven7.core.util.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioMixMp{

    private static final String TAG = "AudioMix";
    private final ExecutorService mService;
    private final ArrayList<MediaTask> mTasks = new ArrayList<>();

    public AudioMixMp(MediaEntity[] entities) {
        convertTasks(entities);
        this.mService = Executors.newFixedThreadPool(entities.length);
    }

    public void play(){
        for(MediaTask task : mTasks){
            mService.submit(task);
        }
    }
    public boolean setVolume(MediaEntity entity, float volume){
        MediaTask[] array = (MediaTask[]) mTasks.toArray();
        for(MediaTask task : array) {
            if(task.entity.equals(entity)) {
                if (task.isPlaying()) {
                    task.setVolume(volume);
                } else {
                    entity.volume = volume;
                }
                return true;
            }
        }
        Logger.w(TAG, "setVolume", "set volume failed. path = " + entity.path);
        return false;
    }
    public void cancel(){
        for(MediaTask task : mTasks){
            task.cancel();
        }
        //mTasks.clear();
    }

    private  void convertTasks(MediaEntity[] files) {
        for (MediaEntity file : files) {
            mTasks.add(new MediaTask(file));
        }
    }

    private static class MediaTask implements Runnable{

        final MediaEntity entity;
        MediaPlayer player;
        volatile boolean playing;

        public MediaTask(MediaEntity entity) {
            this.entity = entity;
        }
        @Override
        public void run() {
            player = new MediaPlayer();
            try {
                player.setDataSource(entity.path);
                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setVolume(entity.volume, entity.volume);
                        mp.start();
                        playing = true;
                    }
                });
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playing = false;
                    }
                });
                player.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel(){
            if(player != null){
                playing = false;
                player.reset();
                player.release();
                player = null;
            }
        }
        public void setVolume(float volume){
            if(player != null){
                player.setVolume(volume, volume);
            }
        }
        boolean isPlaying(){
            return player != null && playing;
        }
    }

    public static class MediaEntity{
        final String path;
        float volume = 1f;
        long delay;  //start delay in mill seconds
        long offset; //start offset in mill seconds

        public MediaEntity(String path, float volume) {
            this.path = path;
            this.volume = volume;
        }
        public MediaEntity(String path) {
            this(path, 1f);
        }

        public long getDelay() {
            return delay;
        }
        public void setDelay(long delay) {
            this.delay = delay;
        }

        public long getOffset() {
            return offset;
        }
        public void setOffset(long offset) {
            this.offset = offset;
        }
    }
}
