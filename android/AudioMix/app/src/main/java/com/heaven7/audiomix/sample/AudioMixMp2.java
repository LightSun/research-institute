package com.heaven7.audiomix.sample;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.heaven7.core.util.MainWorker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by heaven7 on 2018/8/4 0004.
 */
public class AudioMixMp2 {

    final ExecutorService mService = Executors.newFixedThreadPool(2);
    private final String media1;
    private final String media2;
    private List<MediaTask> mCache = new ArrayList<>();

    public AudioMixMp2(String media1, String media2) {
        this.media1 = media1;
        this.media2 = media2;
    }

    public void start(){
        AudioMixMp.MediaEntity md1 = new AudioMixMp.MediaEntity(media1, 1f);
        AudioMixMp.MediaEntity md2 = new AudioMixMp.MediaEntity(media2, 0.5f);
        md2.setDelay(5000);
        md2.setOffset(3000);
        MediaTask mt1 = new MediaTask(md1);
        MediaTask mt2 = new MediaTask(md2);
        mCache.add(mt1);
        mCache.add(mt2);
        mService.submit(mt1);
        mService.submit(mt2);
    }

    public void cancel(){
        for (MediaTask task : mCache){
            task.cancel();
        }
        mCache.clear();
    }

    private static class MediaTask implements Runnable{

        final AudioMixMp.MediaEntity entity;
        MediaPlayer player;
        volatile boolean playing;
        volatile boolean cancelled;

        public MediaTask(AudioMixMp.MediaEntity entity) {
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
                        if(cancelled){
                            return;
                        }
                        mp.setVolume(entity.volume, entity.volume);
                        if(entity.delay > 0) {
                            Looper looper = Looper.myLooper();
                            if(looper == null){
                                Looper.prepare();
                                looper = Looper.myLooper();
                            }
                            new Handler(looper).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(cancelled){
                                        return;
                                    }
                                   // int duration = mp.getDuration();
                                    if(entity.offset > 0) {
                                        mp.seekTo((int) entity.offset);
                                    }
                                    mp.start();
                                    playing = true;
                                }
                            }, entity.delay);
                        }else {
                            mp.start();
                            playing = true;
                        }
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
                cancelled = true;
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
}
