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

    /* like as array
"shot_type_score":[
    {"mediumShot":1},
    {"mediumLongShot":1},
    {"default":1.5}
],
    as simple:
     "shot_type_score":{
    "mediumShot":1,
    "mediumLongShot":1,
    "default":1.5
},
     */
    @Override
    public void write(final JsonWriter out, Map<String, V> value) throws IOException {
        if(asArray()) {
            out.beginArray();
            writeObjectsImpl(out, value);
            out.endArray();
        }else {
            out.beginObject();
            writeSimpleImpl(out, value);
            out.endObject();
        }
    }

    @Override
    public Map<String, V> read(JsonReader in) throws IOException {
        Map<String, V> map = createMap();
        if(asArray()) {
            in.beginArray();
            readObjectsImpl(in, map);
            in.endArray();
        }else{
            in.beginObject();
            readSimpleImpl(in, map);
            in.endObject();
        }
        return map;
    }

    private void readObjectsImpl(JsonReader in, Map<String, V> map) {
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
    }
    private void readSimpleImpl(JsonReader in, Map<String, V> map) {
        try {
            while (in.hasNext()) {
                String name = in.nextName();
                V val = readValue(in);
                map.put(name, val);
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void writeObjectsImpl(final JsonWriter out, Map<String, V> value) {
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
    }
    private void writeSimpleImpl(final JsonWriter out, Map<String, V> value) {
        VisitServices.from(value).mapPair().fire(new FireVisitor<KeyValuePair<String, V>>() {
            @Override
            public Boolean visit(KeyValuePair<String, V> pair, Object param) {
                try {
                    out.name(pair.getKey());
                    writeValue(out, pair.getValue());
                }catch (IOException e){
                    throw new RuntimeException(e);
                }
                return null;
            }
        });
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

    /**
     * make the out json type is array or object.
     * @return true if as array. default is false as object.
     */
    protected boolean asArray(){
        return true;
    }

}
