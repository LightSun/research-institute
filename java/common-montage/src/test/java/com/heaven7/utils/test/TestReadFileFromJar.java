package com.heaven7.utils.test;

import com.heaven7.test.TestPath;
import com.heaven7.utils.ConfigUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author heaven7
 */
public class TestReadFileFromJar {

    public static void main(String[] args) {
        TestPath testPath = new TestPath();
        testPath.testPath();
        InputStream in = ConfigUtil.loadResourcesAsStream("resources/text.txt");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String tempString = "";
            while ((tempString = reader.readLine()) != null) {
                System.out.println("TestReadFileFromJar " + tempString);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
