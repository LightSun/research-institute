package com.heaven7.advance.math;

import com.heaven7.advance.Matrix2;
import com.heaven7.advance.Matrix2Utils;

public class DoubleConvolution extends Convolution<Double> {

    public DoubleConvolution(Matrix2<Double> mat) {
        super(mat);
    }

    @Override
    protected Matrix2Utils.ElementProvider<Double> getElementProvider() {
        return DOUBLE_0_PROVIDER;
    }

    @Override
    public Matrix2<Integer> computeIntInt(int[][] core) {
        return computeConvolutionIntInt(Matrix2.ofIntArrayArray(core), new Matrix2.ConvolutionCallback<Double, Integer, Integer>() {
            @Override
            public Integer multiple(Double f1, Integer f2) {
                return (int) (f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Float> computeIntFloat(int[][] core) {
        return computeConvolutionIntFloat(Matrix2.ofIntArrayArray(core), new Matrix2.ConvolutionCallback<Double, Integer, Float>() {
            @Override
            public Float multiple(Double f1, Integer f2) {
                return (float) (f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Double> computeIntDouble(int[][] core) {
        return computeConvolutionIntDouble(Matrix2.ofIntArrayArray(core), new Matrix2.ConvolutionCallback<Double, Integer, Double>() {
            @Override
            public Double multiple(Double f1, Integer f2) {
                return (f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Integer> computeFloatInt(float[][] core) {
        return computeConvolutionFloatInt(Matrix2.ofFloatArrayArray(core), new Matrix2.ConvolutionCallback<Double, Float, Integer>() {
            @Override
            public Integer multiple(Double f1, Float f2) {
                return (int) (f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Float> computeFloatFloat(float[][] core) {
        return computeConvolutionFloatFloat(Matrix2.ofFloatArrayArray(core), new Matrix2.ConvolutionCallback<Double, Float, Float>() {
            @Override
            public Float multiple(Double f1, Float f2) {
                return (float) (f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Double> computeFloatDouble(float[][] core) {
        return computeConvolutionFloatDouble(Matrix2.ofFloatArrayArray(core), new Matrix2.ConvolutionCallback<Double, Float, Double>() {
            @Override
            public Double multiple(Double f1, Float f2) {
                return (f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Integer> computeDoubleInt(double[][] core) {
        return computeConvolutionDoubleInt(Matrix2.ofDoubleArrayArray(core), new Matrix2.ConvolutionCallback<Double, Double, Integer>() {
            @Override
            public Integer multiple(Double f1, Double f2) {
                return (int) (f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Float> computeDoubleFloat(double[][] core) {
        return computeConvolutionDoubleFloat(Matrix2.ofDoubleArrayArray(core), new Matrix2.ConvolutionCallback<Double, Double, Float>() {
            @Override
            public Float multiple(Double f1, Double f2) {
                return (float) (f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Double> computeDoubleDouble(double[][] core) {
        return computeConvolutionDoubleDouble(Matrix2.ofDoubleArrayArray(core), new Matrix2.ConvolutionCallback<Double, Double, Double>() {
            @Override
            public Double multiple(Double f1, Double f2) {
                return (f1 * f2);
            }
        });
    }
}
