package com.heaven7.vida.research.sample;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.heaven7.core.util.Logger;
import com.heaven7.vida.research.BaseActivity;
import com.heaven7.vida.research.R;

import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import butterknife.BindView;

/**
 * rhino used to running js on java. without webview.
 * Created by heaven7 on 2019/2/11.
 */
public class TestRhinoActivity extends BaseActivity {
    private static final String TAG = "TestRhinoActivity";

    @BindView(R.id.tv1)
    TextView mTv1;
    @BindView(R.id.tv2)
    TextView mTv2;
    @BindView(R.id.tv3)
    TextView mTv3;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_rhino;
    }

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {

        mTv1.setText(runScript(JAVA_CALL_JS_FUNCTION, "Test", new String[] {}));
        mTv2.setText(runScript(JS_CALL_JAVA_FUNCTION, "Test", new String[] {}));
        mTv3.setText(runScript(JS_CALL_JAVA_FUNCTION2, "Test", new String[] {}));
    }

    /** Java执行js的方法 */
    private static final String JAVA_CALL_JS_FUNCTION = "function Test(){ return 'heaven7: java call js Rhino'; }";

    /** js调用Java中的方法 */
    private static final String JS_CALL_JAVA_FUNCTION = //
            "var ScriptAPI = java.lang.Class.forName(\"" + TestRhinoActivity.class.getName() + "\", true, javaLoader);" + //
                    "var methodRead = ScriptAPI.getMethod(\"jsCallJava\", [java.lang.String]);" + //
                    "function jsCallJava(url) {return methodRead.invoke(null, url);}" + //
                    "function Test(){ return jsCallJava('Hello World'); }"; //如果不传参数则默认undefined.(js规则)
    //JavaAdapter can't use on android
    private static final String JS_CALL_JAVA_FUNCTION2 = "var array = [0, 1, 2];\n"+
            "function Test(){" +
            "var array = [0, 1, 2];\n" +
            "\n" +
            "// create an array enumeration.\n" +
            "var elements = new java.util.Enumeration({\n" +
            "        index: 0,\n" +
            "        elements: array,\n" +
            "        hasMoreElements: function() {\n" +
            "                return (this.index < this.elements.length);\n" +
            "\t},      \n" +
            "        nextElement: function() {\n" +
            "                return this.elements[this.index++];\n" +
            "\t}\n" +
            "    });\n" +
            " return 'Hello World' + elements.hasMoreElements(); }";

    /**
     * 执行JS
     *
     * @param js js代码
     * @param functionName js方法名称
     * @param functionParams js方法参数
     * @return
     */
    public String runScript(String js, String functionName, Object[] functionParams) {
        org.mozilla.javascript.Context rhino = org.mozilla.javascript.Context.enter();
        rhino.setOptimizationLevel(-1);
        try {
            Scriptable scope = rhino.initStandardObjects();

            ScriptableObject.putProperty(scope, "javaContext", org.mozilla.javascript.Context.javaToJS(TestRhinoActivity.this, scope));
            ScriptableObject.putProperty(scope, "javaLoader", org.mozilla.javascript.Context.javaToJS(TestRhinoActivity.class.getClassLoader(), scope));

            rhino.evaluateString(scope, js, "MainActivity", 1, null);

            Function function = (Function) scope.get(functionName, scope);

            Object result = function.call(rhino, scope, scope, functionParams);
            if (result instanceof String) {
                return (String) result;
            } else if (result instanceof NativeJavaObject) {
                return (String) ((NativeJavaObject) result).getDefaultValue(String.class);
            } else if (result instanceof NativeObject) {
                return (String) ((NativeObject) result).getDefaultValue(String.class);
            }
            return result.toString();//(String) function.call(rhino, scope, scope, functionParams);
        } finally {
            org.mozilla.javascript.Context.exit();
        }
    }

    public static String jsCallJava(String url) {
        Logger.d(TAG, "jsCallJava", "" + url);
        return "heaven7: js call Java Rhino";
    }
}
