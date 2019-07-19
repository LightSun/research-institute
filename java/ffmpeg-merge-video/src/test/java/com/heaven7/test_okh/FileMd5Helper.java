package com.heaven7.test_okh;

import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author heaven7
 */
public class FileMd5Helper {

    public static String getMD5Three(String path) {
        BigInteger bi = null;
        try {
            byte[] buffer = new byte[8192];
            int len;
            MessageDigest md = MessageDigest.getInstance("MD5");
            File f = new File(path);
            FileInputStream fis = new FileInputStream(f);
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            fis.close();
            byte[] b = md.digest();
            bi = new BigInteger(1, b);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
        return bi.toString(16);
    }

    public static String getMD5Three2(String content) {
        BigInteger bi = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            md.update(contentBytes, 0, contentBytes.length);
            byte[] b = md.digest();
            bi = new BigInteger(1, b);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return bi.toString(16);
    }

    public static void main(String[] args) {
       /* String dir = "D:\\Users\\Administrator\\AppData\\Local\\Temp\\media_files\\resource";
        List<String> files = FileUtils.getFiles(new File(dir), "mp4");
        String md5s = VisitServices.from(files).map(new ResultVisitor<String, String>() {
            @Override
            public String visit(String s, Object param) {
                return getMD5Three(s);
            }
        }).asListService().joinToString(",");
        System.out.println(md5s);*/
        String str = "I love this sdkdfk dgfdgkfdgk sdgfd8gg89dg 788sdf9s sdf54345343523233523135%%&&(*(()*";
        System.out.println(getMD5Three2(str));
    }
}
