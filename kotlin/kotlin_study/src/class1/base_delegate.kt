package base_grammar

import kotlin.reflect.KProperty

/**
 * kotlin 反射
 */
fun main(args: Array<String>) {
    val e = Example()
    println(e.p) //自动调用 Delegate.getValue
    e.p = "NEW"  //自动调用 Delegate.setValue
}


class Example {
    var p: String by Delegate()  //属性通过Delegate映射

    override fun toString() = "Example Class"
}

class Delegate() {
    //thisRef 当前对象, prop当前的属性
    operator fun getValue(thisRef: Any?, prop: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${prop.name}' to me!"
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, value: String) {
        println("$value has been assigned to ${prop.name} in $thisRef")
    }
}
