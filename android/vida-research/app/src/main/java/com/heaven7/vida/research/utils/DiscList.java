package com.heaven7.vida.research.utils;

import com.heaven7.core.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * the disc list. help we manage disc. and the disc is from left . that means the angle of left side is 0.
 * the angle of right side is 180. Here is a simple demo:
 * <pre><code>
 *       private final DiscList<DiscList.BaseDiscItem> mDiscList = new DiscList<>();
 *       private void setUp() {
              List<DiscList.BaseDiscItem> list = new ArrayList<>();
              final float min = 10;
              for(int i = 0 ; i < 10 ; i ++){
                     final int index = i;
                     list.add(new DiscList.BaseDiscItem() {
                         {@literal @} Override
                        public float getDegree() {
                            return  min + index * 10;
                        }
                     });
              }
              mDiscList.setItems(list);
              logItems("init");
         }

         {@literal @}Test
         public void testRotateMulti2(){
             for (int i = 0 ; i < 20;  i++) {
                 mDiscList.rotate(45, false);
                 logItems("i = " + i);
             }
         }
         public void logItems(String tag) {
             System.out.println("=========== start ["+ tag +"] ============");
             List<DiscList.BaseDiscItem> items = mDiscList.getLayoutItems(null);
             for (DiscList.BaseDiscItem item : items){
                  System.out.println(item.getDegree() + " ,reverse = " + item.isReverse());
             }
             System.out.println("drawAngle = " + mDiscList.getStartDrawAngle()
                        + " ,rotate angle = " + mDiscList.getRotatedDegree());
             System.out.println("=========== end ["+ tag +"] ============");
             System.out.println();
         }
 *
 * </code></pre>
 * Created by heaven7 on 2018/4/25 0025.
 */

public class DiscList<E extends DiscList.DiscItem> {

   // private static final String TAG = "DiscList";
    private final ArrayList<E> mItems = new ArrayList<>();
    private final ArrayList<E> mLayoutItems = new ArrayList<>();
    /** for some case .we may allow overlap some angle. */
    private static final int DEFAULT_OVERLAP_DEGREE = 0;

    private float mStartVisibleAngle = 0f;
    private float mEndVisibleAngle = 360f;
    private float mRotatedDegree = 0;
    private float mStartDrawAngle;
    /** for some case .we may allow overlap some angle. */
    private float mOverlapDegree = DEFAULT_OVERLAP_DEGREE;

    /**
     * get the layout items. which should often called after {@linkplain #layout()}.
     *
     * @param out the out items.
     * @return the layout items
     */
    public List<E> getLayoutItems(List<E> out) {
        if (out == null) {
            out = new ArrayList<>();
        }
        out.addAll(mLayoutItems);
        return out;
    }

    /**
     * set the visible angles. which will effect layout.
     *
     * @param start the start visible angle
     * @param end   the end visible angle.
     */
    public void setVisibleAngles(float start, float end) {
        if (end <= start) {
            throw new IllegalArgumentException(" end must > start.");
        }
        if(end - start > 360f){
            throw new IllegalArgumentException();
        }
        if (end < 0) {
            do {
                start += 360f;
                end += 360f;
            }while (end < 0);
        } else if (start > 360f) {
            do {
                start -= 360f;
                end -= 360f;
            } while (start > 360f);
        }
        if(mStartVisibleAngle == start && mEndVisibleAngle == end){
            return;
        }
        this.mStartVisibleAngle = start;
        this.mEndVisibleAngle = end;
        if(layout()){
            rotateTo(mRotatedDegree);
        }
    }

    /**
     * rotate the disc with target degree. degree must > 0.
     *
     * @param degree    the degree . > 0
     * @param clockwise true if it is clockwise. false otherwise.
     */
    public void rotate(float degree, boolean clockwise) {
        if (degree == 0) {
            return;
        }
        if(degree < 0){
            throw new IllegalArgumentException("degree must > 0");
        }
        if (clockwise) {
            this.mRotatedDegree += degree;
            //for clock wise . check tail
            if (!mLayoutItems.isEmpty()) {
                //rotate
                rotateLayoutItems(degree);
                for (int size = mLayoutItems.size(), i = size - 1; i >= 0; i--) {
                    E e = mLayoutItems.get(i);
                    //end position is over start angle.
                    if (shouldDelete(e)) {
                        mLayoutItems.remove(i);
                        onRemoveLayoutItem(e);
                    } else {
                        break;
                    }
                }
                //inert head
                insertHeadItems(mLayoutItems.get(0));

                //delete excess item ( > 360).
                float totalAngle = 0;
                for (int size = mLayoutItems.size(),i = 0; i < size; i++) {
                    totalAngle += mLayoutItems.get(i).getDegree();
                    if(totalAngle > 360 + mOverlapDegree){
                        removeLayoutItemsAfter(i);
                        break;
                    }
                }
            }
        } else {
            this.mRotatedDegree -= degree;
            //for !clock wise . check head
            if (!mLayoutItems.isEmpty()) {
                //rotate
                rotateLayoutItems(-degree);
                for (int size = mLayoutItems.size(), i = 0; i < size; i++) {
                    E e = mLayoutItems.get(i);
                    //end position is over start angle.
                    if(shouldDelete(e)){
                        mLayoutItems.remove(i);
                        onRemoveLayoutItem(e);
                        size --;
                        i--;
                    } else {
                        break;
                    }
                }
                //insert tail.
                insertTailItems(mLayoutItems.get(0), mLayoutItems.get(mLayoutItems.size() - 1));

                //delete excess item ( > 360).
                float totalAngle = 0;
                for (int size = mLayoutItems.size(),i = size - 1; i >=0 ; i--) {
                    totalAngle += mLayoutItems.get(i).getDegree();
                    if(totalAngle > 360 + mOverlapDegree){
                        removeLayoutItemsBefore(i);
                        break;
                    }
                }
            }
        }
        mStartDrawAngle = mLayoutItems.get(0).getStartAngle();
        logItems("rotate");
    }

    public void logItems(String tag) {
        System.out.println("=========== start ["+ tag +"] ============");
        List<E> items = getLayoutItems(null);
        for (E item : items){
            System.out.println(item.getDegree() + " ,reverse = " + item.isReverse());
        }
        System.out.println("drawAngle = " + getStartDrawAngle()
                + " ,rotate angle = " + getRotatedDegree());
        System.out.println("=========== end ["+ tag +"] ============");
        System.out.println();
    }

    /**
     * rotate to target degree.
     *
     * @param degree the degree
     */
    public void rotateTo(float degree) { // -100 . 10
        float delta = degree - mRotatedDegree;
        if(delta == 0){
            return;
        }
        rotate(Math.abs(delta), delta > 0);
    }

    /**
     * set the items which is used to layout .
     *
     * @param items the items to layout.
     */
    public void setItems(List<E> items) {
        mItems.clear();
        mItems.addAll(items);
        setIndexes();
        layout();
        if(mRotatedDegree != 0){
            rotateTo(mRotatedDegree);
        }
    }

    /**
     * get overlap degree. in disc, in some case, we may allow overlap some degree.
     * @return the overlap degree.
     */
    public float getOverlapDegree() {
        return mOverlapDegree;
    }

    /**
     * set overlap degree. in disc, in some case, we may allow overlap some degree.
     * <p>Note ,if you want overlap degree. you must call this before call any of other methods. like {@linkplain #setItems(List)} .</p>
     * @param overlapDegree the overlap degree
     */
    public void setOverlapDegree(float overlapDegree) {
        this.mOverlapDegree = overlapDegree;
    }
    /**
     * get the start draw angle. which should often called after {@linkplain #layout()}
     *
     * @return the start angle of draw.
     */
    public float getStartDrawAngle() {
        return mStartDrawAngle;
    }

    /**
     * get the start visible angle
     * @return the start visible angle
     */
    public float getStartVisibleAngle() {
        return mStartVisibleAngle;
    }

    /**
     * get the end visible angle
     * @return the end visible angle
     */
    public float getEndVisibleAngle() {
        return mEndVisibleAngle;
    }
    /**
     * get the rotated degree
     * @return the the rotated degree
     */
    public float getRotatedDegree() {
        return mRotatedDegree;
    }

    /**
     * get the next item by anchor.
     *
     * @param anchor the anchor
     * @return the next item
     */
    public E next(E anchor) {
        int index = anchor.getIndex();
        if (index == mItems.size() - 1) {
            index = 0;
        } else {
            index++;
        }
        return mItems.get(index);
    }

    /**
     * get the previous item by anchor.
     *
     * @param anchor the anchor
     * @return the previous item
     */
    public E previous(E anchor) {
        int index = anchor.getIndex();
        if (index == 0) {
            index = mItems.size() - 1;
        } else {
            index--;
        }
        return mItems.get(index);
    }

    /**
     * resort the items
     *
     * @param delegate the sort delegate
     * @return false if no items. true otherwise.
     */
    public boolean sort(SortDelegate<E> delegate) {
        if (delegate == null) {
            throw new NullPointerException();
        }
        if(mItems.isEmpty()){
            return false;
        }
        delegate.sort(mItems);
        setIndexes();
        layout();
        rotateTo(mRotatedDegree);
        return true;
    }

    /** do layout items */
    private boolean layout() {
        if(mItems.isEmpty()){
            return false;
        }
        clearLayoutItems();
        float start = mStartVisibleAngle;
        float temp = mStartVisibleAngle;
        E last = mItems.get(0);
        temp += last.getDegree();
        //angle must below 360.
        addLayoutItem(last, false);
        last.setStartAngle(start);
        last.setEndAngle(temp);
        start = temp;
        for (;;){
            E item = next(last);
            temp += item.getDegree();
            if(start < mEndVisibleAngle){
                addLayoutItem(item, false);
                item.setStartAngle(start);
                item.setEndAngle(temp);
                start = temp;
                last = item;
            }else{
                break;
            }
        }
        mStartDrawAngle = mLayoutItems.get(0).getStartAngle();
        return true;
    }

    /** remove all start items that until target index. */
    private void removeLayoutItemsBefore(int index) {
        do {
            E item = mLayoutItems.remove(index);
            onRemoveLayoutItem(item);
            index--;
        }while (index >= 0);
    }

    /** remove all start items that from target index. */
    private void removeLayoutItemsAfter(int index) {
        int size;
        do{
            E item = mLayoutItems.remove(index);
            onRemoveLayoutItem(item);
            size = mLayoutItems.size();
        }while (index < size);
    }

    private void clearLayoutItems() {
        for (E e : mLayoutItems) {
            e.setHold(false);
            e.setReverse(false);
        }
        mLayoutItems.clear();
    }

    private void onRemoveLayoutItem(E e) {
        e.setReverse(false);
        e.setHold(false);
    }

    private void rotateLayoutItems(float degree) {
        for (E e : mLayoutItems) {
            e.setStartAngle(e.getStartAngle() + degree);
            e.setEndAngle(e.getEndAngle() + degree);
        }
    }

    //degree > 0
    private void insertTailItems(E head, E tail) {
        E next = next(tail);
        if (next.isHold()) {
            return;
        }
        float[] angles = adjustAngles(tail, null);
        float endAngle = tail.getEndAngle();
        float nextDegree = next.getDegree();
        if (angles[1] < mEndVisibleAngle && (endAngle + nextDegree ) <= head.getStartAngle() + 360f) {
            next.setStartAngle(endAngle);
            next.setEndAngle(endAngle + nextDegree);
            addLayoutItem(next, false);
            insertTailItems(head, next);
        }
    }

    private void insertHeadItems(E head) {
        float startAngle = adjustAngles(head, null)[0];
        if (startAngle > mStartVisibleAngle) {
            //will insert new item.
            E previous = previous(head);
            if (previous.isHold()) {
                return;
            }
            previous.setStartAngle(head.getStartAngle() - previous.getDegree());
            previous.setEndAngle(head.getStartAngle());
            addLayoutItem(previous, true);
            insertHeadItems(previous);
        }
    }

    //Normalized: to -360 ~ 360.
    private boolean shouldDelete(E e) {
        //start angle > end angle
        float startAngle = e.getStartAngle();
        float endAngle = e.getEndAngle();

        if(startAngle >= 360){
            int count = Math.min((int)startAngle / 360, (int)endAngle/ 360);
            float over = count * 360;
            startAngle -= over;
            endAngle -= over;
        }else if( endAngle <= -360){
            int count = Math.min((int)startAngle / 360, (int)endAngle/ 360);
            float over = count * 360;
            startAngle += over;
            endAngle += over;
        }
        //start angle > max .  or end angle  < min
        return startAngle >= mEndVisibleAngle || endAngle <= mStartVisibleAngle;
    }

    private void addLayoutItem(E e, boolean head) {
        if (head) {
            mLayoutItems.add(0, e);
        } else {
            mLayoutItems.add(e);
        }
        //head is often used by reverse.
        e.setReverse(head);
        e.setHold(true);
    }

    private void setIndexes() {
        for (int i = 0, size = mItems.size(); i < size; i++) {
            mItems.get(i).setIndex(i);
        }
    }

    /** adjust angles to (-360~360) */
    private static float[] adjustAngles(DiscItem e, float[] out) {
        if(out == null){
            out = new float[2];
        }
        float startAngle = e.getStartAngle();
        float endAngle = e.getEndAngle();

        if(startAngle >= 360){
            int count = Math.min((int)startAngle / 360, (int)endAngle/ 360);
            float over = count * 360;
            startAngle -= over;
            endAngle -= over;
        }else if( endAngle <= -360){
            int count = Math.min((int)startAngle / 360, (int)endAngle/ 360);
            float over = count * 360;
            startAngle += over;
            endAngle += over;
        }
        out[0] = startAngle;
        out[1] = endAngle;
        return out;
    }
    /**
     * the disc item.
     * @author heaven7
     */

    public interface DiscItem {
        void setIndex(int index);

        int getIndex();

        float getDegree();

        void setHold(boolean hold);

        boolean isHold();

        /**
         * indicate is reverse item or not
         */
        boolean isReverse();

        void setReverse(boolean reverse);

        void setStartAngle(float previous);

        void setEndAngle(float tempDegree);

        float getStartAngle();

        float getEndAngle();

        String getLogText();
    }

    /**
     * the sort delegate
     * @param <E>
     * @author heaven7
     */
    public interface SortDelegate<E extends DiscItem> {
        void sort(List<E> items);
    }


    /**
     * the base disc item
     * @author heaven7
     */
    public static abstract class BaseDiscItem implements DiscList.DiscItem {
        private int index;
        private boolean hold;
        private boolean reverse;
        private float endAngle;
        private float startAngle;

        @Override
        public void setIndex(int index) {
            this.index = index;
        }
        @Override
        public int getIndex() {
            return index;
        }
        @Override
        public void setHold(boolean hold) {
            this.hold = hold;
        }
        @Override
        public boolean isHold() {
            return hold;
        }

        @Override
        public boolean isReverse() {
            return reverse;
        }

        @Override
        public void setReverse(boolean reverse) {
            this.reverse = reverse;
        }

        @Override
        public void setEndAngle(float angle) {
            this.endAngle = angle;
        }

        @Override
        public void setStartAngle(float angle) {
            this.startAngle = angle;
        }

        @Override
        public float getStartAngle() {
            return startAngle;
        }

        @Override
        public float getEndAngle() {
            return endAngle;
        }

        @Override
        public String getLogText() {
            return toString();
        }

        @Override
        public String toString() {
            return getDegree() + " , start = " + getStartAngle() + " ,end = " + getEndAngle();
        }
    }

}
