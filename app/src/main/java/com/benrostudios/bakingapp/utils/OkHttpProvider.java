package com.benrostudios.bakingapp.utils;

import com.benrostudios.bakingapp.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public abstract class OkHttpProvider {
    static final int CONNECT_TIMEOUT_TEN = 10;
    static final int READ_TIMEOUT_TWENTY = 20;

    private static OkHttpClient sInstance = null;

    public static OkHttpClient getOkHttpInstance() {
        if (sInstance == null) {
            sInstance = new OkHttpClient();

            OkHttpClient.Builder okHttpClientBuilder = sInstance.newBuilder();
            okHttpClientBuilder.connectTimeout(CONNECT_TIMEOUT_TEN, TimeUnit.SECONDS);
            okHttpClientBuilder.readTimeout(READ_TIMEOUT_TWENTY, TimeUnit.SECONDS);

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClientBuilder.addInterceptor(interceptor);
            }
        }
        return sInstance;
    }
}
