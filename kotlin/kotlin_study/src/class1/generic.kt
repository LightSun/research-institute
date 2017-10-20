package class1

import jdk.nashorn.internal.objects.NativeFunction.function

fun main(args: Array<String>) {
    val pair = Pair(1, "one")

    val (num, name) = pair

    println("num = $num, name = $name")

    val (result, status) = getResult(1,2)
    //下面2个句子效果相同
    println("result = $result, status = $status")
    println("result = ${getResult(1,2).component1()}, status = ${getResult(1,2).component2()}")
}

class Pair<K, V>(val first: K, val second: V) {
    operator fun component1(): K {
        return first
    }
    operator fun component2(): V {
        return second
    }
}

//定义类时，构造函数 ： 参数前面需要加var
data class Result(val result: Int, val status: Int)

//在函数时，参数前面不加var
fun getResult(result: Int, status: Int): Result {
    // computations
    return Result(result, status)
}