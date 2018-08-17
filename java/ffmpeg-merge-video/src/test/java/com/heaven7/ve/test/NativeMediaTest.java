package com.heaven7.ve.test;

import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.NativeMedia;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

/**
 * @author heaven7
 */
public class NativeMediaTest {

    private static final String DIR_DLL = "E:\\work\\codes\\montage_sdk";
    private static final String[] sLibs = {
            //dependent. must in order
            "gpsvc.dll",
            "IEShims.dll",
            "sysntfy.dll",

            "avutil-56.dll",
            "swresample-3.dll",
            "swscale-5.dll",
            "postproc-55.dll", // "avutil-56.dll",

            "avcodec-58.dll", //"avutil-56.dll",  "swresample-3.dll",
            "avformat-58.dll",// "avcodec-58.dll" , "avutil-56.dll",
            "avfilter-7.dll", //avcodec-58.dll, avformat-58.dll, avutil-56.dll, "postproc-55.dll", "swresample-3.dll", "swscale-5.dll",
            "avdevice-58.dll", //avcodec-58.dll, avfilter-7.dll, avformat-58.dll, avutil-56.dll

            "glew32.dll",
            "jpeg62.dll",
            "turbojpeg.dll",

            "mediasdklib.dll",
            "Win32Project2.dll",
           // "Win32Project1.dll",
    };

    static {
        VisitServices.from(Arrays.asList(sLibs)).fire(new FireVisitor<String>() {
            @Override
            public Boolean visit(String s, Object param) {
                System.out.println("start load: " + s);
                System.load(DIR_DLL + File.separator + s);
                return true;
            }
        });
    }

    @Test
    public void test1() { //com.heaven7.ve.NativeMedia
        NativeMedia media = new NativeMedia();
        media.setWriteOutPath(DIR_DLL + File.separator + "a.mp4");
        System.out.println("-------");
       /* try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

}
