package com.heaven7.ve.colorgap;

import com.heaven7.java.image.detect.AbstractBatchImageManager;
import com.heaven7.java.image.detect.BatchImageSubjectIdentifyManager;
import com.heaven7.java.image.detect.Location;
import com.heaven7.java.visitor.MapFireVisitor;
import com.heaven7.java.visitor.MapResultVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;

import java.util.List;
import java.util.Map;

/**
 * the subject recognize/identify helper
 * @author heaven7
 */
public abstract class SubjectRecognizeHelper implements AbstractBatchImageManager.Callback<Location> {

    private final BatchImageSubjectIdentifyManager mBisim;
    private final List<MediaPartItem> mItems;

    public SubjectRecognizeHelper(List<MediaPartItem> mItems) {
        List<String> images = VisitServices.from(mItems).map((item, param) -> item.getItem().getFilePath()).getAsList();
        this.mItems = mItems;
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
                return VisitServices.from(mItems).query(new PredicateVisitor<MediaPartItem>() {
                    @Override
                    public Boolean visit(MediaPartItem item, Object param) {
                        return item.getItem().getFilePath().equals(t.getKey());
                    }
                });
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
}
