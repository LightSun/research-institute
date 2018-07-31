package com.heaven7.test.baidu.entity;

public class VThirdBaiduAccessToken {

        private String refresh_token;
        private Long expires_in;
        private String scope;
        private String session_key;
        private String access_token;
        private String session_secret;


        @Override
        public String toString() {
                return "ThirdBaiduAccessToken{" +
                        "refresh_token='" + refresh_token + '\'' +
                        ", expires_in=" + expires_in +
                        ", scope='" + scope + '\'' +
                        ", session_key='" + session_key + '\'' +
                        ", access_token='" + access_token + '\'' +
                        ", session_secret='" + session_secret + '\'' +
                        '}';
        }

        public String getRefresh_token() {
                return refresh_token;
        }

        public Long getExpires_in() {
                return expires_in;
        }

        public String getScope() {
                return scope;
        }

        public String getSession_key() {
                return session_key;
        }

        public String getAccess_token() {
                return access_token;
        }

        public String getSession_secret() {
                return session_secret;
        }
}
