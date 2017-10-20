@file:JvmName("demo2")
package com.heaven7.study.java_calling_kotlin

import java.io.IOException

class Foo2 @JvmOverloads constructor(x: Int, y: Double = 0.0) {
    @JvmOverloads fun f(a: String, b: Int = 0, c: String = "abc") {

    }


    /**
     * java 层去捕获异常
     */
    @Throws(IOException::class)
    fun test() {
        throw IOException()
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

class Box<out T>(val value: T)

interface Base
class Derived : Base

fun boxDerived(value: Derived): Box<Derived> = Box(value)
fun unboxBase(box: Box<Base>): Base = box.value

/**
 * 为解决kotlin泛型和java泛型通配符的问题
 */
fun boxDerived2(value: Derived): Box<@JvmWildcard Derived> = Box(value)
// is translated to
// Box<? extends Derived> boxDerived(Derived value) { ... }

/**
 * JvmSuppressWildcards 可用于函数，类，参数，等
 */
fun unboxBase2(box: Box<@JvmSuppressWildcards Base>): Base = box.value
// is translated to
// Base unboxBase(Box<Base> box) { ... }

fun emptyList(): List<Nothing> = listOf()
// is translated to
// List emptyList() { ... }