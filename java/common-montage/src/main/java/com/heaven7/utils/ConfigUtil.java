package com.heaven7.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class ConfigUtil {

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
