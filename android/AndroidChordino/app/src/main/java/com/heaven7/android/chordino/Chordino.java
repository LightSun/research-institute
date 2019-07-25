package com.heaven7.android.chordino;

/**
 * Created by heaven7 on 2019/7/24.
 */
public final class Chordino {

     public static int[] generateCuts(String musicPath){
         return nGenerateCuts(musicPath, "Chordino");
     }

     public static int[] testSplit(int start, int end){
         return nTestSplit(start, end);
     }
     public static int[] testMerge(int[] datas){
         return nTestMerge(datas);
     }

     private static native int[] nTestSplit(int start, int end);
     private static native int[] nTestMerge(int[] datas);
     private static native int[] nGenerateCuts(String musicPath, String simpleName);
}
