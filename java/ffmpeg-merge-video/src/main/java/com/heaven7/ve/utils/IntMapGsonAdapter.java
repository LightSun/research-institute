package com.heaven7.ve.utils;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author heaven7
 */
public class IntMapGsonAdapter extends MapGsonAdapter<Integer> {

    @Override
    protected Integer readValue(JsonReader in) throws IOException {
        return in.nextInt();
    }
    @Override
    protected void writeValue(JsonWriter out, Integer value) throws IOException {
        out.value(value);
    }
}
