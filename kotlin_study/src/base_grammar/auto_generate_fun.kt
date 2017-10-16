package base_grammar

data class User(val name: String, val id: Int)

fun main(args: Array<String>) {
    val user = User("Alex", 1)
    println(user) // toString()

    val secondUser = User("Alex", 1)
    val thirdUser = User("Max", 2)

    println("user == secondUser: ${user == secondUser}")
    println("user == thirdUser: ${user == thirdUser}")

    // copy() function. 自动生成的
    println(user.copy())
    println(user.copy("Max"))
    println(user.copy(id = 2))
    println(user.copy("Max", 2))
}
