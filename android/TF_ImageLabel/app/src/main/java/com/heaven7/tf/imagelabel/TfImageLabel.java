package com.heaven7.tf.imagelabel;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.support.annotation.NonNull;
import android.util.SparseIntArray;
import android.view.Surface;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions;
import com.heaven7.core.util.Logger;

import java.util.List;

import static android.content.Context.CAMERA_SERVICE;

/**
 * Created by heaven7 on 2018/7/2 0002.
 */
//https://firebase.google.com/docs/ml-kit/android/label-images?authuser=0
public class TfImageLabel {

    private static final String TAG = "TfImageLabel";
    private final Context mContext;
    private FirebaseVisionLabelDetector detector;

    public TfImageLabel(Context context) {
        mContext = context;
        FirebaseVisionLabelDetectorOptions options =
                new FirebaseVisionLabelDetectorOptions.Builder()
                        .setConfidenceThreshold(0.5f)
                        .build();
       /* FirebaseApp.initializeApp(context, new FirebaseOptions.Builder()
                .build());*/
        detector = FirebaseVision.getInstance()
                .getVisionLabelDetector(options);
    }

    public void execute() {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(
                BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_label2));

        Task<List<FirebaseVisionLabel>> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionLabel>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionLabel> labels) {
                                        // Task completed successfully
                                        for (FirebaseVisionLabel lvi: labels){
                                             Logger.i(TAG, "onSuccess", "FirebaseVisionLabel: "
                                                     + toLabelString(lvi));
                                        }
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        e.printStackTrace();
                                    }
                                });

    }

    private static String toLabelString(FirebaseVisionLabel lvi) {
        return "entityId = " + lvi.getEntityId() + " ,label = " + lvi.getLabel()
                + " ,confidence = "+ lvi.getConfidence();
    }

}
