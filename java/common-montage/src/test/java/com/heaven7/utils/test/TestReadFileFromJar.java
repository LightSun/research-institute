package com.heaven7.utils.test;

import com.heaven7.java.base.util.ResourceLoader;
import com.heaven7.test.TestPath;

/**
 * @author heaven7
 */
public class TestReadFileFromJar {

    public static void main(String[] args) {
        TestPath testPath = new TestPath();
        testPath.testPath();
        String s = ResourceLoader.getDefault().loadFileAsString(null, "resources/text.txt");
        System.out.println("TestReadFileFromJar " + s);
    }
}
