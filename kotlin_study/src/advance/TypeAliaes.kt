package advance

import java.io.File
import javax.xml.soap.Node

/**
 * 类型 取别名
 */
fun main(args: Array<String>) {
    //f 是 函数表达式
    val f: (Int) -> Boolean = { it > 0 }
    println(foo(f)) // prints "true"

    //p 是 函数表达式
    val p: Predicate<Int> = { it > 0 }
    //可变参数： vararg elements: T
    println(listOf(1, -2).filter(p)) // prints "[1]"
}

fun foo(p: Predicate<Int>) = p(42)

typealias NodeSet = Set<Node>
typealias FileTable<K> = MutableMap<K, MutableList<File>>
typealias MyHandler = (Int, String, Any) -> Unit
typealias Predicate<T> = (T) -> Boolean


class TypealiasA {
    inner class Inner
}

class TypealiasB {
    inner class Inner
}

typealias AInner = TypealiasA.Inner
typealias BInner = TypealiasB.Inner