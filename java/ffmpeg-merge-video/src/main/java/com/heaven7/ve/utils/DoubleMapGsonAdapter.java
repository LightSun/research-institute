package com.heaven7.ve.utils;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author heaven7
 */
public class DoubleMapGsonAdapter extends MapGsonAdapter<Double> {

    @Override
    protected Double readValue(JsonReader in) throws IOException {
        return in.nextDouble();
    }

    @Override
    protected void writeValue(JsonWriter out, Double value) throws IOException {
        out.value(value);
    }

}
