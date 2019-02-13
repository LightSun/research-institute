import org.mozilla.javascript.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/** @author heaven7 */
public class TestJs {

    public static void main(String[] args) {
        testPi();
        testEnum();
        //
        Context cx = Context.enter();
        ScriptableObject scope = cx.initSafeStandardObjects();
        String jsFile =
                "E:\\study\\github\\research-institute\\java\\study-rhino\\src\\main\\resources\\test_pi.js";
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

    private static void loadJs(Context cx, ScriptableObject scope, String jsFile) {
        long loadStart = System.currentTimeMillis();
        FileReader in = null;
        try {
            in = new FileReader(jsFile);
        } catch (FileNotFoundException ex) {
            Context.reportError("Couldn't open file \"" + jsFile + "\".");
            return;
        }

        try {
            // Here we evalute the entire contents of the file as
            // a script. Text is printed only if the print() function
            // is called.
            long start =  System.currentTimeMillis();
            cx.evaluateReader(scope, in, jsFile, 1, null);
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
}
