package base_grammar

/**
 * 关键字： http://kotlinlang.org/docs/reference/keyword-reference.html
 * val declares a read-only property or local variable
 */
fun main(args: Array<String>) {
    //跟java略微不同 ,  loop: (java)
    loop@ for (i in 1..100) {
        for (j in 1..100) {
            if (j > 10)
                break@loop
        }
    }

    val numbers: IntArray = intArrayOf(10, 20, 30, 40, 50)
    foo(numbers)
    println("===================")
    foo2(numbers)
    println("===================")
    foo3(numbers)
}

fun foo(ints: IntArray){
    ints.forEach lit@ {
        if (it == 30) return@lit
        println(it)
    }
}

fun foo2(ints: IntArray) {
    ints.forEach {
        if (it == 30) return@forEach
        println(it)
    }
}
//kotlin 没有 continue
fun foo3(ints: IntArray) {
    ints.forEach {
        if (it == 30) return
        println(it)
    }
}