package class1


/**
 * data class自带： hashCode, equals. copy.
 *         componentN() 根据属性定义数学怒
 */
data class User4(val name: String, val age: Int){

    val n2:String = "name2"
    val n3:String = "name3"

}

fun main(args: Array<String>) {
    var user =  User4("heaven7", 26)
    println(user.component1()) //只有参加构造的属性才有componentN
    println(user.component2())
    val u2 =user.copy()
    println(u2.toString())
   //反构造解析出 data class 属性
    val (name, age) = u2
    println("$name, $age years of age") // prints "Jane, 35 years of age"
}