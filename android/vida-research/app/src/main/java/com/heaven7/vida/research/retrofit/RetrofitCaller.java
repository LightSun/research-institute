package com.heaven7.vida.research.retrofit;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Response;

import static com.heaven7.vida.research.retrofit.RetrofitCaller.CallListener.CODE_FAILED;
import static com.heaven7.vida.research.retrofit.RetrofitCaller.CallListener.CODE_NET_ERROR;
import static com.heaven7.vida.research.retrofit.RetrofitCaller.CallListener.CODE_SUCCESS;

/**
 *
 *  the retrofit caller help we manage the
 *
 * @param <Api> the type of retrofit api.
 * @param <Data> the the data type.
 * @author heaven7
 * Created by heaven7 on 2017/3/28 0028.
 */

public final class RetrofitCaller<Api, Data> {

    private final InternalCallback<Data> mCallback = new InternalCallback<Data>();
    private Class<Api> mClazz;
    private String mBaseUrl;

    //private String[] mValues;
    private CallFactory<Api, Data> mFactory;

    private RetrofitCaller(){
    }

    /**
     * create the retrofit caller.
     * @param clazz the api class
     * @param dataClass the data class.
     * @param <Api> the type of retrofit api.
     * @param <Data> the the data type.
     * @return an instance of RetrofitCaller.
     */
    public static <Api, Data> RetrofitCaller<Api, Data> create(Class<Api> clazz, Class<Data> dataClass) {
        RetrofitCaller<Api, Data> caller = new RetrofitCaller<>();
        caller.mClazz = clazz;
        return caller;
    }

    /**
     * assigned the api class.
     * @param <Api> the type of retrofit api.
     * @param <Data> the the data type.
     * @param clazz the api class.
     * @return the retrofit caller.
     */
    public static <Api, Data>  RetrofitCaller<Api, Data> create(Class<Api> clazz){
        RetrofitCaller<Api, Data> caller = new RetrofitCaller<>();
        caller.mClazz = clazz;
        return caller;
    }

    /**
     * assign the base url.
     * @param baseUrl the base url
     * @return this.
     */
    public RetrofitCaller<Api, Data> baseUrl(String baseUrl) {
        this.mBaseUrl = baseUrl;
        return this;
    }

    /**
     * assign the call factory to create {@linkplain Call}.
     * @param factory the call factory to create {@linkplain Call}.
     * @return this
     */
    public RetrofitCaller<Api, Data> callFactory(CallFactory<Api, Data> factory) {
        this.mFactory = factory;
        return this;
    }

    /**
     * assign the data callback of retrofit.
     * @param callback the callback of retrofit.
     * @return this.
     */
    public RetrofitCaller<Api, Data> callback(AbstractRetrofitCallback<Data> callback) {
        this.mCallback.mBase = callback;
        return this;
    }
    /**
     * assign the call listener of retrofit.
     * @param l the call listener of retrofit.
     * @return this.
     */
    public RetrofitCaller<Api, Data> callListener(CallListener l) {
        this.mCallback.mListener = l;
        return this;
    }

    /**
     * call the retrofit(http/https) immediately.
     */
    public void call() {
        if (mClazz == null) {
            throw new IllegalStateException("must call from(...) first.");
        }
        Call<Result<Data>> call = mFactory.create(mClazz, mBaseUrl);
        mCallback.mWeakCall = new WeakReference<>(call);
        if(mCallback.mListener != null){
            mCallback.mListener.onStart();
        }
        call.enqueue(mCallback);
    }

    public static String codeToString(int code){
        switch (code){
            case CODE_SUCCESS:
                return "CODE_SUCCESS";

            case CODE_FAILED:
                return "CODE_FAILED";

            case CODE_NET_ERROR:
                return "CODE_NET_ERROR";
        }
        return null;
    }

    /**
     * the call listener of http/https request.
     */
    public interface CallListener{
        int CODE_SUCCESS   = 1;
        int CODE_FAILED    = 2;
        int CODE_NET_ERROR = 3;

        void onStart();
        void beforeResponse();
        void onEnd(int code);
    }

    /**
     * the call factory which create the {@link Call}
     * @param <Api> the type of retrofit api.
     * @param <Data> the the data type.
     */
    public interface CallFactory<Api, Data> {

        /**
         * create the call by target api class and extra parameter.
         * @param clazz the api clazz
         * @param baseUrl the base url. which from {@linkplain RetrofitCaller#baseUrl(String)}
         * @return the {@linkplain Call} object.
         */
        Call<Result<Data>> create(Class<Api> clazz, String baseUrl);
    }

    private static final class InternalCallback<T> extends AbstractRetrofitCallback<T> {

        WeakReference<Call<Result<T>>> mWeakCall;
        AbstractRetrofitCallback<T> mBase;
        CallListener mListener;

        @Override
        public void beforeResponse() {
            if(mListener != null){
                mListener.beforeResponse();
            }
            mBase.beforeResponse();
        }

        @Override
        public void onSuccess(T result) {
            if(mListener != null){
                mListener.onEnd(CODE_SUCCESS);
            }
            mBase.onSuccess(result);
        }

        @Override
        public void onNetworkError(Call<Result<T>> call) {
            if(mListener != null){
                mListener.onEnd(CODE_NET_ERROR);
            }
            mBase.onNetworkError(call);

        }

        @Override
        public void onFailed(Call<Result<T>> call, Response<Result<T>> response) {
            if(mListener != null){
                mListener.onEnd(CODE_FAILED);
            }
            mBase.onFailed(call, response);
        }
    }
}
