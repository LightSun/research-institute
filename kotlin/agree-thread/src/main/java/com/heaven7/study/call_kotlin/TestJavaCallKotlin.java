package com.heaven7.study.call_kotlin;

import com.heaven7.study.java_calling_kotlin.*;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.reflect.KClass;

import java.io.IOException;
import java.util.List;

/**.
 * kotlin 可以直接调用java. 不过省略new关键字
 * 从java调用kotlin
 *   kotlin官方文档: https://kotlinlang.org/docs/reference/using-gradle.html#configuring-dependencies
 *                  https://kotlinlang.org/docs/reference/java-to-kotlin-interop.html#instance-fields
 */
public class TestJavaCallKotlin {

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

        //静态方法
        C.foo(); // works fine
        //C.bar(); // error: not a static method
        // 伴生对象
        C.Companion.foo(); // instance method remains
        C.Companion.bar(); // the only way it works

        // java里使用kotlin反射
        KClass<Foo> kClass = JvmClassMappingKt.getKotlinClass(Foo.class);
        log(kClass.getTypeParameters());

        C c = new C();
       // c.filterValid(null)
        //c.filterValidInt(null)

        //kotlin 1个 方法或者构造。生成多个。 java可调用
        Foo2 foo2 = new Foo2(1, 1.5);
        Foo2 foo3 = new Foo2(1);
        foo3.f("", 0, "");
        //...etc
        try {
            foo3.test();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //泛型相关。
        Box<Derived> box = demo2.boxDerived(new Derived());
        Base base = demo2.unboxBase(box);

        Box<? extends Derived> box1 = demo2.boxDerived2(new Derived());
       // Base base1 = demo2.unboxBase2(box1); //error

        //取消泛型
        List list = demo2.emptyList();
    }

    private static void log(Object obj){
        System.out.println(obj);
    }
}
