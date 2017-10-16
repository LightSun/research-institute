package advance

/**
 * 操作符重载: http://kotlinlang.org/docs/reference/operator-overloading.html
 */
fun main(args: Array<String>) {
    var c = Counter(5)
    println(c + 6) //plus
    println( + c)  //unaryPlus

    val x = 6;
    println(x..6)
    c..6

    Counter2().invoke(5)
}

/**
 * Expression	Translated to
a + b	a.plus(b)
a - b	a.minus(b)
a * b	a.times(b)
a / b	a.div(b)
a % b	a.rem(b), a.mod(b) (deprecated)
a..b	a.rangeTo(b)
 */
data class Counter(val dayIndex: Int) {
    operator fun plus(increment: Int): Counter {
        return Counter(dayIndex + increment)
    }
    operator fun unaryPlus(): Counter {
        return Counter(dayIndex + this.dayIndex)
    }
    operator fun rangeTo(x : Int){
       if (x >= dayIndex){
           println(x)
       }
    }

    operator fun invoke(x : Int){
        println("invoke: $x")
    }
}

class Counter2{
    operator fun invoke(x : Int){
        println("invoke: $x")
    }
}