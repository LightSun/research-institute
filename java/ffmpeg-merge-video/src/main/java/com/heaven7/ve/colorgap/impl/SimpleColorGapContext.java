package com.heaven7.ve.colorgap.impl;

import com.heaven7.ve.anno.SystemResource;
import com.heaven7.ve.collect.ColorGapPerformanceCollector;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.kingdom.FileResourceManager;
import com.heaven7.ve.kingdom.Kingdom;
import com.heaven7.ve.template.VETemplate;
import com.heaven7.ve.utils.SharedThreadPool;

/**
 * @author heaven7
 */
public class SimpleColorGapContext implements ColorGapContext {

    @SystemResource
    private MusicCutter mMusicCutter;
    @SystemResource
    private InitializeParam mInitParam;
    private SharedThreadPool mPool;

    private Kingdom mKingdom;
    private ColorGapPerformanceCollector mCollector;
    private MontageParam mMontageParam;
    private DebugParam mDebugParam;
    private MediaResourceConfiguration mMediaConfig;
    private final FileResourceManager mFileResourceM = new FileResourceManager();

    @Override
    public MediaResourceConfiguration getMediaResourceConfiguration() {
        return mMediaConfig;
    }
    @Override
    public void setMediaResourceConfiguration(MediaResourceConfiguration mrc) {
        this.mMediaConfig = mrc;
    }

    @Override
    public void setInitializeParam(InitializeParam ip) {
        this.mInitParam = ip;
    }
    @Override
    public InitializeParam getInitializeParam() {
        return mInitParam;
    }

    @Override
    public void setDebugParam(DebugParam param) {
        this.mDebugParam = param;
    }
    @Override
    public DebugParam getDebugParam() {
        return getInitializeParam().isDebug() ? mDebugParam : DebugParam.DEFAULT;
    }

    @Override
    public void setMontageParameter(MontageParam param) {
        this.mMontageParam = param;
        mFileResourceM.setEffect(param.getEffectFileName());
        mFileResourceM.setTemplate(param.getTemplateFileName());
        mFileResourceM.resolve(this);
    }
    @Override
    public MontageParam getMontageParameter() {
        return mMontageParam;
    }

    @Override
    public FileResourceManager getFileResourceManager() {
        return mFileResourceM;
    }
    @Override
    public VETemplate getTemplate() {
        return getFileResourceManager().getVETemplate();
    }
    @Override
    public int getTestType() {
        return mInitParam != null ? mInitParam.getTestType() : TEST_TYPE_SERVER;
    }

    @Override
    public Kingdom getKingdom() {
        return mKingdom;
    }
    @Override
    public void setKingdom(Kingdom kingdom) {
        this.mKingdom = kingdom;
    }

    @Override
    public void setColorGapPerformanceCollector(ColorGapPerformanceCollector collector) {
        this.mCollector = collector;
    }
    @Override
    public ColorGapPerformanceCollector getColorGapPerformanceCollector() {
        return mCollector;
    }

    @Override
    public MusicCutter getMusicCutter() {
        return mMusicCutter;
    }
    @Override
    public void setMusicCutter(MusicCutter provider) {
        this.mMusicCutter = provider;
    }

    @Override
    public SharedThreadPool getSharedThreadPool() {
        if(mPool == null){
            mPool = new SharedThreadPool();
        }
        return mPool;
    }
    @Override
    public void setSharedThreadPool(SharedThreadPool pool) {
        this.mPool = pool;
    }

    @Override
    public void copySystemResource(ColorGapContext dst) {
        dst.setMusicCutter(getMusicCutter());
        dst.setInitializeParam(getInitializeParam());
        dst.setSharedThreadPool(getSharedThreadPool());
    }
}
