package com.heaven7.java.image;

import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.util.SparseArray;
import com.heaven7.java.visitor.util.SparseArray2Map;

import java.util.List;

/**
 * the image batch data splitter
 *
 * @param <T> the data type
 * @author heaven7
 */
public interface ImageBatchDataSplitter<T extends ImageBatchDataSplitter.IPositionData> {

    /**
     * split the batch data to multi list. which indicate by multi area.
     *
     * @param count  the actual batch size/count
     * @param width  the width of every image
     * @param height the height of every image
     * @param batch  the batch data
     * @return the splitted batch data
     */
    SparseArray<List<T>> split(int count, int width, int height, List<T> batch);

    class DefaultImageBatchDataSplitter<T extends IPositionData> implements ImageBatchDataSplitter<T> {
        @Override
        public SparseArray<List<T>> split(int count, int width, int height, List<T> batch) {
            Throwables.checkNonPositiveValue(count);
            if (count > 4 || count == 3) {
                throw new IllegalArgumentException();
            }
            SparseArray<List<T>> map = new SparseArray<>();
            //1, 2, 4
            switch (count) {
                case 4: {
                    VisitServices.from(batch).groupService(new ResultVisitor<T, Integer>() {
                        @Override
                        public Integer visit(T t, Object param) {
                            float x = t.getX();
                            float y = t.getY();
                            if(x <= width && y <= height){
                                return 1;
                            }
                            if(x > width && y <= height){
                                return 2;
                            }
                            if(x <= width && y > height){
                                return 3;
                            }
                            if(x > width && y > height){
                                return 4;
                            }
                            throw new RuntimeException(String.format("wrong data. (x, y, width , height) = ( %d, %d %d ,%d )",
                                    x, y, width, height));
                        }
                    }).get().copyTo(new SparseArray2Map<>(map));
                }break;

                case 2:
                    VisitServices.from(batch).groupService(new ResultVisitor<T, Integer>() {
                        @Override
                        public Integer visit(T t, Object param) {
                            float x = t.getX();
                            float y = t.getY();
                            if(x <= width && y <= height){
                                return 1;
                            }
                            if(x > width && y <= height){
                                return 2;
                            }
                            throw new RuntimeException(String.format("wrong data. (x, y, width , height) = ( %d, %d %d ,%d )",
                                    x, y, width, height));
                        }
                    }).get().copyTo(new SparseArray2Map<>(map));
                    break;

                case 1:
                    map.put(1, batch);
                    break;
            }
            return map;
        }
    }

    /**
     * the coordinate data which contains x, y.
     */
    interface IPositionData {
        float getX();
        float getY();
    }
}
