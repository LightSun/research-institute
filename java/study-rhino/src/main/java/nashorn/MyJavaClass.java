package nashorn;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.Arrays;

/**
 * @author heaven7
 */
public class MyJavaClass {

    public static String fun1(String name) {
        System.out.format("Hi there from Java, %s", name);
        return "greetings from java";
    }

    public static void fun3(ScriptObjectMirror mirror) {
        System.out.println(mirror.getClassName() + ": " +
                Arrays.toString(mirror.getOwnKeys(true)));
    }
}
