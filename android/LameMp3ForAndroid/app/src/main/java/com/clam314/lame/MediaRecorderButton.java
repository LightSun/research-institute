package com.clam314.lame;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import java.io.File;

/**
 * Created by clam314 on 2016/7/21
 */
public class MediaRecorderButton extends Button implements MediaRecorderListener{

    //用于标识按钮不同状态，显示不同的样式
    private static final int NORMAL_STATUS = 109;
    private static final int RECORDING_STATUS = 110;
    private static final int CANCEL_STATUS = 111;
    private static final int DISTANCE_Y_CANCEL = 50;

    //设置录音的最短时长,过短的录音当做取消
    private static final long timeDistance = 1000;

    //延迟开始录音的时间，防止连按，多次触发录音功能导致阻塞主线程
    private static final long DELAYED_TIME = 15*100;

    //用于标识录音的不同状态
    public static final int RECORDER_SUCCESS = 209;
    public static final int START_RECORDER = 200;
    public static final int END_RECORDER_60S = 260;
    public static final int END_RECORDER_TOO_SHORT = 270;
    public static final int END_RECORDER_CANCEL = 280;
    private static final int COUNT_STATUS = 180;


    private long startTime = 0;
    private long downTime = 0;
    private boolean cancelCount = false;
    private boolean wantCancel = false;
    private boolean recording = false;
    private boolean overMaxRecodeTime = false;
    private String bastPath;
    private RecorderStatusListener statusListener;
    private RecorderFinishListener finishListener;
    private int mSecond = 0;

    private Handler handler = new Handler(Looper.getMainLooper()){
        static final int COUNT_TIME_END = 0;
        int count = 60;//限制录音的最大时长，单位秒
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case COUNT_STATUS:
                    if(cancelCount){
                        removeMessages(COUNT_STATUS);
                        count = 60;
                        if(statusListener != null) statusListener.onEnd(END_RECORDER_CANCEL);
                        break;
                    }
                    count--;
                    mSecond = 60 - count;
                    if(statusListener != null) statusListener.onEnd(count);
                    if (count == COUNT_TIME_END) {
                        removeMessages(COUNT_STATUS);
                        changeBackground(NORMAL_STATUS);
                        overMaxRecodeTime = true;
                        endRecorder(false);
                        if(statusListener != null) statusListener.onEnd(END_RECORDER_60S);
                        count = 60;
                        return;
                    }
                    sendEmptyMessageDelayed(COUNT_STATUS, 1000);
                    break;
                case START_RECORDER:
                    startRecorder(bastPath);
                    break;
            }
        }
    };

    public MediaRecorderButton(Context context) {
        super(context);
        init();
    }

    public MediaRecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MediaRecorderButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        bastPath = getBasePath();
        changeBackground(NORMAL_STATUS);
    }

    public  String getBasePath() {
        String strPath = null;
        if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            strPath = getContext().getFilesDir() + "/" + "lameMp3";
        } else{
            strPath = Environment.getExternalStorageDirectory() + "/" + "lameMp3";
        }
        File dir = new File(strPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        return strPath;
    }

    public void setBastPath(String bastPath){
        this.bastPath = bastPath;
    }

    public void setFinishListener(RecorderFinishListener listener){
        finishListener = listener;
        LameMp3Manager.instance.setMediaRecorderListener(this);
    }

    public void setRecorderStatusListener(RecorderStatusListener listener){
        if(listener != null){
            this.statusListener = listener;
        }
    }

    private void startRecorder(String basePath){
        overMaxRecodeTime = false;
        mSecond = 0;
        LameMp3Manager.instance.startRecorder(basePath+File.separator+"lame.mp3");
        startTime = System.currentTimeMillis();
        recording = true;
        cancelCount = false;
        handler.sendEmptyMessage(COUNT_STATUS);
        if(statusListener != null) statusListener.onStart(START_RECORDER);
    }

    private void endRecorder(boolean wantCancel){
        if(recording){
            boolean tooShort = System.currentTimeMillis() - startTime < timeDistance;
            if(tooShort){
                LameMp3Manager.instance.cancelRecorder();
                if(statusListener != null) statusListener.onEnd(END_RECORDER_TOO_SHORT);
            }else if(wantCancel){
                LameMp3Manager.instance.cancelRecorder();
                this.cancelCount = true;
                this.wantCancel = false;
            }else {
                LameMp3Manager.instance.stopRecorder();
            }
            recording = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                changeBackground(RECORDING_STATUS);
                handler.removeMessages(START_RECORDER);
                handler.sendEmptyMessageDelayed(START_RECORDER, DELAYED_TIME);
                break;
            case MotionEvent.ACTION_MOVE:
                if(wantToCancel(x, y)){
                    changeBackground(CANCEL_STATUS);
                    wantCancel = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                changeBackground(NORMAL_STATUS);
                if(System.currentTimeMillis() - downTime < DELAYED_TIME){
                    handler.removeMessages(START_RECORDER);
                }
                //录音时间没有超时才停止录音
                if(!overMaxRecodeTime){
                    endRecorder(wantCancel);
                    cancelCount = true;
                }
        }
        return super.onTouchEvent(event);
    }

    private boolean wantToCancel(int x, int y) {
        return x < 0 || x > getWidth() || y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL;
    }

    private void changeBackground(int status){
        switch (status){
            case NORMAL_STATUS:
                setBackgroundResource(R.drawable.button_recordnormal);
                setText("按下开始录音");
                break;
            case RECORDING_STATUS:
                setBackgroundResource(R.drawable.button_recording);
                setText("松开 结束");
                break;
            case CANCEL_STATUS:
                setBackgroundResource(R.drawable.button_recording);
                setText("取消");
                break;
        }
    }

    @Override
    public void onRecorderFinish(int status, String path) {
        if(finishListener != null){
            finishListener.onRecorderFinish(status,path, String.valueOf(mSecond));
        }
    }

    public interface RecorderStatusListener{
        public void onStart(int status);
        public void onEnd(int status);
    }

    public interface RecorderFinishListener {
        public void onRecorderFinish(int status, String filePath, String second);
    }
}
