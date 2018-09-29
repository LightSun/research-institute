package com.heaven7.ve.test;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.Context;

import com.heaven7.ve.colorgap.MediaResourceScanner;
import com.heaven7.ve.cross_os.IMediaResourceItem;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author heaven7
 */
public abstract class SimpleMediaResourceScanner extends MediaResourceScanner {

    private final List<String> videoDataDirs;
    private final List<String> imageDataDirs;

    public SimpleMediaResourceScanner(@Nullable List<String> videoDataDirs, @Nullable List<String> imageDataDirs) {
        this.videoDataDirs = videoDataDirs;
        this.imageDataDirs = imageDataDirs;
    }
    public SimpleMediaResourceScanner(String dataDir) {
        List<String> list = Arrays.asList(dataDir);
        this.videoDataDirs = list;
        this.imageDataDirs = list;
    }

    @Override
    public String scan(Context context, IMediaResourceItem item, String srcDir) {
        if(item.isImage()){
            if(Predicates.isEmpty(imageDataDirs)){
                return null;
            }
            return VisitServices.from(imageDataDirs).map(new ResultVisitor<String, String>() {
                @Override
                public String visit(String s, Object param) {
                    return getImageDataFile(context, s, item);
                }
            }).query(new PredicateVisitor<String>() {
                @Override
                public Boolean visit(String s, Object param) {
                    return new File(s).exists();
                }
            });
        }else{
            if(Predicates.isEmpty(videoDataDirs)){
                return null;
            }
            return VisitServices.from(videoDataDirs).map(new ResultVisitor<String, String>() {
                @Override
                public String visit(String s, Object param) {
                    return getVideoDataFile(context, s, item);
                }
            }).query(new PredicateVisitor<String>() {
                @Override
                public Boolean visit(String s, Object param) {
                    return new File(s).exists();
                }
            });
        }
    }

    protected abstract String getVideoDataFile(Context context, String imageDataDir, IMediaResourceItem item);
    protected abstract String getImageDataFile(Context context, String imageDataDir, IMediaResourceItem item);
}
