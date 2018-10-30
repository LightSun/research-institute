package com.heaven7.ve.cross_os;

/**
 * @author heaven7
 */
public interface ITransitionInfo extends IEffectInfo{

  /*  int TYPE_BLACK        = 1;
    int TYPE_WHITE        = 2;
    *//** 叠化 *//*
    int TYPE_MIX          = 3;
    int TYPE_LEFT_ROLLER  = 4;
    int TYPE_RIGHT_ROLLER = 5;
    int TYPE_LEFT_MOVE    = 6;
    int TYPE_RIGHT_MOVE   = 7;
    int TYPE_SPORT_BLUR   = 8;*/

    int TYPE_NONE       = 0;
    int TYPE_BLACK_FADE = 1;
    int TYPE_WHITE_FADE = 2;
    int TYPE_DISSOLVE   = 3;

    int TYPE_LEFT_MOVE  = 4;
    int TYPE_RIGHT_MOVE = 5;

    int TYPE_LEFT_MOVE_LOW  = 6;
    int TYPE_RIGHT_MOVE_LOW = 7;

    int TYPE_LEFT_ROLLER  = 8;
    int TYPE_RIGHT_ROLLER = 9;

}
