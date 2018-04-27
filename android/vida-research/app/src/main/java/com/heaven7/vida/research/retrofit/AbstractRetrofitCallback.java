package com.heaven7.vida.research.retrofit;

import com.heaven7.core.util.Logger;

import retrofit2.Call;
import retrofit2.Response;

/**
 * the callback of retrofit
 * Created by heaven7 on 2017/3/27 0027.
 */

public abstract class AbstractRetrofitCallback<T> implements retrofit2.Callback<Result<T>> {

    @Override
    public final void onFailure(Call<Result<T>> call, Throwable t) {
        beforeResponse();
        Logger.w("AbstractRetrofitCallback", "onFailure", "url = " + call.request().url()
                + " , exception = " + Logger.toString(t));
        onNetworkError(call);
    }


    @Override
    public final void onResponse(Call<Result<T>> call, Response<Result<T>> response) {
        beforeResponse();
        Result<T> body = response.body();
        if (body != null && body.isSuccess()) {
            Logger.i("AbstractRetrofitCallback", "onResponse", "success. url = " + call.request().url());
            onSuccess(body.getData());
        }else{
            Logger.w("AbstractRetrofitCallback", "onResponse", "failed. url = "+ call.request().url());
            onFailed(call, response);
        }
    }

    /**
     * called no matter failed or success.
     *  called internal or wrapper.
     */
    public void beforeResponse() {

    }

    /**
     * called on network error.
     *  called internal or wrapper.
     * @param call the call
     */
    public void onNetworkError(Call<Result<T>> call) {

    }


    /**
     * called when response failed. that means the result !=0.
     *  called internal or wrapper.
     * @param call
     * @param response the response
     */
    public void onFailed(Call<Result<T>> call, Response<Result<T>> response){

    }

    /**
     * called when response success. that means the result = 0.
     *  called internal or wrapper.
     * @param result the result, can be null
     */
    public abstract void onSuccess(T result);
}
