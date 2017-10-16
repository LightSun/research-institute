package class1

fun main(args: Array<String>) {
    val instance = MyClass.create()
    //匿名
    val x = MyClass2.Companion  //只能给没有取名字的 companion(伴生)
    println(x)

    val instance2 = MyClass3.create()
}

class MyClass {
    companion object Factory {
        fun create(): MyClass = MyClass()
    }
}

class MyClass2 {
    companion object


}

interface Factory<T> {
    fun create(): T
}

class MyClass3 {
    companion object : Factory<MyClass3> {
        override fun create(): MyClass3 = MyClass3()
    }
}
