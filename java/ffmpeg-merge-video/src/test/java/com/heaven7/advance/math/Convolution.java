package com.heaven7.advance.math;

import com.heaven7.advance.Matrix2;
import com.heaven7.advance.Matrix2Utils;
import com.heaven7.java.visitor.PileVisitor;

import static com.heaven7.advance.Matrix2Utils.DOUBLE_0_PROVIDER;
import static com.heaven7.advance.Matrix2Utils.FLOAT_0_PROVIDER;
import static com.heaven7.advance.Matrix2Utils.INT_0_PROVIDER;

/**
 * 卷积
 * n 是输入矩阵，f是卷积核, padding p(四周都有),  步长s (stride)
 * 保持原图像矩阵的大小。满足 (n+2p-f+1) = n ,即 p=(f−1)/2.
 *  输出 (n + 2p -f)/s + 1
 * @author heaven7
 */
public abstract class Convolution<T> {

    /**
     * pre process 'SAME': fill 0 if not enough.
     * result 'ORIGIN ': means out put matrix size is decide by raw matrix and convolution core.
     */
    public static final int MODE_SAME_ORIGIN = 1;
    /**
     * pre process 'SAME': fill 0 if not enough.
     * result 'VALID'    : drop if not enough.
     */
    public static final int MODE_SAME_VALID = 2;
    /**
     * pre process 'VALID': drop if not enough.
     * result 'ORIGIN ': means out put matrix size is decide by raw matrix and convolution core.
     */
    public static final int MODE_VALID_ORIGIN = 3;
    /**
     * pre process 'VALID': drop if not enough.
     * result 'VALID' : drop if not enough.
     */
    public static final int MODE_VALID_VALID = 4;


    private final Matrix2<T> mat;
    private int strideX = 1;
    private int strideY = 1;
    private int mode = MODE_SAME_ORIGIN;

    public Convolution(Matrix2<T> mat) {
        this.mat = mat;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Matrix2<T> getMatrix2() {
        return mat;
    }

    public int getStrideX() {
        return strideX;
    }

    public void setStrideX(int strideX) {
        this.strideX = strideX;
    }

    public int getStrideY() {
        return strideY;
    }

    public void setStrideY(int strideY) {
        this.strideY = strideY;
    }

    public abstract Matrix2<Integer> computeIntInt(int[][] core);

    public abstract Matrix2<Float> computeIntFloat(int[][] core);

    public abstract Matrix2<Double> computeIntDouble(int[][] core);

    public abstract Matrix2<Integer> computeFloatInt(float[][] core);

    public abstract Matrix2<Float> computeFloatFloat(float[][] core);

    public abstract Matrix2<Double> computeFloatDouble(float[][] core);

    public abstract Matrix2<Integer> computeDoubleInt(double[][] core);

    public abstract Matrix2<Float> computeDoubleFloat(double[][] core);

    public abstract Matrix2<Double> computeDoubleDouble(double[][] core);

    /**
     * compute the convolution .
     *
     * @param core     the core matrix of convolution
     * @param coreSum  the sum of convolution core matrix
     * @param callback the callback
     * @param sum      the sum visitor of result
     * @param provider the default value provider of result
     * @param <C>      the element type of convolution core .
     * @param <R>      the result type of matrix
     * @return the result
     */
    protected final <C, R> Matrix2<R> computeConvolution(Matrix2<C> core, double coreSum,
                                                         Matrix2.ConvolutionCallback<T, C, R> callback, PileVisitor<R> sum,
                                                         Matrix2.AverageCallback<R> average, Matrix2.ElementProvider<R> provider) {
        Matrix2<T> appositeMatrix = getAppositeMatrix(core.getRowCount(), core.getColumnCount());
        final int outWidth = getOutputWidth(appositeMatrix, core.getRowCount());
        final int outHeight = getOutputHeight(appositeMatrix, core.getColumnCount());
        return appositeMatrix.computeConvolution(core, coreSum, outWidth, outHeight,
                getStrideX(), getStrideY(), callback, sum, average,provider);
    }

    /**
     * compute the convolution .
     *
     * @param core     the core matrix of convolution
     * @param callback the callback
     * @return the result
     */
    public final Matrix2<Integer> computeConvolutionIntInt(Matrix2<Integer> core, Matrix2.ConvolutionCallback<T, Integer, Integer> callback) {
        return computeConvolution(core, Matrix2Utils.sumInt(core),
                callback, PileVisitor.INT_ADD, Matrix2Utils.INT_AVERAGE, INT_0_PROVIDER);
    }
    /**
     * compute the convolution .
     *
     * @param core     the core matrix of convolution
     * @param callback the callback
     * @return the result
     */
    public final Matrix2<Float> computeConvolutionIntFloat(Matrix2<Integer> core, Matrix2.ConvolutionCallback<T, Integer, Float> callback) {
        return computeConvolution(core, Matrix2Utils.sumInt(core),
                callback, PileVisitor.FLOAT_ADD, Matrix2Utils.FLOAT_AVERAGE, FLOAT_0_PROVIDER);
    }
    /**
     * compute the convolution .
     *
     * @param core     the core matrix of convolution
     * @param callback the callback
     * @return the result
     */
    public final Matrix2<Double> computeConvolutionIntDouble(Matrix2<Integer> core, Matrix2.ConvolutionCallback<T, Integer, Double> callback) {
        return computeConvolution(core, Matrix2Utils.sumInt(core),
                callback, PileVisitor.DOUBLE_ADD, Matrix2Utils.DOUBLE_AVERAGE, DOUBLE_0_PROVIDER);
    }
    /**
     * compute the convolution .
     *
     * @param core     the core matrix of convolution
     * @param callback the callback
     * @return the result
     */
    public final Matrix2<Integer> computeConvolutionFloatInt(Matrix2<Float> core, Matrix2.ConvolutionCallback<T, Float, Integer> callback) {
        return computeConvolution(core, Matrix2Utils.sumFloat(core),
                callback, PileVisitor.INT_ADD, Matrix2Utils.INT_AVERAGE, INT_0_PROVIDER);
    }
    /**
     * compute the convolution .
     *
     * @param core     the core matrix of convolution
     * @param callback the callback
     * @return the result
     */
    public final Matrix2<Float> computeConvolutionFloatFloat(Matrix2<Float> core, Matrix2.ConvolutionCallback<T, Float, Float> callback) {
        return computeConvolution(core, Matrix2Utils.sumFloat(core),
                callback, PileVisitor.FLOAT_ADD, Matrix2Utils.FLOAT_AVERAGE, FLOAT_0_PROVIDER);
    }
    /**
     * compute the convolution .
     *
     * @param core     the core matrix of convolution
     * @param callback the callback
     * @return the result
     */
    public final Matrix2<Double> computeConvolutionFloatDouble(Matrix2<Float> core, Matrix2.ConvolutionCallback<T, Float, Double> callback) {
        return computeConvolution(core, Matrix2Utils.sumFloat(core),
                callback, PileVisitor.DOUBLE_ADD, Matrix2Utils.DOUBLE_AVERAGE, DOUBLE_0_PROVIDER);
    }
    /**
     * compute the convolution .
     *
     * @param core     the core matrix of convolution
     * @param callback the callback
     * @return the result
     */
    public final Matrix2<Integer> computeConvolutionDoubleInt(Matrix2<Double> core, Matrix2.ConvolutionCallback<T, Double, Integer> callback) {
        return computeConvolution(core, Matrix2Utils.sumDouble(core),
                callback, PileVisitor.INT_ADD, Matrix2Utils.INT_AVERAGE, INT_0_PROVIDER);
    }
    /**
     * compute the convolution .
     *
     * @param core     the core matrix of convolution
     * @param callback the callback
     * @return the result
     */
    public final Matrix2<Float> computeConvolutionDoubleFloat(Matrix2<Double> core, Matrix2.ConvolutionCallback<T, Double, Float> callback) {
        return computeConvolution(core, Matrix2Utils.sumDouble(core),
                callback, PileVisitor.FLOAT_ADD, Matrix2Utils.FLOAT_AVERAGE, FLOAT_0_PROVIDER);
    }
    /**
     * compute the convolution .
     *
     * @param core     the core matrix of convolution
     * @param callback the callback
     * @return the result
     */
    public final Matrix2<Double> computeConvolutionDoubleDouble(Matrix2<Double> core, Matrix2.ConvolutionCallback<T, Double, Double> callback) {
        return computeConvolution(core, Matrix2Utils.sumDouble(core),
                callback, PileVisitor.DOUBLE_ADD, Matrix2Utils.DOUBLE_AVERAGE, DOUBLE_0_PROVIDER);
    }
    public static String modeToString(int mode){
        switch (mode){
            case MODE_SAME_ORIGIN:
                return "MODE_SAME_ORIGIN";
            case MODE_SAME_VALID:
                return "MODE_SAME_VALID";
            case MODE_VALID_ORIGIN:
                return "MODE_VALID_ORIGIN";
            case MODE_VALID_VALID:
                return "MODE_VALID_VALID";

           default:
               throw new UnsupportedOperationException("wrong mode = " + mode);
        }
    }

    /**
     * get the element provider, this often used to fill the gap of matrix.
     *
     * @return the element provider
     */
    protected abstract Matrix2.ElementProvider<T> getElementProvider();

    /**
     * get apposite matrix. which may be filled as new matrix.
     * That means the matrix is effect by {@linkplain #getStrideX()}, {@linkplain #getStrideY()} and {@linkplain #getMode()}.
     * @param coreWidth  the width of core convolution
     * @param coreHeight the height of core convolution
     * @return the new matrix
     */
    protected Matrix2<T> getAppositeMatrix(int coreWidth, int coreHeight) {
        Matrix2<T> mat = this.mat.copy();
        final int strideX = getStrideX();
        final int strideY = getStrideY();

        switch (getMode()) {
            case MODE_SAME_ORIGIN:
            case MODE_SAME_VALID:
            {
                //fill x if need
                final int deltaX = mat.getRowCount() - coreWidth;
                int leftX = deltaX % strideX;
                if (leftX > 0) {
                    Matrix2Utils.fillRow(mat, strideX - leftX, null, getElementProvider());
                }
                //fill y if need
                final int deltaY = mat.getColumnCount() - coreHeight;
                int leftY = deltaY % strideY;
                if (leftY > 0) {
                    Matrix2Utils.fillColumn(mat, strideY - leftY, null, getElementProvider());
                }
            }
            break;

            case MODE_VALID_ORIGIN:
            case MODE_VALID_VALID:
            {
                final int deltaX = mat.getRowCount() - coreWidth;
                int leftX = deltaX % strideX;
                if (leftX > 0) {
                    Matrix2Utils.dropWidth(mat, leftX, true);
                }
                final int deltaY = mat.getColumnCount() - coreHeight;
                int leftY = deltaY % strideY;
                if (leftY > 0) {
                    Matrix2Utils.dropHeight(mat, leftY, true);
                }
            }
            break;
        }
        System.out.println(mat.toString());
        System.out.println("strideX = " + strideX + " ,strideY = " + strideY);
        return mat;
    }

    private int getOutputWidth(Matrix2<T> src, int coreWidth) {
        int originWidth;
        switch (getMode()){
            case MODE_SAME_ORIGIN:
            case MODE_VALID_ORIGIN:
                originWidth = this.mat.getRowCount();
                break;

            case MODE_SAME_VALID:
            case MODE_VALID_VALID:
                originWidth = src.getRowCount();
                break;

            default:
                throw new UnsupportedOperationException("wrong mode");
        }
        //(n + 2p -f)/s + 1
        final int delta = originWidth - coreWidth;
        final int result = delta / getStrideX() + 1;
        if(delta % getStrideX() != 0){
            return result + 1;
        }
        return result;
    }

    private int getOutputHeight(Matrix2<T> src, int coreHeight) {
        int originHeight;
        switch (getMode()){
            case MODE_SAME_ORIGIN:
            case MODE_VALID_ORIGIN:
                originHeight = this.mat.getColumnCount();
                break;

            case MODE_SAME_VALID:
            case MODE_VALID_VALID:
                originHeight = src.getColumnCount();
                break;

            default:
                throw new UnsupportedOperationException("wrong mode");
        }
        final int delta = originHeight - coreHeight;
        final int result = delta /getStrideY() + 1;
        if(delta % getStrideY() != 0){
            return result + 1;
        }
        return result;
    }

}
