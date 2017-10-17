package base_grammar

fun main(args: Array<String>) {
    val map = hashMapOf<String, Int>()
    map.put("one", 1)
    map.put("two", 2)

    for ((key, value) in map) {
        println("key = $key, value = $value")
    }

    //加俩个叹号表示， 如果该key对应的value不存在。就会异常。也就是说肯定该value不能为null.
    println(map["one"]!!)
    println(map["4"]!!)
}