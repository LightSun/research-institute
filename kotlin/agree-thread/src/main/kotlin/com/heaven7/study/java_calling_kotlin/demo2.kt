@file:JvmName("demo2")
package com.heaven7.study.java_calling_kotlin

class Foo2 @JvmOverloads constructor(x: Int, y: Double = 0.0) {
    @JvmOverloads fun f(a: String, b: Int = 0, c: String = "abc") {

    }
}
/**
 * 自动生成对应的java:
Constructors:
Foo(int x, double y)
Foo(int x)

// Methods
void f(String a, int b, String c) { }
void f(String a, int b) { }
void f(String a) { }
 **/