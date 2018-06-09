package com.heaven7.advance;

import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.Visitors;

/**
 * the matrix2 utils.
 * @author heaven7
 */
public class Matrix2Utils {

    /**
     * compute the variance .from int(Integer) to float type.
     * @param mat the matrix
     * @return the float value of variance.
     */
    public static float varIntFloat(Matrix2<Integer> mat){
        return mat.variance(Matrix2Utils.INT_FLOAT_TRANSFORMER, PileVisitor.FLOAT_ADD,
                Matrix2Utils.FLOAT_AVERAGE, Matrix2Utils.FLOAT_VARIANCE);
    }

    /**
     * compute the variance .from int(Integer) to double type.
     * @param mat the matrix
     * @return the  value of variance.
     */
    public static double varIntDouble(Matrix2<Integer> mat){
        return mat.variance(Matrix2Utils.INT_DOUBLE_TRANSFORMER, PileVisitor.DOUBLE_ADD,
                Matrix2Utils.DOUBLE_AVERAGE, Matrix2Utils.DOUBLE_VARIANCE);
    }
    /**
     * compute the variance
     * @param mat the matrix
     * @return the float value of variance.
     */
    public static float varFloat(Matrix2<Float> mat){
        return mat.variance(Visitors.unchangeResultVisitor(), PileVisitor.FLOAT_ADD,
                Matrix2Utils.FLOAT_AVERAGE, Matrix2Utils.FLOAT_VARIANCE);
    }
    /**
     * compute the variance
     * @param mat the matrix
     * @return the int value of variance.
     */
    public static int varInt(Matrix2<Integer> mat){
        return mat.variance(Visitors.unchangeResultVisitor(), PileVisitor.INT_ADD,
                Matrix2Utils.INT_AVERAGE, Matrix2Utils.INT_VARIANCE);
    }
    /**
     * compute the variance
     * @param mat the matrix
     * @return the double value of variance.
     */
    public static double varDouble(Matrix2<Double> mat){
        return mat.variance(Visitors.unchangeResultVisitor(), PileVisitor.DOUBLE_ADD,
                Matrix2Utils.DOUBLE_AVERAGE, Matrix2Utils.DOUBLE_VARIANCE);
    }

    public static final Matrix2.AverageCallback<Integer> INT_AVERAGE = new Matrix2.AverageCallback<Integer>() {
        @Override
        public Integer average(Integer total, int count) {
            return total / count;
        }
    };
    public static final Matrix2.AverageCallback<Float> FLOAT_AVERAGE = new Matrix2.AverageCallback<Float>() {
        @Override
        public Float average(Float total, int count) {
            return total / count;
        }
    };
    public static final Matrix2.AverageCallback<Double> DOUBLE_AVERAGE = new Matrix2.AverageCallback<Double>() {
        @Override
        public Double average(Double total, int count) {
            return total / count;
        }
    };

    public static final PileVisitor<Integer> INT_VARIANCE = new PileVisitor<Integer>() {
        @Override
        public Integer visit(Object o, Integer f1, Integer f2) {
            int val = f1 - f2;
            return val * val;
        }
    };
    public static final PileVisitor<Float> FLOAT_VARIANCE = new PileVisitor<Float>() {
        @Override
        public Float visit(Object o, Float f1, Float f2) {
            float val = f1 - f2;
            return val * val;
        }
    };
    public static final PileVisitor<Double> DOUBLE_VARIANCE = new PileVisitor<Double>() {
        @Override
        public Double visit(Object o, Double f1, Double f2) {
            double val = f1 - f2;
            return val * val;
        }
    };

    public static final ResultVisitor<Integer, Float> INT_FLOAT_TRANSFORMER = new ResultVisitor<Integer, Float>() {
        @Override
        public Float visit(Integer integer, Object param) {
            return integer.floatValue();
        }
    };
    public static final ResultVisitor<Integer, Double> INT_DOUBLE_TRANSFORMER = new ResultVisitor<Integer, Double>() {
        @Override
        public Double visit(Integer integer, Object param) {
            return integer.doubleValue();
        }
    };
}
