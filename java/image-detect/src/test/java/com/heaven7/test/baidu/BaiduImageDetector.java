package com.heaven7.test.baidu;

import com.heaven7.java.image.detect.IHighLightData;
import com.heaven7.java.image.detect.ImageDetector;
import com.heaven7.java.image.detect.KeyPointData;
import com.heaven7.java.image.detect.Location;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.test.baidu.entity.VBodyAnalysis;
import com.heaven7.test.baidu.entity.VDetectionVidaSKI;
import com.heaven7.test.baidu.entity.VObjectDetect;
import okhttp3.Call;

import java.util.List;

/**
 * @author heaven7
 */
public class BaiduImageDetector implements ImageDetector {

    protected final VThirdBaiduService mService = new VThirdBaiduService();

    @Override
    public void detectHighLight(byte[] imageData, OnDetectCallback<List<IHighLightData>> callback) {
        mService.postDetectionVidaSKI(imageData, new VThirdBaiduCallback<VDetectionVidaSKI>() {
            @Override
            protected void onSuccess(Call call, VDetectionVidaSKI obj) {
                callback.onSuccess(convert1(obj.getResults()));
            }
            @Override
            protected void onFailed(Call call, String msg) {
                callback.onFailed(0, msg);
            }
        });
    }

    @Override
    public void detectKeyPoints(byte[] imageData, OnDetectCallback<List<KeyPointData>> callback) {
        mService.postBodyAnalysis(imageData, new VThirdBaiduCallback<VBodyAnalysis>() {
            @Override
            protected void onSuccess(Call call, VBodyAnalysis obj) {
                callback.onSuccess(convert1(obj.getPersonInfos()));
            }
            @Override
            protected void onFailed(Call call, String msg) {
                callback.onFailed(0, msg);
            }
        });
    }

    @Override
    public void detectSubjectIdentification(byte[] imageData, OnDetectCallback<Location> callback) {
        mService.postObjectDetect(imageData, new VThirdBaiduCallback<VObjectDetect>() {
            @Override
            protected void onSuccess(Call call, VObjectDetect obj) {
                callback.onSuccess(obj.getResult());
            }
            @Override
            protected void onFailed(Call call, String msg) {
                callback.onFailed(0, msg);
            }
        });
    }

    @Override
    public void detectHighLightBatch(int count, byte[] imageData, OnDetectCallback<List<IHighLightData>> callback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void detectKeyPointsBatch(int count, byte[] imageData, OnDetectCallback<List<KeyPointData>> callback) {
        //have bugs for person not equals.
        throw new UnsupportedOperationException();
        /*mService.postBodyAnalysis(imageData, new VThirdBaiduCallback<VBodyAnalysis>() {
            @Override
            protected void onSuccess(Call call, VBodyAnalysis obj) {
                List<VBodyAnalysis.PersonInfo> personInfos = obj.getPersonInfos();
                //倒序
                int size = personInfos.size();
                if(size % count != 0){
                    System.err.println("error .detectKeyPointsBatch  wrong... request count = "
                            + count + " ,real size = " + size);
                    return;
                }
                List<List<VBodyAnalysis.PersonInfo>> list = VisitServices.from(personInfos)
                        .groupService(size / count).reverseService().getAsList();
                callback.onBatchSuccess(convertHighLight2(list));
            }
        });*/
    }

    @Override
    public void detectSubjectIdentificationBatch(int count, byte[] imageData, OnDetectCallback<Location> callback) {
        throw new UnsupportedOperationException("Subject identification don't support .");
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> convert1(List<?> data){
        return (List<T>) data;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<List<T>> convert2(List<?> data){
        return (List<List<T>>) data;
    }
}
