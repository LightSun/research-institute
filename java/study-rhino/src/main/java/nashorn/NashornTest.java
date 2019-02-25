package nashorn;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * nashorn: support ECMA 5.1.      Ecma 6 not support
 * @author heaven7
 */
public class NashornTest {

    //private static final ScriptEngine ENGINE = new NashornScriptEngineFactory().getScriptEngine();

    public static void main(String[] args)  throws Exception {

        //test1();
        //test2();
       // testCallJava();
        testJavaCallJsFunc();
    }

    private static void testJavaCallJsFunc() throws Exception {
        ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine();
        engine.eval(getReader("/nashorn_java_call_js_func.js"));
        Invocable invocable = (Invocable) engine;

        Object result = invocable.invokeFunction("fun1", "Peter Parker");
        System.out.println(result);
        System.out.println(result.getClass());
    }

    private static void testCallJava()throws ScriptException {
        ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine();
        engine.eval(getReader("/nashorn_cal_java.js"));
    }

    private static void test2() throws ScriptException{
        ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine();
       // engine.eval(getReader("/untitled0.js")); //not support require
        //engine.eval(getReader("/testclass.js")); // not support class.
    }

    private static void test1() throws ScriptException {
        long start = System.currentTimeMillis();
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        ScriptEngine engine = factory.getScriptEngine();
        engine.eval("print('Hello World!');");
        System.out.println(System.currentTimeMillis() - start); // 375

        start = System.currentTimeMillis();
        engine.eval("print('Hello World!');");
        System.out.println(System.currentTimeMillis() - start); //0

       // engine.eval("const a = 1;"); // not
        //engine.eval("let a = 1;");  // not
    }

    private static Reader getReader(String path){
        return new InputStreamReader(NashornTest.class.getResourceAsStream(path));
    }
}
