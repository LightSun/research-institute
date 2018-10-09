package com.heaven7.vida.research.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.heaven7.android.util2.ViewUtil;
import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Throwables;

/**
 * help to locate the view. by locate type and gravity.
 * Created by heaven7 on 2018/10/8 0008.
 */
public class LocationHelper {

    public static final byte GRAVITY_LEFT    = 1;
    public static final byte GRAVITY_RIGHT   = 2;
    public static final byte GRAVITY_CENTER  = 3;

    public static final byte LOCATE_TOP      = 11;
    public static final byte LOCATE_BOTTOM   = 12;

    /** the retain space of left/right/top/bottom */
    private int retainSpace;
    private byte locate;
    private int locateOffset; // > 0 means to bottom, < 0 means to top
    private byte gravity;
    private int gravityOffset;
    private View placeView;
    /** the mark view which show above the anchor view */
    private View markView;
    /** auto fit edge or not .*/
    private boolean autoFitEdge;
    private Callback callback;

    private boolean fullScreen;
    private int stateBarHeight;

    private LocationHelper(LocationHelper.Builder builder) {
        this.retainSpace = builder.retainSpace;
        this.locate = builder.locate;
        this.locateOffset = builder.locateOffset;
        this.gravity = builder.gravity;
        this.gravityOffset = builder.gravityOffset;
        this.placeView = builder.placeView;
        this.autoFitEdge = builder.autoFitEdge;
        this.callback = builder.callback;
        init();
    }

    private void init() {
        Context context = placeView.getContext();
        //get the activity
        while (!(context instanceof Activity) && context instanceof ContextWrapper){
            context = ((ContextWrapper) context).getBaseContext();
        }
        this.fullScreen = context instanceof Activity && (((Activity) context).getWindow()
                .getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN ) != 0 ;
        this.stateBarHeight = ViewUtil.getStatusHeight(context);
    }

    public void applyHorizontal(View anchor){
        int placeViewWidth = getPlaceViewWidth();
        int placeViewHeight = getPlaceViewHeight();
        Logger.d("LocationHelper", "applyHorizontal", "placeViewWidth = " + placeViewWidth + " ,placeViewHeight = " + placeViewHeight);

        int width = anchor.getMeasuredWidth();
        int height = anchor.getMeasuredHeight();
        int[] cors = new int[2];
        anchor.getLocationOnScreen(cors);
        cors[1] = fullScreen ? cors[1] : cors[1] - stateBarHeight;

        //the center x, y
        int x = 0;
        int y = 0;
        switch (locate){
            case LOCATE_TOP:{
                switch (gravity){
                    case GRAVITY_LEFT:
                        x = cors[0];
                        y = cors[1] + locateOffset - placeViewHeight;
                        break;

                    case GRAVITY_RIGHT:
                        x = cors[0] + width - placeViewWidth;
                        y = cors[1] + locateOffset - placeViewHeight;
                        break;

                    case GRAVITY_CENTER:
                        x = cors[0] + width / 2 - placeViewWidth / 2;
                        y = cors[1] + locateOffset - placeViewHeight;
                        break;
                }
            }break;

            case LOCATE_BOTTOM:{
                switch (gravity){
                    case GRAVITY_LEFT:
                        x = cors[0];
                        y = cors[1] + height + locateOffset;
                        break;

                    case GRAVITY_RIGHT:
                        x = cors[0] + width - placeView.getMeasuredWidth();
                        y = cors[1] + height + locateOffset;
                        break;

                    case GRAVITY_CENTER:
                        x = cors[0] + width / 2 - placeView.getMeasuredWidth() / 2;
                        y = cors[1] + height + locateOffset;
                        break;
                }
            }break;

            default:
                throw new UnsupportedOperationException("locate = " + locate);
        }
        if(gravityOffset != 0){
            x += gravityOffset;
        }
        int[] xy = fitEdge(x, y);
        callback.applyLocation(xy[0], xy[1]);
        if(markView != null) {
            int mWidth = markView.getMeasuredWidth();
            //permit move
            int marginLeft = (cors[0] + width / 2) - (x + mWidth / 2);
            if(marginLeft > 0){
                callback.applyMarkMargin(marginLeft);
            }
        }
    }

    private int[] fitEdge(int x, int y) {
        int[] cors = new int[2];
        cors[0] = x;
        cors[1] = y;

        DisplayMetrics dm = CommonUtils.getDisplayMetrics(placeView.getContext());
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        if(autoFitEdge){
            if(x < retainSpace){
                cors[0] = retainSpace;
            }else{
                int delta = x + getPlaceViewWidth() -(screenWidth - retainSpace);
                if(delta > 0) {
                    cors[0] = x - delta;
                }
            }
            if(y < retainSpace){
                cors[1] = retainSpace;
            }else{
                int delta = y + getPlaceViewHeight() - (screenHeight - retainSpace);
                if(delta > 0){
                    cors[1] = y - delta;
                }
            }
        }
        return cors;
    }

    private int getPlaceViewWidth(){
        int width = placeView.getMeasuredWidth();
        if(width == 0){
            throw new IllegalStateException();
        }
        return width;
    }
    private int getPlaceViewHeight(){
        int height = placeView.getMeasuredHeight();
        if(height == 0){
            throw new IllegalStateException();
        }
        return height;
    }

    public interface Callback{
        void applyLocation(int x, int y);
        void applyMarkMargin(int marginLeft);
    }

    //-------------------------------------------------
    public int getRetainSpace() {
        return this.retainSpace;
    }

    public byte getLocate() {
        return this.locate;
    }

    public int getLocateOffset() {
        return this.locateOffset;
    }

    public byte getGravity() {
        return this.gravity;
    }

    public int getGravityOffset() {
        return this.gravityOffset;
    }

    public View getPlaceView() {
        return this.placeView;
    }

    public boolean isAutoFitEdge() {
        return this.autoFitEdge;
    }

    public Callback getCallback() {
        return this.callback;
    }

    public static class Builder {
        /** the retain space of left/right/top/bottom */
        private int retainSpace;
        private byte locate;
        private int locateOffset; // > 0 means to bottom, < 0 means to top
        private byte gravity;
        private int gravityOffset;
        private View placeView;
        /** auto fit edge or not .*/
        private boolean autoFitEdge;
        private Callback callback;

        public Builder setRetainSpace(int retainSpace) {
            this.retainSpace = retainSpace;
            return this;
        }

        public Builder setLocate(byte locate) {
            this.locate = locate;
            return this;
        }

        public Builder setLocateOffset(int locateOffset) {
            this.locateOffset = locateOffset;
            return this;
        }

        public Builder setGravity(byte gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setGravityOffset(int gravityOffset) {
            this.gravityOffset = gravityOffset;
            return this;
        }

        public Builder setPlaceView(View placeView) {
            this.placeView = placeView;
            return this;
        }

        public Builder setAutoFitEdge(boolean autoFitEdge) {
            this.autoFitEdge = autoFitEdge;
            return this;
        }

        public Builder setCallback(Callback callback) {
            this.callback = callback;
            return this;
        }

        public LocationHelper build() {
            Throwables.checkNull(placeView);
            return new LocationHelper(this);
        }
    }
}
