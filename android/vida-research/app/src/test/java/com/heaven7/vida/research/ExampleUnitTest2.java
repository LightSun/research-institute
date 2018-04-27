package com.heaven7.vida.research;

import com.heaven7.vida.research.utils.DiscList;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest2 {

    private final DiscList<DiscList.BaseDiscItem> mDiscList = new DiscList<>();

    public ExampleUnitTest2() {
        setUp();
    }

    private void setUp() {
        List<DiscList.BaseDiscItem> list = new ArrayList<>();
        final float min = 10;
        for(int i = 0 ; i < 10 ; i ++){
            final int index = i;
            list.add(new DiscList.BaseDiscItem() {
                @Override
                public float getDegree() {
                    return  min + index * 10;
                }
            });
        }
        mDiscList.setItems(list);
        logItems();
        //no rotated , no set visible angles(default 0 - 180).
        // 0 - 180  ---- 0 - 360
        //10, 20, 30, 40, 50 , 60 , 70 ,80
    }

    @Test
    public void logItems() {
        logItems("init");
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

    @Test
    public void testRotate(){
        mDiscList.rotate(30, true);
        logItems();
    }

    @Test
    public void testRotate2(){
        mDiscList.rotateTo(90);
        logItems();
    }

    @Test
    public void testVisibleAngle(){
        mDiscList.setVisibleAngles(10, 170);
        logItems();
        mDiscList.rotate(30, true);
        logItems();

        mDiscList.rotate(60, false);
        logItems();
    }

    @Test //正向旋转
    public void testRotateMulti(){
        // i = 19,  bugs
        for (int i = 0 ; i < 20;  i++) {
            mDiscList.rotate(45, true);
            logItems("i = " + i);
        }
    }
    @Test //反向旋转
    public void testRotateMulti2(){
        for (int i = 0 ; i < 20;  i++) {
            mDiscList.rotate(45, false);
            logItems("i = " + i);
        }
    }

}