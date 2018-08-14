package com.heaven7.ve.colorgap.impl.montage;

import com.google.gson.Gson;
import com.heaven7.utils.Context;
import com.heaven7.ve.MediaResourceItem;
import com.heaven7.ve.colorgap.MediaDataLoader;
import com.vida.common.IOUtils;
import com.vida.common.entity.MediaData;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * @author heaven7
 */
public class MediaHighLightLoader implements MediaDataLoader {

    private static final Gson sGson = new Gson();

    @Override
    public Object load(Context context, MediaResourceItem item, String dataPath) {
        Reader reader = null;
        try {
            String json = IOUtils.readString(reader = new FileReader(dataPath));
            return sGson.fromJson(json, MediaData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            IOUtils.closeQuietly(reader);
        }
        return null;
    }
}
