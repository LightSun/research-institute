package com.vida.ai.test;

import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.impl.SimpleColorGapContext;
import com.heaven7.ve.starter.ImageDetectStarter;
import com.heaven7.ve.starter.KingdomStarter;
import com.heaven7.ve.starter.MusicCutStarter;
import com.vida.ai.third.baidu.BaiduImageDetector;

/**
 * @author heaven7
 */
public class Launcher {

    public static void launch(Context context) {
        new ImageDetectStarter().init(context, BaiduImageDetector.class.getName());
        new KingdomStarter().init(context, null);
       //TODO new MusicCutStarter(null).init(context, null);
    }

    public static void main(String[] args) {
        launch(new SimpleColorGapContext());
    }
}

//cmd /c start /wait /b ffmpeg -i D:\Users\heyunpeng\AppData\Local\Temp\media_files\e10adc3949ba59abbe56e057f20f883e\1\resource\1533358646780.mp4 -r 1 -ss 00:00:00.00 -s 1920*1080 D:\Users\heyunpeng\AppData\Local\Temp\media_files\e10adc3949ba59abbe56e057f20f883e\1\temp\1533358646780\img_%05d.jpg -y