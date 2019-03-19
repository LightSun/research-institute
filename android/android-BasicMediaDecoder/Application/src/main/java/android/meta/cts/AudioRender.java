package android.meta.cts;

/**
 * Created by heaven7 on 2018/10/16 0016.
 */
public interface AudioRender {

    void setAudioListener(AudioListener l);

    void prepare();

    void start();

    void pause();

    void cancel();

    void setVolume(float volume);

    boolean isPlaying();

    boolean isPlayDone();

    interface AudioListener{
        void onPlayEnd(AudioRender render, int overTime);
    }
}
