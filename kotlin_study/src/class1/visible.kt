package class1

/**
 * The [ internal ] visibility modifier means that the member is visible within the same module.
 * More specifically, a module is a set of Kotlin files compiled together:
        an IntelliJ IDEA module;
        a Maven project;
        a Gradle source set;
        a set of files compiled with one invocation of the Ant task.
 */
// file name: visible.kt

private fun foo() {} // visible inside visible.kt

public var bar: Int = 5 // property is visible everywhere
    private set         // setter is visible only in visible.kt

internal val baz = 6    // visible inside the same module


open class Outer {
    private val a = 1
    protected open val b = 2
    internal val c = 3   //整个module中可见
    val d = 4  // public by default

    protected class Nested {
        public val e: Int = 5
    }
}

class Subclass : Outer() {
    // a is not visible
    // b, c and d are visible
    // Nested and e are visible

    override val b = 5   // 'b' is protected
}

/**
 * kotlin 中 protected 仅对子类可见。同包不可见（区别于java）
 */
class Unrelated(o: Outer) {
    // o.a, o.b are not visible
    // o.c and o.d are visible (same module)
    // Outer.Nested is not visible, and Nested::e is not visible either
}


class CC private constructor(a: Int)