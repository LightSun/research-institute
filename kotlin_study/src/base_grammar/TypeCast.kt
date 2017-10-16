package base_grammar

/**
 * 类型转化
 */
fun main(args: Array<String>) {
    demo(null)
    demo("123")
    demo(args)

   /* var x = 5;
    when (x) {
        is Int -> print(x + 1)
        is String -> print(x.length + 1)
        is IntArray -> print(x.sum())
    }*/

    var y = "124";

    val x1: String = y as String //不能为null, 否则异常
    val x2: String? = y as String?; //可以为null
    val x3: String? = y as? String  //可以为null
}

fun demo(x: Any?) {
    if (x is String) { //if(x !is String){    }
        print(x.length) // x is automatically cast to String
    }
}