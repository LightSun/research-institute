package com.heaven7.advance;

import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.Visitors;

import java.util.ArrayList;
import java.util.List;

/**
 * the matrix2 utils.
 *
 * @author heaven7
 */
public class Matrix2Utils {


    public static final Matrix2.ElementProvider<Integer> INT_0_PROVIDER = new Matrix2.ElementProvider<Integer>() {
        @Override
        public Integer provide(int wIndex, int hIndex, Object param) {
            return 0;
        }
    };
    public static final Matrix2.ElementProvider<Float> FLOAT_0_PROVIDER = new Matrix2.ElementProvider<Float>() {
        @Override
        public Float provide(int wIndex, int hIndex, Object param) {
            return 0f;
        }
    };
    public static final Matrix2.ElementProvider<Double> DOUBLE_0_PROVIDER = new Matrix2.ElementProvider<Double>() {
        @Override
        public Double provide(int wIndex, int hIndex, Object param) {
            return 0d;
        }
    };

    /**
     * merge the two matrix as merge width
     *
     * @param main  the main matrix, which will save the merged result
     * @param other the other matrix
     * @param <T>   the element type
     */
    public static <T> void mergeByRow(Matrix2<T> main, Matrix2<T> other) {
        if (main.getColumnCount() != other.getColumnCount()) {
            throw new IllegalArgumentException("height must be equals.");
        }
        fillRow(main, other.getRowCount(), null, Matrix2.ElementProvider.ofMatrix(other));
    }

    /**
     * merge the two matrix as merge height
     *
     * @param main  the main matrix, which will save the merged result
     * @param other the other matrix
     * @param <T>   the element type
     */
    public static <T> void mergeByColumn(Matrix2<T> main, Matrix2<T> other) {
        if (main.getRowCount() != other.getRowCount()) {
            throw new IllegalArgumentException("width must be equals.");
        }
        fillColumn(main, other.getColumnCount(), null, Matrix2.ElementProvider.ofMatrix(other));
    }

    /**
     * fill the row by target delta row
     *
     * @param src      the src matrix
     * @param delta    the delta width
     * @param param    the param which used for creator
     * @param provider the element provider
     * @param <T>      the type of element.
     */
    public static <T> void fillRow(Matrix2<T> src, int delta, Object param, Matrix2.ElementProvider<T> provider) {
        List<List<T>> rawValues = src.getRawValues();
        int height = src.getColumnCount();
        for (int i = 0; i < delta; i++) {
            List<T> list = new ArrayList<>();
            for (int h = height - 1; h >= 0; h--) {
                list.add(0, provider.provide(i, h, param));
            }
            rawValues.add(list);
        }
    }

    /**
     * fill the column by target delta column
     *
     * @param src      the src matrix
     * @param delta    the delta height
     * @param param    the param which used for creator
     * @param provider the element provider
     * @param <T>      the type of element.
     */
    public static <T> void fillColumn(Matrix2<T> src, int delta, Object param, Matrix2.ElementProvider<T> provider) {
        List<List<T>> rawValues = src.getRawValues();
        final int width = src.getRowCount();
        int originSize;
        for (int i = width - 1; i >= 0; i--) {
            List<T> ts = rawValues.get(i);
            originSize = ts.size();
            for (int h = delta - 1; h >= 0; h--) {
                ts.add(originSize, provider.provide(i, h, param));
            }
        }
    }

    /**
     * drop the delta width for matrix/
     * @param src the src matrix
     * @param delta the delta width
     * @param dropLast true to drop from last
     * @param <T> the element type of matrix
     */
    public static <T> void dropWidth(Matrix2<T> src, int delta, boolean dropLast) {
        List<List<T>> rawValues = src.getRawValues();
        if (delta > rawValues.size()) {
            throw new IllegalArgumentException("row-count is not enough");
        }
        for (int i = delta - 1; i >= 0; i--) {
            rawValues.remove(dropLast ? src.getRowCount() - 1 : 0);
        }
    }
    /**
     * drop the delta height for matrix/
     * @param src the src matrix
     * @param delta the delta height
     * @param dropLast true to drop from last
     * @param <T> the element type of matrix
     */
    public static <T> void dropHeight(Matrix2<T> src, int delta, boolean dropLast) {
        if (delta > src.getColumnCount()) {
            throw new IllegalArgumentException("column-count is not enough");
        }
        for (List<T> list : src.getRawValues()) {
            for (int i = delta - 1; i >= 0; i--) {
                list.remove(dropLast ? list.size() - 1 : 0);
            }
        }
    }

    public static double sumInt(Matrix2<Integer> mat){
        return mat.sum(PileVisitor.INT_ADD);
    }
    public static double sumFloat(Matrix2<Float> mat){
        return mat.sum(PileVisitor.FLOAT_ADD);
    }
    public static double sumDouble(Matrix2<Double> mat){
        return mat.sum(PileVisitor.DOUBLE_ADD);
    }

    /**
     * compute the variance .from int(Integer) to float type.
     *
     * @param mat the matrix
     * @return the float value of variance.
     */
    public static float varIntFloat(Matrix2<Integer> mat) {
        return mat.variance(Matrix2Utils.INT_FLOAT_TRANSFORMER, PileVisitor.FLOAT_ADD,
                Matrix2Utils.FLOAT_AVERAGE, Matrix2Utils.FLOAT_VARIANCE);
    }

    /**
     * compute the variance .from int(Integer) to double type.
     *
     * @param mat the matrix
     * @return the  value of variance.
     */
    public static double varIntDouble(Matrix2<Integer> mat) {
        return mat.variance(Matrix2Utils.INT_DOUBLE_TRANSFORMER, PileVisitor.DOUBLE_ADD,
                Matrix2Utils.DOUBLE_AVERAGE, Matrix2Utils.DOUBLE_VARIANCE);
    }

    /**
     * compute the variance
     *
     * @param mat the matrix
     * @return the float value of variance.
     */
    public static float varFloat(Matrix2<Float> mat) {
        return mat.variance(Visitors.unchangeResultVisitor(), PileVisitor.FLOAT_ADD,
                Matrix2Utils.FLOAT_AVERAGE, Matrix2Utils.FLOAT_VARIANCE);
    }

    /**
     * compute the variance
     *
     * @param mat the matrix
     * @return the int value of variance.
     */
    public static int varInt(Matrix2<Integer> mat) {
        return mat.variance(Visitors.unchangeResultVisitor(), PileVisitor.INT_ADD,
                Matrix2Utils.INT_AVERAGE, Matrix2Utils.INT_VARIANCE);
    }

    /**
     * compute the variance
     *
     * @param mat the matrix
     * @return the double value of variance.
     */
    public static double varDouble(Matrix2<Double> mat) {
        return mat.variance(Visitors.unchangeResultVisitor(), PileVisitor.DOUBLE_ADD,
                Matrix2Utils.DOUBLE_AVERAGE, Matrix2Utils.DOUBLE_VARIANCE);
    }

    public static final Matrix2.AverageCallback<Integer> INT_AVERAGE = new Matrix2.AverageCallback<Integer>() {
        @Override
        public Integer average(Integer total, double count) {
            return (int)(total / count);
        }
    };
    public static final Matrix2.AverageCallback<Float> FLOAT_AVERAGE = new Matrix2.AverageCallback<Float>() {
        @Override
        public Float average(Float total, double count) {
            return (float)(total / count);
        }
    };
    public static final Matrix2.AverageCallback<Double> DOUBLE_AVERAGE = new Matrix2.AverageCallback<Double>() {
        @Override
        public Double average(Double total, double count) {
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
