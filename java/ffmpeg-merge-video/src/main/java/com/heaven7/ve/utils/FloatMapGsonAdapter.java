package com.heaven7.ve.utils;

import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Map;


/**
 * @author heaven7
 */
public class FloatMapGsonAdapter extends MapGsonAdapter<Float> {

    @Override
    protected Float readValue(JsonReader in) throws IOException{
        return Float.valueOf(in.nextString());
    }
    @Override
    protected void writeValue(JsonWriter out, Float value) throws IOException{
        out.value(value.floatValue());
    }

    /*public static void main(String[] args) {
        String json = "{\"shot_type_score\":[\n" +
                "{\"mediumShot\":1},\n" +

                "{\"mediumLongShot\":1},\n" +
                "{\"default\":1.5}\n" +
                "]}";

        ShotTypeScore score = new Gson().fromJson(json, ShotTypeScore.class);
        System.out.println(score.shot_type_score);
        System.out.println(new Gson().toJson(score));
    }

    public static class ShotTypeScore{
        @JsonAdapter(FloatMapGsonAdapter.class)
        Map<String, Float> shot_type_score;

    }*/

}
