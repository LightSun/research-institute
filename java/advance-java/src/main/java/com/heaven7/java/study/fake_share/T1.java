package com.heaven7.java.study.fake_share;

import org.openjdk.jol.info.ClassData;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.layouters.CurrentLayouter;
import sun.misc.VMSupport;

/**
 * Java对象的内存布局：对象头（Header）、实例数据（Instance Data）和对齐填充（Padding）。无论是32位还是64位的HotSpot，使用的都是8字节对齐。也就是说每个java对象，占用的字节数都是8的整数倍。（对象头 + 实例数据 + padding） % 8等于0且0 <= padding < 8。在网上看到各种介绍如何手动计算对象大小的文章，总结了几点：
 * 1.基本数据类型占用的字节数，JVM规范中有明确的规定，无论是在32位还是64位的虚拟机，占用的内存大小是相同的。
 * 2.reference类型在32位JVM下占用4个字节，但是在64位下可能占用4个字节或8个字节，这取决于是否启用了64位JVM的指针压缩参数UseCompressedOops。
 * 3.new Object()这个对象在32位JVM上占8个字节，在64位JVM上占16个字节。
 * 4.开启(-XX:+UseCompressedOops)指针压缩，对象头占12字节; 关闭(-XX:-UseCompressedOops)指针压缩,对象头占16字节。
 * 5.64位JVM上，数组对象的对象头占用24个字节，启用压缩之后占用16个字节。之所以比普通对象占用内存多是因为需要额外的空间存储数组的长度。
 * 6.对象内存布局中的实例数据，不包括类的static字段的大小，因为static字段是属于类的，被该类的所有对象共享。
 */
public class T1 {
    private int id;
    private long[] arrs;

    //java object layout. 查看jvm对象的布局
    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("java.vm.name")); //Java HotSpot(TM) 64-Bit Server VM

        CurrentLayouter layouter = new CurrentLayouter();
        ClassLayout layout = layouter.layout(ClassData.parseClass(T1.class));
        System.out.println(layout.toPrintable());

        System.out.println(layouter.layout(ClassData.parseClass(MyClass.class)).toPrintable());
    }

    static class MyClass {
        byte a;
        int c;
        boolean d;
        long e;
        Object f;

    }

}  