package class1

fun main(args: Array<String>) {
    val l = mutableListOf(1, 2, 3)
    l.swap(0, 2) // 'this' inside 'swap()' will hold the value of 'l'
    print(l.toString(5))
}

//这里this 代表的是这个list.
fun MutableList<Int>.swap(index1: Int, index2: Int) {
    val tmp = this[index1] // 'this' corresponds to the list
    this[index1] = this[index2]
    this[index2] = tmp
}

fun <T> MutableList<T>.swap2(index1: Int, index2: Int) {
    val tmp = this[index1] // 'this' corresponds to the list
    this[index1] = this[index2]
    this[index2] = tmp
}

fun Any?.toString(i : Int): String {
    if (this == null) return "null"
    // after the null check, 'this' is autocast to a non-null type, so the toString() below
    // resolves to the member function of the Any class
    return toString()
}

//====================================
open class CCCC{
    //kotlin 可以在任意对象上附加属性...相当6
    val <T> List<T>.lastIndex: Int
        get() = size - 1
}

class D: CCCC()

fun CCCC.foo() = "c"

fun D.foo() = "d"

fun printFoo(c: CCCC) {
    println(c.foo())
}
//======================================


class MyClass5 {
    companion object { }  // will be called "Companion"
}

fun MyClass5.Companion.foo() {
    // ...
}

//======================================
/**
    package com.example.usage

    import foo.bar.goo // importing all extensions by name "goo"
    // or
    import foo.bar.*   // importing everything from "foo.bar"

    fun usage(baz: Baz) {
        baz.goo()
    }

 */

//=====================================================
class C5 {
    fun D.foo() {
        toString()         // calls D.toString()
        this@C5.toString()  // calls C5.toString()
    }
}