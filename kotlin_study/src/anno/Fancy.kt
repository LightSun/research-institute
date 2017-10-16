package anno

@Target(AnnotationTarget.CLASS,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.EXPRESSION,
        AnnotationTarget.CONSTRUCTOR,
        AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Fancy


@Fancy class Foo {
    @Fancy fun baz(@Fancy foo: Int): Int {
        return (@Fancy 1)
    }
}

class Foo2 @Fancy constructor(dependency: MyDependency) {
    // ...
}
class Foo3 {
    var x: MyDependency? = null
        @Fancy set
}

class MyDependency