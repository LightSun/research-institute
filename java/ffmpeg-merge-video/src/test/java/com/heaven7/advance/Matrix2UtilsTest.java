package com.heaven7.advance;

import org.junit.Test;

import static com.heaven7.advance.Matrix2Utils.fillHeight;
import static com.heaven7.advance.Matrix2Utils.fillWidth;

public class Matrix2UtilsTest {

    @Test
    public void testDropHeight(){
        int[][] data = new int[][]{
                new int[]{1, 2, 3, 4},
                new int[]{4, 5, 6, 7},
                new int[]{7, 8, 9, 10},
        };
        Matrix2<Integer> mat = Matrix2.ofIntArrayArray(data);
        Matrix2<Integer> mat2 = mat.copy();
        Matrix2<Integer> mat3 = mat.copy();

        Matrix2Utils.dropHeight(mat2, 1, true);
        System.out.println(mat2.toString());

        Matrix2Utils.dropHeight(mat3, 2, false);
        System.out.println(mat3.toString());
    }
    @Test
    public void testDropWidth(){
        int[][] data = new int[][]{
                new int[]{1, 2, 3, 4},
                new int[]{4, 5, 6, 7},
                new int[]{7, 8, 9, 10},
        };
        Matrix2<Integer> mat = Matrix2.ofIntArrayArray(data);
        Matrix2<Integer> mat2 = mat.copy();
        Matrix2<Integer> mat3 = mat.copy();

        Matrix2Utils.dropWidth(mat2, 1, true);
        System.out.println(mat2.toString());

        Matrix2Utils.dropWidth(mat3, 2, false);
        System.out.println(mat3.toString());
    }

    @Test
    public void testFillAndMerge() {
        int[][] data = new int[][]{
                new int[]{1, 2, 3, 4},
                new int[]{4, 5, 6, 7},
                new int[]{7, 8, 9, 10},
        };
        Matrix2<Integer> mat = Matrix2.ofIntArrayArray(data);
        System.out.println(mat.getRawValues().get(0));
        Matrix2<Integer> mat2 = mat.copy();

        int[][] wArr = new int[][]{
                new int[]{-1, -2, -3, -4},
                new int[]{-4, -5, -6, -7},
        };
        fillWidth(mat, wArr.length, wArr, new Matrix2Utils.ElementProvider<Integer>() {
            @Override
            public Integer provide(int wIndex, int hIndex, Object param) {
                int[][] arr = (int[][]) param;
                return arr[wIndex][hIndex];
            }
        });
        System.out.println(mat.toString());

        int[][] hArr = new int[][]{
                new int[]{100, 200},
                new int[]{101, 201},
                new int[]{102, 202},
                new int[]{103, 203},
                new int[]{104, 204},
        };
        fillHeight(mat, hArr[0].length, hArr, new Matrix2Utils.ElementProvider<Integer>() {
            @Override
            public Integer provide(int wIndex, int hIndex, Object param) {
                int[][] arr = (int[][]) param;
                return arr[wIndex][hIndex];
            }
        });
        System.out.println(mat.toString());

        Matrix2Utils.mergeByWidth(mat2, Matrix2.ofIntArrayArray(wArr));
        System.out.println(mat2.toString());

        Matrix2Utils.mergeByHeight(mat2, Matrix2.ofIntArrayArray(hArr));
        System.out.println(mat2.toString());
    }
}
