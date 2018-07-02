package com.heaven7.vida.research.face_bd;

/**
 * 主体内容识别 bean
 * Created by heaven7 on 2018/7/2 0002.
 */
public class MainClassifyBean {

    private long log_id;
    private Result result;

    public long getLog_id() {
        return log_id;
    }
    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public Result getResult() {
        return result;
    }
    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "MainClassifyBean{" +
                "log_id=" + log_id +
                ", result=" + result +
                '}';
    }

    public static class Result{
        int width;
        int height;
        int top;
        int left;

        @Override
        public String toString() {
            return "Result{" +
                    "width=" + width +
                    ", height=" + height +
                    ", top=" + top +
                    ", left=" + left +
                    '}';
        }
    }
}
