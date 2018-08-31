package com.heaven7.test;

import com.heaven7.ve.colorgap.CutInfo;
import com.heaven7.ve.colorgap.MusicCutter;
import com.heaven7.ve.colorgap.impl.SimpleColorGapContext;
import com.heaven7.ve.colorgap.impl.SimpleMusicPathProvider;
import com.heaven7.ve.starter.MusicCutStarter;
import org.junit.Test;

/**
 * @author heaven7
 */
public class MusicCutStartterTest {


    @Test
    public void test1(){
        SimpleColorGapContext context = new SimpleColorGapContext();
        new MusicCutStarter(new SimpleMusicPathProvider("E:\\tmp\\music_cut"))
        .init(context, null);

        assert context.getMusicCutter() != null;
        MusicCutter cutter = context.getMusicCutter();
        CutInfo[] infos = cutter.cut(null, new String[]{"E:\\tmp\\music_cut\\M1.mp3"});
        assert infos.length == 1;
        System.out.println();
    }
}
