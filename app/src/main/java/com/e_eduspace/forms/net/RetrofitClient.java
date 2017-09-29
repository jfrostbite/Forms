package com.e_eduspace.forms.net;

import android.content.Context;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017-07-19.
 */

public class RetrofitClient {

    private OkHttpClient.Builder mOkBuilder;
    private Retrofit.Builder mRxBuilder;
    private Retrofit mRetrofit;

    private RetrofitClient() {
        mOkBuilder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
        ;

        mRxBuilder = new Retrofit.Builder()
                .baseUrl(NetConstant.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());

    }

    private interface RetrofitHolder {
        RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient newInstance() {
        return RetrofitHolder.INSTANCE;
    }

    public RetrofitClient init(Context context, File... files) {
        SSLUtils sslUtils = new SSLUtils(files);
        SSLSocketFactory sslSocketFactory = sslUtils.getSSLSocketFactory();
        X509TrustManager x509 = sslUtils.getX509();
        mRetrofit = mRxBuilder.client(mOkBuilder.sslSocketFactory(sslSocketFactory, x509).build()).build();
        return this;
    }

    public <T> T getService(Class<T> cls) {
        return mRetrofit.create(cls);
    }
}
