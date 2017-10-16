fun main(args: Array<String>) {
    if (args.size == 0) {
        println("Please provide a name as a command-line argument")
        return
    }
    for (name in args)
        println("Hello, $name!")
    println("Hello, ${args[0]}!")

    val language = "FR"
    println(when (language) {
        "EN" -> "Hello!"
        "FR" -> "Salut!"
        "IT" -> "Ciao!"
        else -> "Sorry, I can't greet you in $language yet"
    })

    Greeter(args[0]).greet()
}


class Greeter(val name: String) {
    fun greet() {
        println("Hello, ${name}");
    }
}