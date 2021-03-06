package com.vida.ai.test;

import com.heaven7.java.base.util.Logger;
import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.colorgap.TransitionDelegate;
import com.heaven7.ve.colorgap.impl.SimpleColorGapContext;
import com.heaven7.ve.colorgap.impl.SimpleMusicPathProvider;
import com.heaven7.ve.configs.BootStrapData;
import com.heaven7.ve.starter.ImageDetectStarter;
import com.heaven7.ve.starter.KingdomStarter;
import com.heaven7.ve.starter.MusicCutStarter;
import com.heaven7.ve.utils.SharedThreadPool;
import com.vida.ai.third.baidu.BaiduImageDetector;

/**
 * @author heaven7
 */
public class Launcher {

    private static final String TAG = "Launcher";
    private static final SimpleColorGapContext sInitContext = new SimpleColorGapContext();

    private static void init(){
        BootStrapData data = BootStrapData.get(null);
        ColorGapContext.InitializeParam ip = new ColorGapContext.InitializeParam();
        ip.setTestType(data.getTestType());
        ip.setTemplateDir(data.getTemplateDir());
        ip.setEffectDir(data.getEffectDir());
        ip.setDebug(data.isDebug());
        ip.setEffectResourceDir(data.getEffectResourceDir());
        ip.setTransitionDelegate(new TransitionDelegate() {
            @Override
            public long getDuration(int transitionType) {
                return 30;
            }
        });
        sInitContext.setInitializeParam(ip);
        sInitContext.getSharedThreadPool().setTaskObserver(new SharedThreadPool.TaskObserver() {
            @Override
            public void onTaskStart(int module, String mark, Runnable r) {
                Logger.d(TAG, "onTaskStart", mark);
            }
            @Override
            public void onTaskEnd(int module, String mark, Runnable r) {
                Logger.d(TAG, "onTaskEnd", mark);
            }
        });
    }

    static {
        init();
    }

    public static void launch(){
        Launcher.launch(sInitContext);
    }

    private static void launch(Context context) {
        new ImageDetectStarter().init(context, BaiduImageDetector.class.getName());
        new KingdomStarter().init(context, null);
        new MusicCutStarter(new SimpleMusicPathProvider(BootStrapData.get(context).getMusicDir()))
                .init(context, null);
    }

    public static ColorGapContext createColorGapContext(){
        SimpleColorGapContext context = new SimpleColorGapContext();
        sInitContext.copySystemResource(context);
        return context;
    }

    public static ColorGapContext.InitializeParam getInitializeParam(){
        return sInitContext.getInitializeParam();
    }
}

//cmd /c start /wait /b ffmpeg -i D:\Users\heyunpeng\AppData\Local\Temp\media_files\e10adc3949ba59abbe56e057f20f883e\1\resource\1533358646780.mp4 -r 1 -ss 00:00:00.00 -s 1920*1080 D:\Users\heyunpeng\AppData\Local\Temp\media_files\e10adc3949ba59abbe56e057f20f883e\1\temp\1533358646780\img_%05d.jpg -y
