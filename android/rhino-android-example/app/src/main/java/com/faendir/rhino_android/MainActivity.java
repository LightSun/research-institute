package com.faendir.rhino_android;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.faendir.rhino_android.utils.AssetsFileCopyUtils;
import com.faendir.rhinotest.R;
import com.heaven7.core.util.Logger;
import com.heaven7.core.util.PermissionHelper;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private final PermissionHelper mHelper = new PermissionHelper(this);
    private Context context;
    private Scriptable scope;
    private RhinoAndroidHelper rhinoAndroidHelper;

    private RhinoContextTest mRhinoTest;

    private ZipFileTest mZipTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        rhinoAndroidHelper = new RhinoAndroidHelper(this);
        context = rhinoAndroidHelper.enterContext();
        context.setOptimizationLevel(1);
        scope = new ImporterTopLevel(context);
        mRhinoTest = new RhinoContextTest(this);

        mZipTest = new ZipFileTest(this);
        mHelper.startRequestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new int[1], new PermissionHelper.ICallback() {
            @Override
            public void onRequestPermissionResult(String requestPermission, int requestCode, boolean success) {
                Logger.d(TAG, "onRequestPermissionResult", "success = " + success);
                if(success){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mZipTest.readAndInvoke();
                        }
                    }).start();
                }
            }
        });
    }

    private void toastScript(String script) {
        try {
            Object result = context.evaluateString(scope, script, "<hello_world>", 1, null);
            Toast.makeText(this, Context.toString(result), Toast.LENGTH_LONG).show();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void button(View v) {
        switch (v.getId()) {
            case R.id.button:
                toastScript(((EditText) findViewById(R.id.editText)).getText().toString());
                break;
            case R.id.button3:
                //new OptimizationComparisonTask(this, rhinoAndroidHelper).execute(-1, 0, 1);
               /* mRhinoTest.testRequire("js/untitled0.js");
                mRhinoTest.testRequire("js/test_func_class.js");*/
                String script = "var Person = require(\"untitled\");\n" +
                        "function testName()\n" +
                        "{\n" +
                        "    var a = 1;\n" +
                        "    var b = Person.test();\n" +
                        "    var c = a + b;\n" +
                        "    return c;\n" +
                        "}";
                System.out.println(getValue(mRhinoTest.testInvokeMethod(script, "testName")));
                break;
        }
    }

    private Object getValue(Object result) {
        if (result instanceof String) {
            return (String) result;
        } else if (result instanceof NativeJavaObject) {
            return (String) ((NativeJavaObject) result).getDefaultValue(String.class);
        } else if (result instanceof NativeObject) {
            return (String) ((NativeObject) result).getDefaultValue(String.class);
        }
        return result.toString();//(String) function.call(rhino, scope, scope, functionParams);
    }

}
