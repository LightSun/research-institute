package class1



open class A {
    open fun f() { print("A") }
    fun a() { print("a") }
}
interface B {
    fun f() { print("B") } // interface members are 'open' by default
    fun b() { print("b") }
}

/**
 * 继承 必须是A(). 接口则不用
 */
class C() : A(), B {
    // The compiler requires f() to be overridden:
    override fun f() {
        super<A>.f() // call to A.f()
        super<B>.f() // call to B.f()
    }
}

open class Base {
    open fun f() {}
}

/**
 * abstract class
 */
abstract class Derived : Base() {
    override abstract fun f()
}