package class1

fun main(args: Array<String>) {

    val tp = TestProp();
    //tp.setterVisibility = 5; //set is private
}

class TestProp{

    var allByDefault: Int? = 0 // default getter and setter implied
    var initialized = 1 // has type Int, default getter and setter
    val isEmpty: Boolean get() = this.initialized == 0

    var setterVisibility: String = "abc"
        private set // the setter is private and has the default implementation

    var stringRepresentation: String
        get() = this.toString()
        set(value) {
            setDataFromString(value) // parses the string and assigns values to other properties
        }

    private fun setDataFromString(value: String) {
        //need impl
    }

    //var setterWithAnnotation: Any? = null @Inject set // annotate the setter with Inject
}

private var _table: Map<String, Int>? = null
private val table: Map<String, Int>
    get() {
        if (_table == null) {
            _table = HashMap() // Type parameters are inferred
        }
        return _table ?: throw AssertionError("Set to null by another thread")
    }

const val SUBSYSTEM_DEPRECATED: String = "This subsystem is deprecated"