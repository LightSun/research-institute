package com.heaven7.test.baidu;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.image.ImageBatchDataSplitter;
import com.heaven7.java.image.detect.*;
import com.heaven7.java.visitor.util.SparseArray;
import com.heaven7.test.baidu.entity.VBodyAnalysis;
import com.heaven7.test.baidu.entity.VDetectionVidaSKI;
import com.heaven7.test.baidu.entity.VObjectDetect;
import okhttp3.Call;

import java.util.List;

/**
 * @author heaven7
 */
public class BaiduImageDetector implements ImageDetector {

    private final VThirdBaiduService mService = new VThirdBaiduService();
    private final ImageBatchDataSplitter<VBodyAnalysis.PersonInfo> mPerson_splitter = new ImageBatchDataSplitter.DefaultImageBatchDataSplitter<>();
    private final ImageBatchDataSplitter<VDetectionVidaSKI.Results> mSki_splitter = new ImageBatchDataSplitter.DefaultImageBatchDataSplitter<>();

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
    public void detectHighLightBatch(BatchInfo info, byte[] imageData, OnDetectCallback<List<IHighLightData>> callback) {
        mService.postDetectionVidaSKI(imageData, new VThirdBaiduCallback<VDetectionVidaSKI>() {
            @Override
            protected void onSuccess(Call call, VDetectionVidaSKI obj) {
                List<VDetectionVidaSKI.Results> results = obj.getResults();
                if(!Predicates.isEmpty(results)){
                    SparseArray<List<VDetectionVidaSKI.Results>> map = mSki_splitter.split(info.getCount(),
                            info.getWidth(), info.getHeight(), results);
                    callback.onBatchSuccess(convert2(map));
                }else{
                    callback.onFailed(0, "no data");
                }
            }
            @Override
            protected void onFailed(Call call, String msg) {
                callback.onFailed(0, msg);
            }
        });
    }

    @Override
    public void detectKeyPointsBatch(BatchInfo info, byte[] imageData, OnDetectCallback<List<KeyPointData>> callback) {
        mService.postBodyAnalysis(imageData, new VThirdBaiduCallback<VBodyAnalysis>() {
            @Override
            protected void onSuccess(Call call, VBodyAnalysis obj) {
                List<VBodyAnalysis.PersonInfo> personInfos = obj.getPersonInfos();
                if(Predicates.isEmpty(personInfos)){
                    callback.onFailed(0, "no data");
                    return;
                }
                SparseArray<List<VBodyAnalysis.PersonInfo>> lists = mPerson_splitter.split(info.getCount(),
                        info.getWidth(), info.getHeight(), personInfos);
                callback.onBatchSuccess(convert2(lists));
            }
            @Override
            protected void onFailed(Call call, String msg) {
                callback.onFailed(0, msg);
            }
        });
    }

    @Override
    public void detectSubjectIdentificationBatch(BatchInfo info, byte[] imageData, OnDetectCallback<Location> callback) {
        throw new UnsupportedOperationException("Subject identification don't support .");
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> convert1(List<?> data){
        return (List<T>) data;
    }

    @SuppressWarnings("unchecked")
    public static <T> SparseArray<List<T>> convert2(SparseArray<?> data){
        return (SparseArray<List<T>>) data;
    }
}
