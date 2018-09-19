package com.heaven7.advance;

import com.heaven7.advance.math.Convolution;
import com.heaven7.advance.math.IntConvolution;
import org.junit.Test;

public class ConvolutionTest {

    @Test
    public void testIntInt2() {
        //3行， 4列
        int[][] data = new int[][]{
                new int[]{11, 22, 33, 34},
                new int[]{40, 50, 68, 76},
                new int[]{7004, 8009, 9002, 64},
        };

        int[][] core = new int[][]{
                new int[]{1, -1},
                new int[]{-1, 1},
        };
        Matrix2<Integer> mat = Matrix2.ofIntArrayArray(data);
        System.out.println(mat.getRowCount());
        System.out.println(mat.getColumnCount());

        logByMode(core, mat, IntConvolution.MODE_SAME_ORIGIN);
        logByMode(core, mat, IntConvolution.MODE_SAME_VALID);
        logByMode(core, mat, IntConvolution.MODE_VALID_ORIGIN);
        logByMode(core, mat, IntConvolution.MODE_VALID_VALID);
    }

    private void logByMode(int[][] core, Matrix2<Integer> mat, int mode) {
        IntConvolution ic = new IntConvolution(mat);
        ic.setMode(mode);
        ic.setStrideX(2);

        Matrix2<Integer> matResult = ic.computeIntInt(core);
        System.out.println(Convolution.modeToString(mode));
        System.out.println(matResult.toString());
        System.out.println("---------------------------------------");
    }

    @Test
    public void testIntInt1() {
        //3行， 4列
        int[][] data = new int[][]{
                new int[]{11, 22, 33, 34},
                new int[]{40, 50, 68, 76},
                new int[]{7004, 8009, 9002, 64},
        };

        int[][] core = new int[][]{
                new int[]{1, -1},
                new int[]{-1, 1},
        };
        Matrix2<Integer> mat = Matrix2.ofIntArrayArray(data);
        System.out.println(mat.getRowCount());
        System.out.println(mat.getColumnCount());
        IntConvolution ic = new IntConvolution(mat);
        ic.setStrideX(2);
        Matrix2<Integer> matResult = ic.computeIntInt(core);
        System.out.println(matResult.toString());
    }
}
