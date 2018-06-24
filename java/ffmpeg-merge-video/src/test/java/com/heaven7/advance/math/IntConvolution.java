package com.heaven7.advance.math;

import com.heaven7.advance.Matrix2;
import com.heaven7.advance.Matrix2Utils;

import static com.heaven7.advance.Matrix2Utils.INT_0_PROVIDER;

public class IntConvolution extends Convolution<Integer> {

    public IntConvolution(Matrix2<Integer> mat) {
        super(mat);
    }

    @Override
    protected Matrix2Utils.ElementProvider<Integer> getElementProvider() {
        return INT_0_PROVIDER;
    }

    @Override
    public Matrix2<Integer> computeIntInt(int[][] core) {
        return computeConvolutionIntInt(Matrix2.ofIntArrayArray(core), new Matrix2.ConvolutionCallback<Integer, Integer, Integer>() {
            @Override
            public Integer multiple(Integer f1, Integer f2) {
                return f1 * f2;
            }
        });
    }

    @Override
    public Matrix2<Float> computeIntFloat(int[][] core) {
        return computeConvolutionIntFloat(Matrix2.ofIntArrayArray(core), new Matrix2.ConvolutionCallback<Integer, Integer, Float>() {
                    @Override
                    public Float multiple(Integer f1, Integer f2) {
                        return (float) (f1 * f2);
                    }
                });
    }

    @Override
    public Matrix2<Double> computeIntDouble(int[][] core) {
        return computeConvolutionIntDouble(Matrix2.ofIntArrayArray(core), new Matrix2.ConvolutionCallback<Integer, Integer, Double>() {
            @Override
            public Double multiple(Integer f1, Integer f2) {
                return (double) (f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Integer> computeFloatInt(float[][] core) {
        return computeConvolutionFloatInt(Matrix2.ofFloatArrayArray(core), new Matrix2.ConvolutionCallback<Integer, Float, Integer>() {
            @Override
            public Integer multiple(Integer f1, Float f2) {
                return (int) (f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Float> computeFloatFloat(float[][] core) {
        return computeConvolutionFloatFloat(Matrix2.ofFloatArrayArray(core), new Matrix2.ConvolutionCallback<Integer, Float, Float>() {
            @Override
            public Float multiple(Integer f1, Float f2) {
                return f1 * f2;
            }
        });
    }

    @Override
    public Matrix2<Double> computeFloatDouble(float[][] core) {
        return computeConvolutionFloatDouble(Matrix2.ofFloatArrayArray(core), new Matrix2.ConvolutionCallback<Integer, Float, Double>() {
            @Override
            public Double multiple(Integer f1, Float f2) {
                return  (double)(f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Integer> computeDoubleInt(double[][] core) {
        return computeConvolutionDoubleInt(Matrix2.ofDoubleArrayArray(core), new Matrix2.ConvolutionCallback<Integer, Double, Integer>() {
            @Override
            public Integer multiple(Integer f1, Double f2) {
                return  (int)(f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Float> computeDoubleFloat(double[][] core) {
        return computeConvolutionDoubleFloat(Matrix2.ofDoubleArrayArray(core), new Matrix2.ConvolutionCallback<Integer, Double, Float>() {
            @Override
            public Float multiple(Integer f1, Double f2) {
                return  (float)(f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Double> computeDoubleDouble(double[][] core) {
        return computeConvolutionDoubleDouble(Matrix2.ofDoubleArrayArray(core), new Matrix2.ConvolutionCallback<Integer, Double, Double>() {
            @Override
            public Double multiple(Integer f1, Double f2) {
                return (f1 * f2);
            }
        });
    }
}
