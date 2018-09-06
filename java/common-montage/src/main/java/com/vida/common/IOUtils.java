package com.vida.common;

import java.io.*;

public class IOUtils {
    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static String readFileAsString(String path){
        Reader reader = null;
        try {
            reader = new FileReader(path);
            return readString(reader);
        }catch (IOException e){
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeQuietly(reader);
        }
    }

    public static String readString(Reader r) throws IOException {
        BufferedReader br = r instanceof BufferedReader ? (BufferedReader) r : new BufferedReader(r);

        String str;
        StringBuilder sb = new StringBuilder();
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        return sb.toString();
    }

    public static String readStringThenClose(Reader r) throws IOException {
        BufferedReader br = r instanceof BufferedReader ? (BufferedReader) r : new BufferedReader(r);
        try{
            String str;
            StringBuilder sb = new StringBuilder();
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        }finally{
            closeQuietly(br);
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                // Ignore.
            }
        }
    }
    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
