package class1

/**
 * 泛型。 和java略微不同.  * 表示通配符
Function<*, String> means Function<in Nothing, String>;
Function<Int, *> means Function<Int, out Any?>;
Function<*, *> means Function<in Nothing, out Any?>.
 */
fun main(args: Array<String>) {
    val ints: Array<Int> = arrayOf(1, 2, 3)
    val any = Array<Any>(3) { "" }
    //copy(ints, any) // Error: expects (Array<Any>, Array<Any>)

    copy2(ints, any) //ok. 第一个参数定义时用了out限定符表示super T
}

//=============== 方法泛型==================
fun <T> singletonList(item: T): List<T> {
    // ...
    //list 内涵it属性表示item
    return listOf(item)
}

fun <T> T.basicToString() : String? {  // extension function
    // ...
    return null
}

// 上限定. extends T
fun <T : Comparable<T>> sort2(list: List<T>) {
    // ...
}

//================== 方法参数泛型 ============================

fun copy(from: Array<Any>, to: Array<Any>) {
    assert(from.size == to.size)
    for (i in from.indices)
        to[i] = from[i]
}
fun copy2(from: Array<out Any>, to: Array<Any>) {
    assert(from.size == to.size)
    for (i in from.indices)
        to[i] = from[i]
}


//============ 类泛型 ================
abstract class Source<out T> {
    abstract fun nextT(): T
}

//Source类的泛型用out指定了。 所有可以用任何super T的对象
fun demo(strs: Source<String>) {
    val objects: Source<Any> = strs // This is OK, since T is an out-parameter
    // ...
}

abstract class Comparable<in T> {
    abstract fun compareTo(other: T): Int
}
// Comparable对象 用了in指定， 表示 可以用任何 extends T的
fun demo(x: Comparable<Number>) {
    x.compareTo(1.0) // 1.0 has type Double, which is a subtype of Number
    // Thus, we can assign x to a variable of type Comparable<Double>
    val y: Comparable<Double> = x // OK!
}