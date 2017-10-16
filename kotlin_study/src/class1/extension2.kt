package class1

import java.util.*


fun main(args: Array<String>) {
    C6().caller(D6())   // prints "D.foo in C"
    C7().caller(D6())  // prints "D.foo in C1" - dispatch receiver is resolved virtually
    C6().caller(D7())  // prints "D.foo in C" - extension receiver is resolved statically

    // Java
   // Collections.swap(list, Collections.binarySearch(list, Collections.max(otherList)), Collections.max(list));
}

//==================================================
open class D6 {
}

class D7 : D6() {
}

open class C6 {
    open fun D6.foo() {
        println("D.foo in C")
    }

    open fun D7.foo() {
        println("D1.foo in C")
    }

    fun caller(d: D6) {
        d.foo()   // call the extension function
    }
}

class C7 : C6() {
    override fun D6.foo() {
        println("D.foo in C1")
    }

    override fun D7.foo() {
        println("D1.foo in C1")
    }
}
//=============================================
/**
 * 相当于枚举的扩展。 但是可以有多个对象。
 * 不允许非私有构造
 * 请注意，扩展类（间接继承器）的子类的类可以放置在任何位置，而不必在同一个文件中。
 */
//密封类不能直接实例化。
sealed class Expr

data class Const(val number: Double) : Expr(){
}
data class Sum(val e1: Expr, val e2: Expr) : Expr()
object NotANumber : Expr()

fun eval(expr: Expr): Double = when(expr) {
    is Const -> expr.number
    is Sum -> eval(expr.e1) + eval(expr.e2)
    NotANumber -> Double.NaN
// the `else` clause is not required because we've covered all the cases
}