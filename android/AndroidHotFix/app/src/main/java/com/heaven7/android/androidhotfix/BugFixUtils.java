package com.heaven7.android.androidhotfix;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by ygdx_lk on 17/12/4.
 */

public class BugFixUtils {

    public static final String DEX_DIR = "odex";
    public static final String dexName = "patch.dex";
    private static final String TAG = "BugFixUtils";

    public static void fixbug(Context context){
        String patchPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + dexName;
        downLoadPatch(context, patchPath);
        loadPatch(context);
    }

    //加载补丁dex
    public static void loadPatch(Context context) {
        List<File> dexs = getPatchDexs(context);
        if(dexs != null && dexs.size() > 0){
            inject(context, dexs);
        }
    }

    //注入
    private static void inject(Context context, List<File> dexs) {
        Log.e(TAG, "inject: ");
        //dex存储目录
        File fileDir = context.getDir(DEX_DIR, Context.MODE_PRIVATE);

        //dex加载后的缓存目录
        String optimizeDir = fileDir.getAbsolutePath() + File.separator + "opt_dex";
        File optFile = new File(optimizeDir);
        if(!optFile.exists()){
            optFile.mkdirs();
        }

        //获取app的类加载器
        PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
        for (File dex : dexs) {
            Log.i(TAG, "inject: 获取dex。。。" + dex.getAbsolutePath());
            //加载修复的dex文件
            //public DexClassLoader(
            //String dexPath, dex路径
            // String optimizedDirectory, dex加载的缓存路径
            // String librarySearchPath,
            // ClassLoader parent)
            DexClassLoader dexClassLoader = new DexClassLoader(dex.getAbsolutePath(), optFile.getAbsolutePath(), null, pathClassLoader);

            try {
                //获取DexClassLoader和PathClassLoader中的DexElements
                Object dexObj = getPathList(dexClassLoader);
                Object pathObj = getPathList(pathClassLoader);

                Object dexElements = getDexElements(dexObj);
                Object pathElements = getDexElements(pathObj);

                //合并
                Object mergeElements = combineArray(dexElements, pathElements);

                //将mergeElements覆盖pathClassLoader中的elements
                setField(pathObj,"dexElements", mergeElements);
                Log.e(TAG, "inject: merge");

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    //获取dalvik.system.BaseDexClassLoader中的DexPathList pathList
    private static Object getPathList(Object baseDexClassLoader) throws IllegalArgumentException,
            NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        return getField(baseDexClassLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    private static Object getField(Object obj, Class<?> cl, String field) throws NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    //获取DexPathList中的Element[] dexElements
    private static Object getDexElements(Object paramObject) throws IllegalArgumentException,
            NoSuchFieldException, IllegalAccessException {
        return getField(paramObject, paramObject.getClass(), "dexElements");
    }

    private static void setField(Object obj, String field, Object value) throws NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        Class<?> cl = obj.getClass();
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        localField.set(obj, value);
    }

    /**
     *
     * @param arrayLhs 放到前面的数组
     * @param arrayRhs 放到后面的数组
     * @return
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Log.i(TAG, "combineArray: i" + i + "  j" + (j - i));
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

    //获取DEX_DIR下的补丁dexs
    private static List<File> getPatchDexs(Context context) {
        List<File> patchDexs = new ArrayList<>();
        File fileDir = context.getDir(DEX_DIR, Context.MODE_PRIVATE);
        if(fileDir != null && fileDir.exists()){
            File[] files = fileDir.listFiles();
            if(files != null){
                for (File file : files) {
                    if (file != null) {
                        String fileName = file.getName();
                        //判断是否是补丁dex(patch.dex)可能有多个
                        if(fileName.startsWith("patch") && fileName.endsWith(".dex")){
                            patchDexs.add(file);
                        }
                    }
                }
            }
        }
        return patchDexs;
    }

    //从服务器或本地下载补丁dex
    private static void downLoadPatch(Context context, String patchPath) {
        // /data/data/packagename/odex
        File fileDir = context.getDir(DEX_DIR, Context.MODE_PRIVATE);
        String filePath = fileDir.getAbsolutePath() + File.separator + dexName;
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
        //拷贝补丁到/data/data/packagename/odex下
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(patchPath);
            os = new FileOutputStream(file);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1){
                Log.d(TAG, "downLoadPatch: " + len);
                os.write(buffer, 0, len);
            }
            Log.i(TAG, "downLoadPatch: " + file.getAbsolutePath());
        }catch (Exception e){
            Log.e(TAG, "downLoadPatch: " + e.getMessage());
            e.printStackTrace();
        }finally {
            if(os != null){
                try {os.close();} catch (IOException e) {e.printStackTrace();}
            }

            if(is != null){
                try {is.close();} catch (IOException e) {e.printStackTrace();}
            }
        }
    }
}