package com.heaven7.java.study.cpu_cache;

public class CpuCacheTest {

    /** 指令级别并发:
     *  一般来说 Loop 1消耗的时间 比2 少， 但是在java中恰好相反
     */
    public static void main(String [] args){
        int steps = 256 * 1024 * 1024;
        int[] a = new int[2];

        long start_1 = System.currentTimeMillis();
// Loop 1
        for (int i=0; i<steps; i++) { a[0]++; a[1]++; }
        System.out.println("cost >>>  loop 1: " + (System.currentTimeMillis() - start_1));

        long start_2 = System.currentTimeMillis();
// Loop 2
        for (int i=0; i<steps; i++) { a[0]++; a[0]++; }
        System.out.println("cost >>>  loop 2: " + (System.currentTimeMillis() - start_2));
    }

}
