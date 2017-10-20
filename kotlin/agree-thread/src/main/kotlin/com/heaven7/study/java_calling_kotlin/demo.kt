@file:JvmName("DemoUtils")
package com.heaven7.study.java_calling_kotlin


class Foo(val id: String){

    companion object {
        @JvmField
        val COMPARATOR: Comparator<Foo> = compareBy<Foo> { it.id }
    }
    /**
     * You can annotate a property with @JvmField if it has a backing field, is not private,
     * does not have open, override or const modifiers, and is not a delegated property.
     * //不能是private, open, override, const, delegated
     */
    @JvmField val ID = id
}
//单例
object Singleton {
    lateinit var provider: Foo
}

//伴生对象
class BuildCs{
    companion object {
        const val VERSION = 9
    }
}

object Obj{
    const val CONST = 1
    init {

    }
}
const val MAX = 239


class C {
    init {

    }
    constructor()
    constructor(name: String){
      //  logger.info("User initialized with name: ${name}")
    }
    companion object {
        @JvmStatic fun foo() {}
        fun bar() {} //not static
    }

    /**
     * 解决字段.get/set 和其他方法名字相同的情况
     */
    val x: Int
        @JvmName("getX_prop")
        get() = 15

    fun getX() = 10

    fun List<String>.filterValid(): List<String>?{
        //mock...
        return null
    }

    /**
     * 解决在java层签名相同的情况
     */
    @JvmName("filterValidInt")
    fun List<Int>.filterValid(): List<Int>?{
        //mock...
        return null
    }
}

//================= 静态

fun bar() {
}
