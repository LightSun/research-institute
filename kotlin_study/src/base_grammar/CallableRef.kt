package base_grammar


fun main(args: Array<String>) {
    val numbers = listOf(1, 2, 3)
    println(numbers.filter(::isOdd)) // ::isOdd 为true的将被过滤

    //组合过滤
    val oddLength = compose(::isOdd, ::length)
    val strings = listOf("a", "ab", "abc")
    println(strings.filter(oddLength))
    println(oddLength)
}

fun isOdd(x: Int) = x % 2 != 0

fun length(s: String) = s.length

// 组合. 函数表达式
/**
 * f, g 都是变量名（这里代表函数/方法）.
 * 此方法相当于 compose(f, g) = f(g(*)). 结果类型为f函数的结果类型
 */
fun <A, B, C> compose(f: (B) -> C, g: (A) -> B )  : (A) -> C {
    //过程 从输入A  -> B  -> C
    return {
        x ->
        println(g(x)) //g(x) 这里是 length 函数
        f(g(x))       //f(..)  这里是isOdd 函数
    }
}