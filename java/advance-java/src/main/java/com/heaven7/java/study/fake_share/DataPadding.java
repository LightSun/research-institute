package com.heaven7.java.study.fake_share;

public class DataPadding{
    long a1,a2,a3,a4,a5,a6,a7,a8;//防止与前一个对象产生伪共享
    int value;
    long modifyTime;
    long b1,b2,b3,b4,b5,b6,b7,b8;//防止不相关变量伪共享;
    boolean flag;
    long c1,c2,c3,c4,c5,c6,c7,c8;//防止不相关变量伪共享;
    long createTime;
    char key;
    long d1,d2,d3,d4,d5,d6,d7,d8;//防止与下一个对象产生伪共享

    // 类前加上代表整个类的每个变量都会在单独的cache line中
    @sun.misc.Contended
    @SuppressWarnings("restriction")
    public class ContendedData {
        int value;
        long modifyTime;
        boolean flag;
        long createTime;
        char key;
    }
    // 属性前加上时需要加上组标签
    @SuppressWarnings("restriction")
    public class ContendedGroupData {
        @sun.misc.Contended("group1")
        int value;
        @sun.misc.Contended("group1")
        long modifyTime;

        @sun.misc.Contended("group2")
        boolean flag;

        @sun.misc.Contended("group3")
        long createTime;
        @sun.misc.Contended("group3")
        char key;
    }

}