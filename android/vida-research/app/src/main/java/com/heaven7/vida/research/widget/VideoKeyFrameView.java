package com.heaven7.vida.research.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by heaven7 on 2019/1/28.
 */
//TODO
public class VideoKeyFrameView extends View {

    private int mFrameSize;
    private int mRoundSize;
    private int mFrameSpace;
    private int mTranslateX;

    private List<KeyFrameNode> mFrameNodes;

    public VideoKeyFrameView(Context context) {
        super(context);
    }

    public VideoKeyFrameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoKeyFrameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mTranslateX, 0);
        //TOdo draw
        canvas.restore();
    }
}
