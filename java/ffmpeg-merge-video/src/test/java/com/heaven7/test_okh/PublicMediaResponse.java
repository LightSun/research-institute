package com.heaven7.test_okh;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author heaven7
 */
public class PublicMediaResponse {

    private int code;
    private String message;
    private Data data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }
    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        @SerializedName("publicMedias")
        private List<PublicMediaInfo> publicMediaInfos;

        public List<PublicMediaInfo> getPublicMediaInfos() {
            return publicMediaInfos;
        }

        public void setPublicMediaInfos(List<PublicMediaInfo> publicMediaInfos) {
            this.publicMediaInfos = publicMediaInfos;
        }
    }
}
