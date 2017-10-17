package class1

import kotlin.reflect.jvm.internal.impl.resolve.constants.EnumValue


fun main(args: Array<String>) {
    //Color.valueOf("0xFF0000") // Color
    //Color.values(): Array<EnumClass>
    printAllValues<Color>()
}


enum class Direction {
    NORTH, SOUTH, WEST, EAST
}

inline fun <reified T : Enum<T>> printAllValues() {
    print(enumValues<T>().joinToString {
          it.name
       }
    )
}

enum class Color(val rgb: Int) {
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF)
}

enum class ProtocolState {
    WAITING {
        override fun signal() = TALKING
    },

    TALKING {
        override fun signal() = WAITING
    };

    abstract fun signal(): ProtocolState
}