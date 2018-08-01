package com.heaven7.utils;

import com.heaven7.ve.VEContext;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * this class help we read text file as line and line .
 * Created by heaven7 on 2018/4/14 0014.
 */

public final class TextReadHelper<Line>{

    private final Callback<Line> mCallback;

    public TextReadHelper(Callback<Line> mCallback) {
        this.mCallback = mCallback;
    }

    public List<Line> read(VEContext context, String url) throws LoadException {
        List<Line> results = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = mCallback.open(context, url);
            String line;
            while((line = reader.readLine()) != null){
                Line l1 = mCallback.parse(line);
                if(l1 != null){
                    results.add(l1);
                }
            }
        } catch (IOException e) {
            throw new LoadException(e);
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return results;
    }

    public interface Callback<Line>{

        BufferedReader open(VEContext context, String url) throws IOException;

        /**
         * parse the line into a object.
         * @param line the line str
         * @return the result. can be null.
         */
        Line parse(String line);
    }

    public static abstract class BaseAssetsCallback<Line> implements Callback<Line>{
        @Override
        public BufferedReader open(VEContext context, String url) throws IOException {
            return new BufferedReader(new InputStreamReader(new FileInputStream(url)));
        }
    }


}
