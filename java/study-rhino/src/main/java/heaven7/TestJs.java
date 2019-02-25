package heaven7;

import org.mozilla.javascript.*;
import org.mozilla.javascript.commonjs.module.Require;
import org.mozilla.javascript.commonjs.module.provider.StrongCachingModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.provider.UrlModuleSourceProvider;
import org.mozilla.javascript.tools.shell.Global;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * -keep class org.mozilla.javascript.** { *; }
 * -dontwarn org.mozilla.javascript.**
 * @author heaven7 */
public class TestJs {

    //liquidcore。 完全兼容nodejs语法。 lib:  22 + 15M ()
    public static void main(String[] args) {
        //testLoadSimpleJs();
       // loadJs("/untitled0.js");
        //loadJs("/test_extend.js");
        //loadJs("/test_func_class.js");
       // loadJs("/testjs_oth.js");
       // loadJs("/testclass.js");
        loadJs("/jsdoc.js");
    }

    private static void testLoadSimpleJs() {
        //testPi();
        //testEnum();
        //
        Context cx = Context.enter();
        ScriptableObject scope = cx.initStandardObjects();
        String[] names = { "print"};
        scope.defineFunctionProperties(names, TestJs.class, ScriptableObject.DONTENUM);

        String jsFile =
                "E:\\study\\github\\research-institute\\java\\study-rhino\\src\\main\\resources\\enum.js";
        try {
            loadJs(cx, scope, jsFile);
        } finally {
            Context.exit();
        }
    }

    private static void testEnum() {
        long start = System.currentTimeMillis();
        Enumeration<Integer> en = new Enumeration<Integer>(){
            List<Integer> list = Arrays.asList(1, 2, 3);
            int index;
            @Override
            public boolean hasMoreElements() {
                return index < list.size();
            }

            @Override
            public Integer nextElement() {
                return list.get(index ++);
            }
        };
        while (en.hasMoreElements()){
            en.nextElement();
        }
        System.out.println("java >>> testEnum(). " + (System.currentTimeMillis() - start));
    }

    private static void testPi() {
        long loadStart = System.currentTimeMillis();
        float Pi = 0;
        float n = 1;
        for (int i = 0; i < 100000000; i++) {
            Pi = Pi + (4 / n) - (4 / (n + 2));
            n = n + 4;
        }
        System.out.println("java >>> testPi(). " + (System.currentTimeMillis() - loadStart));
    }
    //test require
    public static void loadJs(String js){ // js like '/testRelativeId.js'
        final Context cx = Context.enter();
        //cx.setOptimizationLevel(-1);
        try{
            ScriptableObject scope = new Global(cx); //below also ok
           /* final ScriptableObject scope = cx.initStandardObjects();
            String[] names = { "print"};
            scope.defineFunctionProperties(names, TestJs.class, ScriptableObject.DONTENUM);*/
           ScriptableObject.putProperty(scope, "file", Context.javaToJS(new JsFile("heaven7.txt"), scope));

            final Require require = getSandboxedRequire(cx, scope);
            require.install(scope);
            cx.evaluateReader(scope, getReader(js), js, 1, null);
        }catch (Exception e){
            e.printStackTrace();
        } finally{
            Context.exit();
        }
    }
    public static void loadJs(Context cx, ScriptableObject scope, String jsFile) {
        FileReader reader = null;
        try {
            reader = new FileReader(jsFile);
            loadJs(cx, scope, jsFile, reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Context.reportError("Couldn't open file \"" + jsFile + "\".");
        }finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static void loadJs(Context cx, ScriptableObject scope, String srcName, Reader in) {
        long loadStart = System.currentTimeMillis();
        try {
            // Here we evalute the entire contents of the file as
            // a script. Text is printed only if the print() function
            // is called.
            long start =  System.currentTimeMillis();
            cx.evaluateReader(scope, in, srcName, 1, null);
            System.out.println("only invoke >>> cost time =" + (System.currentTimeMillis() - start));
            System.out.println("whole >>> cost time =" + (System.currentTimeMillis() - loadStart));
        } catch (WrappedException we) {
            System.err.println(we.getWrappedException().toString());
            we.printStackTrace();
        } catch (EvaluatorException ee) {
            System.err.println("js: " + ee.getMessage());
        } catch (JavaScriptException jse) {
            System.err.println("js: " + jse.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.toString());
        } finally {
            try {
                in.close();
            } catch (IOException ioe) {
                System.err.println(ioe.toString());
            }
        }
    }

    public static void print(Context cx, Scriptable thisObj,
                             Object[] args, Function funObj) { //load(['enum.js'])
        for (int i=0; i < args.length; i++) {
            if (i > 0)
                System.out.print(" ");

            // Convert the arbitrary JavaScript value into a string form.
            String s = Context.toString(args[i]);

            System.out.print(s);
        }
        System.out.println();
    }
    private static Require getSandboxedRequire(Context cx, Scriptable scope)
            throws URISyntaxException {
        return new Require(
                cx,
                scope,
              //  cx.initStandardObjects(),
                new StrongCachingModuleScriptProvider(
                        new UrlModuleSourceProvider(Collections.singleton(getDirectory()), null)),
                null,
                null,
                true);
    }
    private static URI getDirectory() throws URISyntaxException {
        URL resource = TestJs.class.getResource("/untitled.js"); //需要加斜杠。否则失败。
        final String jsFile = resource.toExternalForm();
        return new URI(jsFile.substring(0, jsFile.lastIndexOf('/') + 1));
    }
    private static Reader getReader(String name) {
        return new InputStreamReader(TestJs.class.getResourceAsStream(name));
    }
}
