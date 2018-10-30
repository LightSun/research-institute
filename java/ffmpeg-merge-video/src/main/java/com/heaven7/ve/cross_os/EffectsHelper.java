package com.heaven7.ve.cross_os;

/**
 * @author heaven7
 */
public class EffectsHelper {

    public static void setTypeForTransition(ITransitionInfo info, String val) {
        int type;
        switch (val) {
            case "black_fade":
                type = ITransitionInfo.TYPE_BLACK_FADE;
                break;
            case "white_fade":
                type = ITransitionInfo.TYPE_WHITE_FADE;
                break;
            case "dissolve":
                type = ITransitionInfo.TYPE_DISSOLVE;
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
            case "right_move_low":
                type = ITransitionInfo.TYPE_RIGHT_MOVE_LOW;
                break;
            case "left_move_low":
                type = ITransitionInfo.TYPE_LEFT_MOVE_LOW;
                break;
//-------------------------------------------------------------
            case "sport_blur":
            default:
                throw new UnsupportedOperationException("wrong transition value = " + val);
        }
        info.setType(type);
    }

    public static void setTypeForSpecialEffect(ISpecialEffectInfo sei, String val) {
        int type;
        switch (val) {
            case "slow_zoom_in":
                type = ISpecialEffectInfo.TYPE_ZOOM_IN;
                break;
            case "slow_zoom_out":
                type = ISpecialEffectInfo.TYPE_ZOOM_OUT;
                break;
            case "left_move":
                type = ISpecialEffectInfo.TYPE_LEFT_TRANSLATION;
                break;
            case "right_move":
                type = ISpecialEffectInfo.TYPE_RIGHT_TRANSLATION;
                break;

            case "very_slow":
                type = ISpecialEffectInfo.TYPE_SPEED_VERY_SLOW;
                break;
            case "slow":
                type = ISpecialEffectInfo.TYPE_SPEED_SLOW;
                break;
            case "fast":
                type = ISpecialEffectInfo.TYPE_SPEED_FAST;
                break;
            case "very_fast":
                type = ISpecialEffectInfo.TYPE_SPEED_VERY_FAST;
                break;

            case "slow_to_fast":
                type = ISpecialEffectInfo.TYPE_SPEED_SLOW_TO_FAST;
                break;
            case "fast_to_slow":
                type = ISpecialEffectInfo.TYPE_SPEED_FAST_TO_SLOW;
                break;
            case "normal_to_slow":
                type = ISpecialEffectInfo.TYPE_SPEED_NORMAL_TO_SLOW;
                break;
            case "slow_to_normal":
                type = ISpecialEffectInfo.TYPE_SPEED_SLOW_TO_NORMAL;
                break;

            case "double_zoom_in":
                type = ISpecialEffectInfo.TYPE_DOUBLE_ZOOM_IN;
                break;

            case "split_screen_v2":
                type = ISpecialEffectInfo.TYPE_SPLIT_SCREEN_V2;
                break;
            case "split_screen_v3":
                type = ISpecialEffectInfo.TYPE_SPLIT_SCREEN_V3;
                break;
            case "split_screen_h2":
                type = ISpecialEffectInfo.TYPE_SPLIT_SCREEN_H2;
                break;
            case "split_screen_h3":
                type = ISpecialEffectInfo.TYPE_SPLIT_SCREEN_H3;
                break;

            default:
                throw new UnsupportedOperationException("wrong transition value = " + val);
        }
        sei.setType(type);
    }
}
