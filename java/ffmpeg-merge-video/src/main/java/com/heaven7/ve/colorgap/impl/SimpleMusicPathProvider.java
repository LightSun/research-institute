package com.heaven7.ve.colorgap.impl;

import com.heaven7.ve.colorgap.MusicPathProvider;
import com.heaven7.ve.starter.MusicCutStarter;

import java.io.File;

/**
 * @author heaven7
 */
public class SimpleMusicPathProvider implements MusicPathProvider {

    private final String dir;
    public SimpleMusicPathProvider(String dir) {
        this.dir = dir;
    }
    @Override
    public String getMusicPath(MusicCutStarter.MusicCutData data) {
        return dir + File.separator + "M" + data.getName() + ".mp3";
    }
}
