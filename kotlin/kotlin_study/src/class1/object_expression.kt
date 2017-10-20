package class1


fun main(args: Array<String>) {
}

 class C8 {
    // Private function, so the return type is the anonymous object type
    private fun foo() = object {
        val x: String = "x"
    }

    // Public function, so the return type is Any
    fun publicFoo() = object {
        val x: String = "x"
    }

    fun bar() {
        val x1 = foo().x        // ok
        //val x2 = publicFoo().x  // ERROR: Unresolved reference 'x'
    }
}