package com.heaven7.ve;

/**
 * the special effect
 * Created by heaven7 on 2018/3/29 0029.
 */

public class SpecialEffect extends EffectInfo{

     //1缓慢缩小2缓慢放大3左平移4右平移5极慢(慢速)
    public static final int TYPE_SLOW_ZOOM_IN  = 1;
    public static final int TYPE_SLOW_ZOOM_OUT = 2;
    public static final int TYPE_LEFT_MOVE     = 3;
    public static final int TYPE_RIGHT_MOVE    = 4;
    public static final int TYPE_VERY_SLOW     = 5;

    public void setTypeFrom(String val) {
        int type;
        switch (val){
            case "slow_zoom_in":
                type = TYPE_SLOW_ZOOM_IN;
                break;
            case "slow_zoom_out":
                type = TYPE_SLOW_ZOOM_OUT;
                break;
            case "left_move":
                type = TYPE_LEFT_MOVE;
                break;
            case "right_move":
                type = TYPE_RIGHT_MOVE;
                break;
            case "very_slow":
                type = TYPE_VERY_SLOW;
                break;
            default:
                throw new UnsupportedOperationException("wrong transition value = " + val);
        }
        setType(type);
    }

}
