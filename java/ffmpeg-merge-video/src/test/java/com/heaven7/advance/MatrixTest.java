package com.heaven7.advance;

import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.collection.VisitServices;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class MatrixTest {

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
        StringWriter writer = new StringWriter();
        for (int i = 0, size = list.size(); i < size; i++) {
            list.get(i).dump(writer);
            System.out.println(writer.toString());
            writer.getBuffer().delete(0, writer.getBuffer().length());
        }

        int[][] data = new int[][]{
                new int[]{1,2,3,4},
                new int[]{4,5,6,7},
                new int[]{7,8,9,10},
        };
        Matrix2<Integer> mat = Matrix2.ofIntArrayArray(data);
        System.out.println(mat.getRowCount());
        System.out.println(mat.getColumnCount());

        System.out.println(mat.copy()
                .padding(1,2,3,4, Matrix2Utils.INT_0_PROVIDER)
                .toString());
    }
}
