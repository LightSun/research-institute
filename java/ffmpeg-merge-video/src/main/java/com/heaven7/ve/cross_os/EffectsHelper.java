package com.heaven7.ve.cross_os;

/**
 * @author heaven7
 */
public class EffectsHelper {

    public static void setTypeForTransition(ITransitionInfo info, String val) {
        int type;
        switch (val) {
            case "black_fade":
                type = ITransitionInfo.TYPE_BLACK;
                break;
            case "white_fade":
                type = ITransitionInfo.TYPE_WHITE;
                break;
            case "mix":
                type = ITransitionInfo.TYPE_MIX;
                break;
            case "left_roller":
                type = ITransitionInfo.TYPE_LEFT_ROLLER;
                break;
            case "right_roller":
                type = ITransitionInfo.TYPE_RIGHT_ROLLER;
                break;
            case "left_move":
                type = ITransitionInfo.TYPE_LEFT_MOVE;
                break;
            case "right_move":
                type = ITransitionInfo.TYPE_RIGHT_MOVE;
                break;
            case "sport_blur":
                type = ITransitionInfo.TYPE_SPORT_BLUR;
                break;
            default:
                throw new UnsupportedOperationException("wrong transition value = " + val);
        }
        info.setType(type);
    }

    public static void setTypeForSpecialEffect(ISpecialEffectInfo sei, String val) {
        int type;
        switch (val) {
            case "slow_zoom_in":
                type = ISpecialEffectInfo.TYPE_SLOW_ZOOM_IN;
                break;
            case "slow_zoom_out":
                type = ISpecialEffectInfo.TYPE_SLOW_ZOOM_OUT;
                break;
            case "left_move":
                type = ISpecialEffectInfo.TYPE_LEFT_MOVE;
                break;
            case "right_move":
                type = ISpecialEffectInfo.TYPE_RIGHT_MOVE;
                break;
            case "very_slow":
                type = ISpecialEffectInfo.TYPE_VERY_SLOW;
                break;
            default:
                throw new UnsupportedOperationException("wrong transition value = " + val);
        }
        sei.setType(type);
    }
}
