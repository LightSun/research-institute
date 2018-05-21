package com.heaven7.ve;

import com.heaven7.core.util.Logger;

/**
 * 转场信息.
 * Created by heaven7 on 2018/1/16 0016.
 */
public class TransitionInfo extends EffectInfo {

    private static final String TAG = "TransitionInfo";

    public static final int TYPE_NONE = 0;
    public static final int TYPE_BLACK = 1;
    public static final int TYPE_DISSOLVE = 2;
    public static final int TYPE_LIGHTNING = 3;
    public static final int TYPE_VAGUE = 4;

    public native int getLeftDuration();
    public native void setLeftDuration(int leftDuration);
    public native int getRightDuration();
    public native void setRightDuration(int rightDuration);

    /*public int getIconId() {
        switch (getType()) {
            case TYPE_NONE:
                return R.drawable.cut_nothing;
            case TYPE_BLACK:
                return R.drawable.cut_black;
            case TYPE_DISSOLVE:
                return R.drawable.cut_dissolve;
            case TYPE_LIGHTNING:
                return R.drawable.cut_lightning;
            case TYPE_VAGUE:
                return R.drawable.cut_vague;

            default:
                Logger.w(TAG, "getResourceId", "un support type = " + getType());
        }
        return R.drawable.cut_nothing;
    }*/

    public String getTypeString() {
        switch (getType()) {
            case TYPE_NONE:
                return "TYPE_NONE";
            case TYPE_BLACK:
                return "TYPE_BLACK";
            case TYPE_DISSOLVE:
                return "TYPE_DISSOLVE";
            case TYPE_LIGHTNING:
                return "TYPE_LIGHTNING";
            case TYPE_VAGUE:
                return "TYPE_VAGUE";

            default:
                Logger.w(TAG, "getResourceId", "un support type = " + getType());
        }
        return null;
    }

    @Override
    protected int getNativeType() {
        return NTYPE_TRANSITION;
    }

    @Override
    public void setFrom(TimeTraveller src) {
        super.setFrom(src);
        if(src instanceof TransitionInfo){
            TransitionInfo ti = (TransitionInfo) src;
            setLeftDuration(ti.getLeftDuration());
            setRightDuration(ti.getRightDuration());
        }
    }
}
