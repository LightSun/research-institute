package com.faendir.rhino_android;


import com.heaven7.java.base.util.ResourceLoader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.commonjs.module.Require;
import org.mozilla.javascript.commonjs.module.provider.ModuleSource;
import org.mozilla.javascript.commonjs.module.provider.ModuleSourceProvider;
import org.mozilla.javascript.commonjs.module.provider.StrongCachingModuleScriptProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;

import androidx.annotation.Keep;

/**
 * Created by heaven7 on 2019/2/18.
 */
@Keep
public class RhinoContextTest {
    private android.content.Context aCtx;
    private Context context;

    public RhinoContextTest(android.content.Context context) {
        context = context.getApplicationContext();
        this.aCtx = context;
        this.context = new RhinoAndroidHelper(context).enterContext();
    }

    public void testRequire(String assetFileName){
        try {
            ScriptableObject scope = context.initStandardObjects();
            String[] names = { "print"};
            scope.defineFunctionProperties(names, RhinoContextTest.class, ScriptableObject.DONTENUM);

            final Require require = getSandboxedRequire(context, aCtx, scope);
            require.install(scope);
            context.evaluateReader(scope, getReader(assetFileName), assetFileName, 1, null);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Object testInvokeMethod(String script, String funcName){
        ScriptableObject scope = context.initStandardObjects();
        String[] names = { "print"};
        scope.defineFunctionProperties(names, RhinoContextTest.class, ScriptableObject.DONTENUM);

        final Require require = getSandboxedRequire(context, aCtx, scope);
        require.install(scope);
        context.evaluateString(scope, script, script, 1, null);
        Function function = (Function) scope.get(funcName, scope);
        return function.call(context, scope, scope, new String[]{});
    }

    public void enter(){
        this.context = new RhinoAndroidHelper(aCtx).enterContext();
    }

    public void exit(){
        Context.exit();
    }

    private static Require getSandboxedRequire(Context cx, android.content.Context aCtx,Scriptable scope) {
        return new Require(
                cx,
                scope,
                //  cx.initStandardObjects(),
                new StrongCachingModuleScriptProvider(
                        new ModuleResourceProviderImpl(aCtx)),
                null,
                null,
                true);
    }
    private Reader getReader(String name) {
        try {
            return new InputStreamReader(aCtx.getAssets().open(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Keep
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

    private static class ModuleResourceProviderImpl implements ModuleSourceProvider{

        private android.content.Context aCtx;

        public ModuleResourceProviderImpl(android.content.Context aCtx) {
            this.aCtx = aCtx;
        }

        @Override //untitled
        public ModuleSource loadSource(String moduleId, Scriptable paths, Object validator) throws IOException, URISyntaxException {
            String path = "js/" + moduleId + ".js";
            String uri = "file:///android_asset/" + path;
            String base = "file:///android_asset/js";
            InputStream in = ResourceLoader.getDefault().loadFileAsStream(aCtx, path);
            return new ModuleSource(new InputStreamReader(in), null, new URI(uri), new URI(base), validator);
        }
        @Override
        public ModuleSource loadSource(URI uri, URI baseUri, Object validator) throws IOException, URISyntaxException {
            return null;
        }
    }
}
