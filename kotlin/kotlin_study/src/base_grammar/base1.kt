package base_grammar

fun main(args: Array<String>) {
    println(max(args[0].toInt(), args[1].toInt()))

    val x = parseInt("5")
   // val y = parseInt(" 8")//error
    val y = parseInt("h")//error

    // We cannot say 'x * y' now because they may hold nulls
    if (x != null && y != null) {
        print(x * y) // Now we can
    } else {
        println("One of the arguments is null")
    }

    println(getStringLength("aaa"))
    println(getStringLength(1))

    //while
    var i = 0
    while (i < args.size)
        println(args[i++])

    for (arg in args)
        println(arg)
}

fun getStringLength(obj: Any): Int? {
    if (obj is String)
        return obj.length // no cast to String is needed
    return null
}

fun max(a: Int, b: Int) =
        if (a > b) a else b

// Return null if str does not hold a number
fun parseInt(str: String): Int? {
    try {
        return str.toInt()
    } catch (e: NumberFormatException) {
        println("One of the arguments isn't Int")
    }
    return null
}