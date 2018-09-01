package com.heaven7.ve.utils;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author heaven7
 */
public class StringMapGsonAdapter extends MapGsonAdapter<String> {

    @Override
    protected String readValue(JsonReader in) throws IOException {
        return in.nextString();
    }
    @Override
    protected void writeValue(JsonWriter out, String value) throws IOException {
        out.value(value);
    }
}
