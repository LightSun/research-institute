package com.heaven7.study.call_kotlin;

import com.heaven7.study.java_calling_kotlin.*;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.reflect.KClass;

/**.
 * kotlin 可以直接调用java. 不过省略new关键字
 * 从java调用kotlin
 *   kotlin官方文档: https://kotlinlang.org/docs/reference/using-gradle.html#configuring-dependencies
 */
public class TestJavaCallKotlin {

    private KClass<Foo> kClass;

    /**
     * 可见性: 映射
     private -> private
     private top-level declarations -> package-local declarations;
     protected -> protected
          (note that Java allows accessing protected members from other classes in the same package and Kotlin doesn't,
          so Java classes will have broader access to the code);
     internal declarations become public in Java.
           Members of internal classes go through name mangling,
           to make it harder to accidentally use them from Java and to allow overloading for members with the same signature
           that don't see each other according to Kotlin rules;
     public remains public.
     * @param args
     */
    public static void main(String[] args){
        Foo foo = new Foo("123");
        //访问对象成员
        System.out.println(foo.ID);
        //访问静态成员
        System.out.println(Foo.COMPARATOR);

        com.heaven7.study.java_calling_kotlin.DemoUtils.bar();

        //访问工具方法
        Utils.foo();
        Utils.bar2();

        Singleton.provider = foo;
        System.out.println(BuildCs.VERSION);
        System.out.println(Obj.CONST);
        System.out.println(DemoUtils.MAX);

        C.foo(); // works fine
        //C.bar(); // error: not a static method
        C.Companion.foo(); // instance method remains
        C.Companion.bar(); // the only way it works

        KClass<Foo> kClass = JvmClassMappingKt.getKotlinClass(Foo.class);
        log(kClass.getTypeParameters());

        C c = new C();
       // c.filterValid(null)
        //c.filterValidInt(null)

        Foo2 foo2 = new Foo2(1, 1.5);
        Foo2 foo3 = new Foo2(1);
        foo3.f("", 0, "");
        //...etc
    }

    private static void log(Object obj){
        System.out.println(obj);
    }
}
