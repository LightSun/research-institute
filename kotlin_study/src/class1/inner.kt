package class1

import java.awt.event.MouseAdapter


//============ Anonymous inner classes ====================
/*window.addMouseListener(object: MouseAdapter() {
    override fun mouseClicked(e: MouseEvent) {
        // ...
    }

    override fun mouseEntered(e: MouseEvent) {
        // ...
    }
})*/
//=============== inner class
class Outer2 {
    private val bar: Int = 1
    inner class Inner {
        fun foo() = bar
    }
}

//val demo = Outer2().Inner().foo() // == 1