package com.heaven7.ve.configs;

import com.heaven7.java.base.util.IOUtils;
import com.heaven7.java.base.util.ResourceLoader;
import com.heaven7.utils.Context;
import com.heaven7.utils.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * the dictionary loader
 * @author heaven7
 */
public interface DictionaryLoader {

    DictionaryLoader Y8M_LOADER = new DictionaryLoader() {
        @Override
        public void loadDictionary(Context context, String path, Callback callback) {
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(ResourceLoader.getDefault().loadFileAsStream(context, path)));
                String line;
                while ((line = in.readLine()) != null) {
                    String[] strs = line.split(",");
                    final int index;
                    try {//guard  index
                        index = Integer.parseInt(strs[0]);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    String name = strs[3];
                    if (!TextUtils.isEmpty(name)) {
                        callback.onLoad(index, name);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(in);
            }
        }
    };
    DictionaryLoader SIMPLE_LOADER = new DictionaryLoader() {
        @Override
        public void loadDictionary(Context context, String path, Callback callback) {
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(ResourceLoader.getDefault().loadFileAsStream(context, path)));
                String line;
                int index = 0;
                while ((line = in.readLine()) != null) {
                    callback.onLoad(index, line);
                    index ++;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(in);
            }
        }
    };

    /**
     * load the Dictionary
     * @param context the context
     * @param path the path . may be relative
     * @param callback the callback
     */
    void loadDictionary(Context context, String path, Callback callback);

    /**
     * the callback of load Dictionary
     */
    interface Callback{
        /**
         * called on load one word
         * @param index the index of word
         * @param name the word string.
         */
        void onLoad(int index, String name);
    }

}
