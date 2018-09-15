package com.heaven7.utils;

import com.heaven7.java.base.anno.Platform;
import com.vida.common.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;

/**
 * only used on pc.
 * @author heaven7
 */
@Platform
public class ConfigUtil {

    public static String loadResourcesAsString(String path){
        Reader reader = null;
        try {
            reader = new InputStreamReader(loadResourcesAsStream(path));
            return IOUtils.readString(reader);
        }catch (IOException e){
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeQuietly(reader);
        }
    }

    public static InputStream loadResourcesAsStream(String path){
        return ConfigUtil.class.getClassLoader().getResourceAsStream(path);
    }

    public static Properties loadResources(String path){
       // InputStream in = ClassLoader.getSystemResourceAsStream(path);
        InputStream in = loadResourcesAsStream(path); //table/wedding_map.properties
        return load(in, "load  Resources from module failed.");
    }

    private static Properties load(InputStream in, String exceptionMsg){
        Properties prop = new Properties();
        try {
            prop.load(in);
            Object value;
            for(Map.Entry<Object, Object> en: prop.entrySet()){
                value = en.getValue();
                if(value instanceof String){
                    en.setValue(((String)value).trim());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(exceptionMsg);
        }
        try {
            in.close();
        } catch (IOException e) {
        }
        return prop;
    }


}
