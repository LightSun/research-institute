package com.heaven7.advance;

import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.collection.VisitServices;

import java.util.ArrayList;
import java.util.List;

/**
 * 2维矩阵
 * @param <T>
 */
public class Matrix2<T> {

    private final List<List<T>> values;

    public Matrix2(List<List<T>> values) {
        this.values = values;
    }

    public int getWidth(){
        return values.size();
    }
    public int getHeight(){
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

    /** divide chunk by target width and height. */
    public List<Matrix2<T>> divideChunk(int width, int height) {
        List<Matrix2<T>> result = new ArrayList<>();

        final int wSize = values.size();
        final int hSize = values.get(0).size();
        int lastWidthIndex = 0, lastHeightIndex = 0;

        while (lastWidthIndex < wSize && lastHeightIndex < hSize){
            int size1 = Math.min(width, wSize - lastWidthIndex);
            int size2 = Math.min(height, hSize - lastHeightIndex);
            List<List<T>> list = new ArrayList<>();
            for(int i = 0 ; i < size1 ; i ++){
                List<T> cols = values.get(i + lastWidthIndex);
                List<T> tmp = new ArrayList<>();
                for(int i2 = 0 ; i2 < size2 ; i2 ++){
                    T t = cols.get(i2 + lastHeightIndex);
                    tmp.add(t);
                }
                list.add(tmp);
            }
            result.add(new Matrix2<>(list));

            lastWidthIndex += size1;
            //hor：done, start vertical
            if(lastWidthIndex >= wSize){
                lastWidthIndex = 0;
                lastHeightIndex += size2;
            }
        }
        return result;
    }

    public void dump() {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        final int wSize = values.size();
        final int hSize = values.get(0).size();
        for(int i = 0 ; i < wSize ; i ++){
            List<T> cols = values.get(i);
            List<T> tmp = new ArrayList<>();
            for(int i2 = 0 ; i2 < hSize ; i2 ++){
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
        for (int i = 0 ; i < 9 ; i ++){
            List<Integer> cols = new ArrayList<>();
            for (int i2 = 0 ; i2 < 9 ; i2 ++){
                cols.add(i2);
            }
            result.add(cols);
            Integer sum = VisitServices.from(cols).pile(new PileVisitor<Integer>() {
                @Override
                public Integer visit(Object o, Integer integer, Integer integer2) {
                    return integer + integer2;
                }
            });
            System.out.println("raw " + i  + ", mean = " + sum / cols.size());
        }
        Matrix2<Integer> matrix = new Matrix2<>(result);

        Integer sum = matrix.sum(new PileVisitor<Integer>() {
            @Override
            public Integer visit(Object o, Integer val1, Integer val2) {
                return val1 + val2;
            }
        });
        List<Matrix2<Integer>> list = matrix.divideChunk(3, 3);
        if(list.size() != 9){
            throw new IllegalStateException();
        }

        for (int i = 0 , size = list.size() ; i < size ; i ++){
            list.get(i).dump();
        }
    }

}
