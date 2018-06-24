package com.heaven7.advance.math;

import com.heaven7.advance.Matrix2;
import com.heaven7.advance.Matrix2Utils;

import static com.heaven7.advance.Matrix2Utils.FLOAT_0_PROVIDER;

public class FloatConvolution extends Convolution<Float> {

    public FloatConvolution(Matrix2<Float> mat) {
        super(mat);
    }

    @Override
    protected Matrix2Utils.ElementProvider<Float> getElementProvider() {
        return FLOAT_0_PROVIDER;
    }

    @Override
    public Matrix2<Integer> computeIntInt(int[][] core) {
        return computeConvolutionIntInt(Matrix2.ofIntArrayArray(core), new Matrix2.ConvolutionCallback<Float, Integer, Integer>() {
            @Override
            public Integer multiple(Float f1, Integer f2) {
                return (int)(f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Float> computeIntFloat(int[][] core) {
        return computeConvolutionIntFloat(Matrix2.ofIntArrayArray(core), new Matrix2.ConvolutionCallback<Float, Integer, Float>() {
            @Override
            public Float multiple(Float f1, Integer f2) {
                return (f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Double> computeIntDouble(int[][] core) {
        return computeConvolutionIntDouble(Matrix2.ofIntArrayArray(core), new Matrix2.ConvolutionCallback<Float, Integer, Double>() {
            @Override
            public Double multiple(Float f1, Integer f2) {
                return (double)(f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Integer> computeFloatInt(float[][] core) {
        return computeConvolutionFloatInt(Matrix2.ofFloatArrayArray(core), new Matrix2.ConvolutionCallback<Float, Float, Integer>() {
            @Override
            public Integer multiple(Float f1, Float f2) {
                return (int) (f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Float> computeFloatFloat(float[][] core) {
        return computeConvolutionFloatFloat(Matrix2.ofFloatArrayArray(core), new Matrix2.ConvolutionCallback<Float, Float, Float>() {
            @Override
            public Float multiple(Float f1, Float f2) {
                return f1 * f2;
            }
        });
    }

    @Override
    public Matrix2<Double> computeFloatDouble(float[][] core) {
        return computeConvolutionFloatDouble(Matrix2.ofFloatArrayArray(core), new Matrix2.ConvolutionCallback<Float, Float, Double>() {
            @Override
            public Double multiple(Float f1, Float f2) {
                return  (double)(f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Integer> computeDoubleInt(double[][] core) {
        return computeConvolutionDoubleInt(Matrix2.ofDoubleArrayArray(core), new Matrix2.ConvolutionCallback<Float, Double, Integer>() {
            @Override
            public Integer multiple(Float f1, Double f2) {
                return  (int)(f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Float> computeDoubleFloat(double[][] core) {
        return computeConvolutionDoubleFloat(Matrix2.ofDoubleArrayArray(core), new Matrix2.ConvolutionCallback<Float, Double, Float>() {
            @Override
            public Float multiple(Float f1, Double f2) {
                return  (float)(f1 * f2);
            }
        });
    }

    @Override
    public Matrix2<Double> computeDoubleDouble(double[][] core) {
        return computeConvolutionDoubleDouble(Matrix2.ofDoubleArrayArray(core), new Matrix2.ConvolutionCallback<Float, Double, Double>() {
            @Override
            public Double multiple(Float f1, Double f2) {
                return (f1 * f2);
            }
        });
    }
}
