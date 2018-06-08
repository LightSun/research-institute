package com.heaven7.advance;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.collection.VisitServices;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 2维矩阵
 *
 * @param <T>
 */
public class Matrix2<T> {

    private final List<List<T>> values;

    public Matrix2(List<List<T>> values) {
        this.values = values;
    }

    public static Matrix2<Integer> ofInt(int[][] data) {
        List<List<Integer>> list = new ArrayList<>();
        int w = data.length;
        int h = data[0].length;
        for (int i = 0; i < w; i++) {
            List<Integer> cols = new ArrayList<>();
            for (int j = 0; j < h; j++) {
                cols.add(data[i][j]);
            }
            list.add(cols);
        }
        return new Matrix2<Integer>(list);
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

    public static Matrix2<Double> ofFloatArray(int w, int h, double[] arr) {
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

    public static <T> Matrix2<T> ofArray(int w, int h, T[] arr) {
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
        return Collections.unmodifiableList(values);
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

    @SuppressWarnings("unchecked")
    public T[] toArray(@Nullable T[] out) {
        if (getWidth() == 0 || getHeight() == 0) {
            return null;
        }
        int w = getWidth();
        int h = getHeight();
        if (out == null) {
            out = (T[]) Array.newInstance(getRawValues().get(0).get(0).getClass(), w * h);
        }
        //相当于右旋转90.
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                out[i * w + j] = values.get(j).get(i);
            }
        }
        return out;
    }

    public void dump() {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        final int wSize = values.size();
        final int hSize = values.get(0).size();
        for (int i = 0; i < wSize; i++) {
            List<T> cols = values.get(i);
            List<T> tmp = new ArrayList<>();
            for (int i2 = 0; i2 < hSize; i2++) {
                T t = cols.get(i2);
                tmp.add(t);
            }
            sb.append("Raw ").append(i).append(": ").append(tmp.toString()).append("\r\n");
        }
        sb.append("]\n");

        System.out.println(sb.toString());
    }

    //test
    public static void main(String[] args) {
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            List<Integer> cols = new ArrayList<>();
            for (int i2 = 0; i2 < 9; i2++) {
                cols.add(i2);
            }
            result.add(cols);
            Integer sum = VisitServices.from(cols).pile(new PileVisitor<Integer>() {
                @Override
                public Integer visit(Object o, Integer integer, Integer integer2) {
                    return integer + integer2;
                }
            });
            System.out.println("raw " + i + ", mean = " + sum / cols.size());
        }
        Matrix2<Integer> matrix = new Matrix2<>(result);

        Integer sum = matrix.sum(new PileVisitor<Integer>() {
            @Override
            public Integer visit(Object o, Integer val1, Integer val2) {
                return val1 + val2;
            }
        });
        List<Matrix2<Integer>> list = matrix.divideChunk(3, 3);
        if (list.size() != 9) {
            throw new IllegalStateException();
        }

        for (int i = 0, size = list.size(); i < size; i++) {
            list.get(i).dump();
        }
    }

}
