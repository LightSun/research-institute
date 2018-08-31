package com.heaven7.ve.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * map gson adapter
 * @author heaven7
 */
public abstract class MapGsonAdapter<V> extends TypeAdapter<Map<String, V>> {

    /* like
"shot_type_score":[
    {"mediumShot":1},
    {"mediumLongShot":1},
    {"default":1.5}
],
     */
    @Override
    public void write(JsonWriter out, Map<String, V> value) throws IOException {
        out.beginArray();
        VisitServices.from(value).mapPair().fire(new FireVisitor<KeyValuePair<String, V>>() {
            @Override
            public Boolean visit(KeyValuePair<String, V> pair, Object param) {
                try {
                    out.beginObject();
                    out.name(pair.getKey());
                    writeValue(out, pair.getValue());
                    out.endObject();
                }catch (IOException e){
                    throw new RuntimeException(e);
                }
                return null;
            }
        });
        out.endArray();
    }
    @Override
    public Map<String, V> read(JsonReader in) throws IOException {
        Map<String, V> map = createMap();
        in.beginArray();
        try {
            while (in.hasNext()) {
                in.beginObject();
                String name = in.nextName();
                V val = readValue(in);
                map.put(name, val);
                in.endObject();
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        in.endArray();
        return map;
    }

    /**
     * create map
     * @return the map
     */
    protected Map<String, V> createMap(){
        return new HashMap<>();
    }

    /**
     * read the value.
     * @param in the json reader
     * @return value
     */
    protected abstract V readValue(JsonReader in) throws IOException;

    /**
     * write the value
     * @param out the json writer
     * @param value the value to write
     */
    protected abstract void writeValue(JsonWriter out, V value) throws IOException;

}
