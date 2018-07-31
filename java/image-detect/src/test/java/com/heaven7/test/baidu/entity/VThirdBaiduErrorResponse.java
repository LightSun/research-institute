package com.heaven7.test.baidu.entity;

public class VThirdBaiduErrorResponse {

    Long error_code;
    String error_msg;
    Long log_id;

    public Long getError_code() {
        return error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public Long getLog_id() {
        return log_id;
    }

    @Override
    public String toString() {
        return "VThirdBaiduErrorResponse{" +
                "error_code=" + error_code +
                ", error_msg='" + error_msg + '\'' +
                ", log_id=" + log_id +
                '}';
    }
}
