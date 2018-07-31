package com.heaven7.test.baidu;

import com.google.gson.GsonBuilder;
import com.heaven7.java.visitor.MapFireVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import java.util.Map;

public class RequestBodys {

    /** cast data to json */
    public static RequestBody toFormUrlEncodeBody(String key, Object data) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add(key, new GsonBuilder().create().toJson(data));
        return builder.build();
    }

    public static RequestBody toFormUrlEncodeBody(GsonUtils.IRawData data) {
        return toFormUrlEncodeBody(GsonUtils.toMap(data));
    }

    public static RequestBody toFormUrlEncodeBody(Map<String, String> param){
        return toFormUrlEncodeBody(param, false);
    }

    public static RequestBody toFormUrlEncodeBody(Map<String, String> param, boolean urlEncode){
        FormBody.Builder builder = new FormBody.Builder();
        VisitServices.from(param).fire(new MapFireVisitor<String, String>() {
            @Override
            public Boolean visit(KeyValuePair<String, String> pair, Object param) {
                if(urlEncode){
                    builder.addEncoded(pair.getKey(), pair.getValue());
                }else {
                    builder.add(pair.getKey(), pair.getValue());
                }
                return null;
            }
        });
        return builder.build();
    }
}
