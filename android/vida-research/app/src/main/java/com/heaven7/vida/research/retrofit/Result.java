package com.heaven7.vida.research.retrofit;

/**
 * Created by heaven7 on 2017/3/22 0022.
 */
public class Result<T>{
    private int result_num = -1;
    private T result;

    public int getResult_num() {
        return result_num;
    }
    public void setResult_num(int result_num) {
        this.result_num = result_num;
    }
    public T getResult() {
        return result;
    }
    public void setResult(T result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return result != null;
    }

    public T getData() {
        return result;
    }
}
