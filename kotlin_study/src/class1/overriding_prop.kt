package class1

open class Foo {
    open val x: Int get() {
       // ...
        return 0;
    }
}

class Bar1 : Foo() {
    override val x: Int =  5;
}

interface Foo2 {
    val count: Int
}

class Bar2(override val count: Int) : Foo2

class Bar3 : Foo2 {
    override var count: Int = 0
}