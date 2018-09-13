package com.heaven7.ve;

/**
 * 转场信息.
 * Created by heaven7 on 2018/1/16 0016.
 */
public class TransitionInfo extends EffectInfo {

    //private static final String TAG = "TransitionInfo";

    public static final int TYPE_BLACK = 1;
    public static final int TYPE_WHITE = 2;
    public static final int TYPE_MIX   = 3;
    public static final int TYPE_LEFT_ROLLER  = 4;
    public static final int TYPE_RIGHT_ROLLER = 5;
    public static final int TYPE_LEFT_MOVE    = 6;
    public static final int TYPE_RIGHT_MOVE   = 7;
    public static final int TYPE_SPORT_BLUR   = 8;

    public void setTypeFrom(String val) {
        int type;
        switch (val){
            case "black_fade":
                type = TYPE_BLACK;
                break;
            case "white_fade":
                type = TYPE_WHITE;
                break;
            case "mix":
                type = TYPE_MIX;
                break;
            case "left_roller":
                type = TYPE_LEFT_ROLLER;
                break;
            case "right_roller":
                type = TYPE_RIGHT_ROLLER;
                break;
            case "left_move":
                type = TYPE_LEFT_MOVE;
                break;
            case "right_move":
                type = TYPE_RIGHT_MOVE;
                break;
            case "sport_blur":
                type = TYPE_SPORT_BLUR;
                break;
            default:
                throw new UnsupportedOperationException("wrong transition value = " + val);
        }
        setType(type);
    }

}
