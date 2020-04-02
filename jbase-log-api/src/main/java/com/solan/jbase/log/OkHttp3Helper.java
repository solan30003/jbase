package com.solan.jbase.log;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * @author: hyl
 * @date: 2020/3/30 16:08
 */
public class OkHttp3Helper {
    public static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(200, 5, TimeUnit.MINUTES))
                .retryOnConnectionFailure(true)
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build();
    }
}
