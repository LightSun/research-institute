package com.heaven7.ve.colorgap;

import com.heaven7.java.image.ImageFactory;
import com.heaven7.java.image.detect.AbstractBatchImageManager;
import com.heaven7.java.image.detect.BatchImageSubjectIdentifyManager;
import com.heaven7.java.image.detect.IHighLightData;
import com.heaven7.java.image.detect.Location;
import com.heaven7.java.visitor.*;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * the subject recognize/identify helper
 * @author heaven7
 */
public abstract class SubjectRecognizeHelper implements AbstractBatchImageManager.Callback<Location> {

    private final BatchImageSubjectIdentifyManager mBisim;
    private final List<Pair> mPairs = new ArrayList<>();

    public SubjectRecognizeHelper(List<MediaPartItem> mItems) {
        List<String> images = VisitServices.from(mItems).map(new ResultVisitor<MediaPartItem, String>() {
            @Override
            public String visit(MediaPartItem item, Object param) {
                if(item.getItem().isVideo()){
                    long duration = item.imageMeta.getDuration();
                    int middleTime = (int) (duration / 2 / 1000);
                    KeyValuePair<Integer, List<IHighLightData>> highLight = item.getHighLight();
                    int time = highLight != null ? highLight.getKey() : middleTime;
                    return ImageFactory.getImageInitializer().getVideoFrameDelegate().getFrameImagePath(
                            item.item.getFilePath(), time);
                }
                return item.getItem().getFilePath();
            }
        }).getAsList();
        //save as pair
        VisitServices.from(images).fireWithIndex(new FireIndexedVisitor<String>() {
            @Override
            public Void visit(Object param, String imgPath, int index, int size) {
                mPairs.add(new Pair(mItems.get(index), imgPath));
                return null;
            }
        });
        this.mBisim = new BatchImageSubjectIdentifyManager(images);
    }

    public void start(){
        mBisim.detect(this);
    }

    @Override
    public void onCallback(Map<String, Location> map) {
        VisitServices.from(map).map2MapValue(new MapResultVisitor<String, Location, MediaPartItem>() {
            @Override
            public MediaPartItem visit(KeyValuePair<String, Location> t, Object param) {
                return VisitServices.from(mPairs).query(new PredicateVisitor<Pair>() {
                    @Override
                    public Boolean visit(Pair pair, Object param) {
                        return pair.imgPath.equals(t.getKey());
                    }
                }).partItem;
            }
        }).fire(new MapFireVisitor<MediaPartItem, Location>() {
            @Override
            public Boolean visit(KeyValuePair<MediaPartItem, Location> pair, Object param) {
                //TODO get shot type for Subject Recognize
                return null;
            }
        });
        onDone();
    }

    protected abstract void onDone();

    private static class Pair{
        final MediaPartItem partItem;
        final String imgPath;

        public Pair(MediaPartItem partItem, String imgPath) {
            this.partItem = partItem;
            this.imgPath = imgPath;
        }
    }

}
