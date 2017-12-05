
# java object layout

* Java对象的内存布局：对象头（Header）、实例数据（Instance Data）和对齐填充（Padding）。无论是32位还是64位的HotSpot，使用的都是8字节对齐。
也就是说每个java对象，占用的字节数都是8的整数倍。（对象头 + 实例数据 + padding） % 8等于0且0 <= padding < 8。
在网上看到各种介绍如何手动计算对象大小的文章，总结了几点：
  1.基本数据类型占用的字节数，JVM规范中有明确的规定，无论是在32位还是64位的虚拟机，占用的内存大小是相同的。
  2.reference类型在32位JVM下占用4个字节，但是在64位下可能占用4个字节或8个字节，这取决于是否启用了64位JVM的指针压缩参数UseCompressedOops。
  3.new Object()这个对象在32位JVM上占8个字节，在64位JVM上占16个字节。
  4.开启(-XX:+UseCompressedOops)指针压缩，对象头占12字节; 关闭(-XX:-UseCompressedOops)指针压缩,对象头占16字节。
  5.64位JVM上，数组对象的对象头占用24个字节，启用压缩之后占用16个字节。之所以比普通对象占用内存多是因为需要额外的空间存储数组的长度。
  6.对象内存布局中的实例数据，不包括类的static字段的大小，因为static字段是属于类的，被该类的所有对象共享。

参考文章： http://blog.csdn.net/aitangyong/article/details/46416667
Java对象内存结构： http://www.importnew.com/1305.html

#  对象属性在内存中按照下面的顺序来组织：
  * 1. 双精度型（doubles）和长整型（longs）
  * 2. 整型（ints）和浮点型（floats）
  * 3. 短整型（shorts）和字符型（chars）
  * 4. 布尔型（booleans）和字节型（bytes）
  * 5. 引用类型（references）

# 类属性按照如下优先级进行排列(减小内存的开销)：
    长整型和双精度类型；整型和浮点型；字符和短整型；字节类型和布尔类型，最后是引用类型。这些属性都按照各自的单位对齐。