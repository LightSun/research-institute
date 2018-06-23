package com.heaven7.advance;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.visitor.FireIndexedVisitor;
import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.collection.VisitServices;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Two-dimensional matrix
 *
 * @param <T> the type of element
 */
public class Matrix2<T> {

    private final List<List<T>> values;

    public Matrix2(List<List<T>> values) {
        this.values = values;
    }

    public static Matrix2<Integer> ofIntArrayArray(int[][] data) {
        List<List<Integer>> list = new ArrayList<>();
        final int w = data.length;
        final int h = data[0].length;
        for (int i = 0; i < w; i++) {
            List<Integer> cols = new ArrayList<>();
            for (int j = 0; j < h; j++) {
                cols.add(data[i][j]);
            }
            list.add(cols);
        }
        return new Matrix2<Integer>(list);
    }

    public static Matrix2<Float> ofFloatArrayArray(float[][] data) {
        List<List<Float>> list = new ArrayList<>();
        final int w = data.length;
        final int h = data[0].length;
        for (int i = 0; i < w; i++) {
            List<Float> cols = new ArrayList<>();
            for (int j = 0; j < h; j++) {
                cols.add(data[i][j]);
            }
            list.add(cols);
        }
        return new Matrix2<Float>(list);
    }

    public static Matrix2<Double> ofDoubleArrayArray(double[][] data) {
        List<List<Double>> list = new ArrayList<>();
        final int w = data.length;
        final int h = data[0].length;
        for (int i = 0; i < w; i++) {
            List<Double> cols = new ArrayList<>();
            for (int j = 0; j < h; j++) {
                cols.add(data[i][j]);
            }
            list.add(cols);
        }
        return new Matrix2<Double>(list);
    }

    public static Matrix2<Integer> ofIntArray(int w, int h, int[] arr) {
        if (arr.length != w * h) {
            throw new IllegalArgumentException();
        }
        List<List<Integer>> list = new ArrayList<>();
        for (int i = 0; i < h; i++) {
            List<Integer> cols = new ArrayList<>();
            for (int j = 0; j < w; j++) {
                cols.add(arr[i * w + j]);
            }
            list.add(cols);
        }
        return new Matrix2<>(list);
    }

    public static Matrix2<Float> ofFloatArray(int w, int h, float[] arr) {
        if (arr.length != w * h) {
            throw new IllegalArgumentException();
        }
        List<List<Float>> list = new ArrayList<>();
        for (int i = 0; i < h; i++) {
            List<Float> cols = new ArrayList<>();
            for (int j = 0; j < w; j++) {
                cols.add(arr[i * w + j]);
            }
            list.add(cols);
        }
        return new Matrix2<>(list);
    }

    public static Matrix2<Double> ofDoubleArray(int w, int h, double[] arr) {
        if (arr.length != w * h) {
            throw new IllegalArgumentException();
        }
        List<List<Double>> list = new ArrayList<>();
        for (int i = 0; i < h; i++) {
            List<Double> cols = new ArrayList<>();
            for (int j = 0; j < w; j++) {
                cols.add(arr[i * w + j]);
            }
            list.add(cols);
        }
        return new Matrix2<>(list);
    }

    public static <T> Matrix2<T> ofObjectArray(int w, int h, T[] arr) {
        if (arr.length != w * h) {
            throw new IllegalArgumentException();
        }
        List<List<T>> list = new ArrayList<>();
        for (int i = 0; i < h; i++) {
            List<T> cols = new ArrayList<>();
            for (int j = 0; j < w; j++) {
                cols.add(arr[i * w + j]);
            }
            list.add(cols);
        }
        return new Matrix2<>(list);
    }

    public List<List<T>> getRawValues() {
        return values;
    }

    //row
    public int getWidth() {
        return values.size();
    }

    //col
    public int getHeight() {
        return values.isEmpty() ? 0 : values.get(0).size();
    }

    public int getTotalSize() {
        return getWidth() * getHeight();
    }

    //================================================================================================================

    public void padding(int left, int top, int right, int bottom, Matrix2Utils.ElementProvider<T> provider){
        //TODO IMPL
        //left and right make add columns, top and bottom make add rows
        if(left > 0){
            int w = getWidth();
           // int h = getHeight();
            int size;
            for (int i = 0; i < w; i++) {
                List<T> ts = values.get(i);
                for(int j = 0 ; j < left ; j ++){
                    ts.add(0, provider.provide(i, j, null));
                }
                if(right > 0){
                    size = ts.size();
                    for(int j = 0 ; j < right ; j ++){
                        ts.add(provider.provide(i, size + j, null));
                    }
                }
            }
        }else {
            if (right > 0) {
                int w = getWidth();
                int size;
                for (int i = 0; i < w; i++) {
                    List<T> ts = values.get(i);
                    size = ts.size();
                    for (int j = 0; j < right; j++) {
                        ts.add(provider.provide(i, size + j, null));
                    }
                }
            }
        }

        if(top > 0){
            int h = getHeight();
            for(int t = 0 ; t < top ; t ++){
                for(int  i = 0 ; i < h ; i ++){
                    //TODO
                }
            }
        }
    }

    /**
     * compute the convolution. this should called after call fill.
     *
     * @param core     the core matrix of convolution
     * @param coreSum  the sum of core convolution
     * @param outW     the out width of matrix
     * @param outH     the out height of matrix
     * @param strideX  the stride x of row
     * @param strideY  the  stride y of column
     * @param callback the convolution callback
     * @param sum      the sum visitor of result type
     * @param average  the  average callback of result type
     * @param provider the provider of fill gap.
     * @param <C>      the type of core convolution
     * @param <R>      the result type of convolution
     * @return the result of convolution
     */
    public <C, R> Matrix2<R> computeConvolution(Matrix2<C> core, double coreSum, int outW, int outH,
                                                int strideX, int strideY,
                                                ConvolutionCallback<T, C, R> callback, PileVisitor<R> sum,
                                                AverageCallback<R> average, Matrix2Utils.ElementProvider<R> provider) {
        List<List<R>> results = new ArrayList<>();
        for (int i = outW - 1; i >= 0; i--) {
            results.add(new ArrayList<>());
        }
        int w_core = core.getWidth();
        int h_core = core.getHeight();
        int w = getWidth();
        int h = getHeight();

        int wIndex = 0;
        //int hIndex = 0;
        int lastWidthIndex = 0, lastHeightIndex = 0;
        while (true) {
            R total = null;
            for (int i = 0; i < w_core; i++) {
                for (int i2 = 0; i2 < h_core; i2++) {
                    T cur = values.get(i + lastWidthIndex).get(i2 + lastHeightIndex);
                    C factor = core.getRawValues().get(i).get(i2);
                    R result = callback.multiple(cur, factor);
                    if (total == null) {
                        total = result;
                    } else {
                        total = sum.visit(null, total, result);
                    }
                }
            }
            if (coreSum != 0 && coreSum != 1) {
                total = average.average(total, coreSum);
            }
            results.get(wIndex).add(total);
            lastHeightIndex += strideY;
            if (lastHeightIndex >= h - strideY) {
                lastHeightIndex = 0;
                lastWidthIndex += strideX;
                if (lastWidthIndex > w - strideX) {
                    break;
                }
                wIndex++;
            }
        }
        //Filling in the gaps
        VisitServices.from(results).fireWithIndex(new FireIndexedVisitor<List<R>>() {
            @Override
            public Void visit(Object param, List<R> list, int index, int size) {
                if (list.size() < outH) {
                    int count = outH - list.size();
                    for (int i = 0; i < count; i++) {
                        list.add(provider.provide(index, i, null));
                    }
                }
                return null;
            }
        });
        return new Matrix2<>(results);
    }

    public T sum(final PileVisitor<T> pileVisitor) {
        return sum(Visitors.unchangeResultVisitor(), pileVisitor);
    }

    public <R> R sum(ResultVisitor<T, R> transformer, final PileVisitor<R> pileVisitor) {
        return VisitServices.from(values).pile(null, new ResultVisitor<List<T>, R>() {
            @Override
            public R visit(List<T> ts, Object param) {
                return VisitServices.from(ts).map(transformer).pile(pileVisitor);
            }
        }, pileVisitor);
    }

    /**
     * divide chunk by target width and height.
     */
    public List<Matrix2<T>> divideChunk(int width, int height) {
        List<Matrix2<T>> result = new ArrayList<>();

        final int wSize = values.size();
        final int hSize = values.get(0).size();
        int lastWidthIndex = 0, lastHeightIndex = 0;

        while (lastWidthIndex < wSize && lastHeightIndex < hSize) {
            int size1 = Math.min(width, wSize - lastWidthIndex);
            int size2 = Math.min(height, hSize - lastHeightIndex);
            List<List<T>> list = new ArrayList<>();
            for (int i = 0; i < size1; i++) {
                List<T> cols = values.get(i + lastWidthIndex);
                List<T> tmp = new ArrayList<>();
                for (int i2 = 0; i2 < size2; i2++) {
                    T t = cols.get(i2 + lastHeightIndex);
                    tmp.add(t);
                }
                list.add(tmp);
            }
            result.add(new Matrix2<>(list));

            lastWidthIndex += size1;
            //hor：done, start vertical
            if (lastWidthIndex >= wSize) {
                lastWidthIndex = 0;
                lastHeightIndex += size2;
            }
        }
        return result;
    }

    /**
     * compute the variance.
     *
     * @param transformer the transformer
     * @param sum         the sum visitor
     * @param average     the average callback
     * @param variance    the variance visitor
     * @param <R>         the result type.
     * @return the variance value.
     */
    public <R> R variance(ResultVisitor<T, R> transformer, PileVisitor<R> sum,
                          AverageCallback<R> average, PileVisitor<R> variance) {
        return variance(null, transformer, sum, average, variance);
    }

    /**
     * compute the variance.
     *
     * @param param       the extra parameter
     * @param transformer the transformer
     * @param sum         the sum visitor
     * @param average     the average callback
     * @param variance    the variance visitor
     * @param <R>         the result type.
     * @return the variance value.
     */
    public <R> R variance(@Nullable Object param, ResultVisitor<T, R> transformer, PileVisitor<R> sum,
                          AverageCallback<R> average, PileVisitor<R> variance) {
        final int w = getWidth();
        final int h = getHeight();
        R averageVal = average.average(sum(transformer, sum), w * h);

        R total = null;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                R result = variance.visit(param, averageVal,
                        transformer.visit(values.get(j).get(i), param));
                if (total != null) {
                    total = sum.visit(param, total, result);
                } else {
                    total = result;
                }
            }
        }
        return average.average(total, w * h);
    }

    /**
     * AT
     */
    public Matrix2<T> transpose() {
        List<List<T>> list = new ArrayList<>();
        int w = getWidth();
        int h = getHeight();
        for (int i = 0; i < h; i++) {
            List<T> cols = new ArrayList<>();
            for (int j = 0; j < w; j++) {
                cols.add(values.get(j).get(i));
            }
            list.add(cols);
        }
        return new Matrix2<>(list);
    }

    public Matrix2<T> turnLeftRight() {
        ArrayList<List<T>> lists = new ArrayList<>(values);
        Collections.reverse(lists);
        return new Matrix2<>(lists);
    }

    public Matrix2<T> turnTopBottom() {
        ArrayList<List<T>> lists = new ArrayList<>();
        for (List<T> list : values) {
            ArrayList<T> list1 = new ArrayList<>(list);
            Collections.reverse(list1);
            lists.add(list1);
        }
        return new Matrix2<>(lists);
    }

    public Matrix2<T> turnRight90() {
        return ofObjectArray(getWidth(), getHeight(), toArray(null));
    }

    /**
     * rotate the matrix2 clockwise.
     *
     * @param degree the degree to rotate
     * @return the result matrix2
     */
    public Matrix2<T> rotateClockwise(int degree) {
        if (degree % 90 != 0) {
            throw new IllegalArgumentException();
        }
        degree %= 360;
        if (degree < 0) {
            degree += 360;
        }
        if (degree == 0) {
            return this;
        }
        switch (degree) {
            case 90:
                return turnRight90().turnLeftRight();

            case 180:
                return turnTopBottom().turnLeftRight();

            case 270:
                return turnRight90().turnTopBottom();
        }
        throw new IllegalStateException("can't reach here");
    }

    public String toString() {
        StringWriter sw = new StringWriter();
        dump(sw);
        return sw.toString();
    }

    public void dump(Writer writer) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        final int wSize = values.size();
        // final int hSize = values.get(0).size();
        for (int i = 0; i < wSize; i++) {
            List<T> cols = values.get(i);
            sb.append("Row ").append(i).append(": ").append(cols.toString()).append("\r\n");
        }
        sb.append("]\n");
        try {
            writer.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public T[] toArray() {
        return toArray(null);
    }

    /**
     * make Two-dimensional matrix to One-dimensional matrix.
     * for usage. see {@linkplain #turnRight90()}.
     *
     * @param out the out array . can be null
     * @return the One-dimensional matrix.
     */
    @SuppressWarnings("unchecked")
    public T[] toArray(@Nullable T[] out) {
        if (getWidth() == 0 || getHeight() == 0) {
            return null;
        }
        int w = getWidth();
        int h = getHeight();
        if (out == null) {
            out = (T[]) Array.newInstance(values.get(0).get(0).getClass(), w * h);
        }
        //Equivalent to right rotation 90
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                out[i * w + j] = values.get(j).get(i);
            }
        }
        return out;
    }

    /**
     * copy the matrix to new matrix.
     *
     * @return the matrix
     */
    public Matrix2<T> copy() {
        ArrayList<List<T>> lists = new ArrayList<>();
        for (List<T> list : values) {
            lists.add(new ArrayList<>(list));
        }
        return new Matrix2<>(lists);
    }

    public interface AverageCallback<T> {
        /**
         * compute the average
         *
         * @param total the total value
         * @param count the count to average
         * @return the average result
         */
        T average(T total, double count);
    }

    /**
     * the convolution callback
     *
     * @param <T> the element type of current matrix
     * @param <C> the type of convolution core
     * @param <R> the result type matrix
     */
    public interface ConvolutionCallback<T, C, R> {
        /**
         * compute the convolution .
         *
         * @param t the element from current matrix
         * @param c the element from convolution core
         * @return the result
         */
        R multiple(T t, C c);
    }

}
