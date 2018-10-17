package com.heaven7.android.androidhotfix;

/**
 * bug fix use dex replace
 * Created by heaven7 on 2018/10/17 0017.
 */
public class TestBugFix {

    //bugs
    public int calculate(){
        int i = 0;
        int j = 10;
        return j/i;
    }
    //right
   /* public int calculate(){
        int i = 1;
        int j = 10;
        return j/i;
    }*/
}
