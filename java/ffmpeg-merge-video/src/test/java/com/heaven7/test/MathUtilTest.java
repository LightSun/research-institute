package com.heaven7.test;

import static com.heaven7.ve.utils.MathUtil.log2n;
import static com.heaven7.ve.utils.MathUtil.max2K;

/**
 * @author heaven7
 */
public class MathUtilTest {
    public static void main(String[] args) {
        System.out.println(Math.log(Math.E));// 1
        System.out.println(Math.log(5) / Math.log(2)); // 2
        System.out.println(max2K(5)); // 4
        System.out.println(max2K(1)); //1
        System.out.println(max2K(Integer.MAX_VALUE)); // 1073741824
        //System.out.println(max2K(-1)); //1
        int curFlags = 15;
        int maxKey;
        for (; curFlags > 0;) {
            maxKey = max2K(curFlags);
            System.out.println("log2 log2n(maxKey) = " + log2n(maxKey));
            System.out.println("log2 maxKey = " + maxKey);
            if (maxKey > 0) {
                curFlags -= maxKey;
            }
        }
    }
}
