package com.smart.cloud.fire.retrofit;

import java.util.concurrent.TimeUnit;

import fire.cloud.smart.com.smartcloudfire.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * 网络连接客户端。。
 * on 2016/8/24.
 */
public class AppClient {
    private static final int DEFAULT_TIMEOUT = 50;
    public static Retrofit mRetrofit;


    /**
     * 初始化retrofit对象，并配置和返回对象。。
     * @param url
     * @return
     */
    public static Retrofit retrofit(String url) {
        if(mRetrofit!=null){
            mRetrofit=null;
        }
        if (mRetrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            if (BuildConfig.DEBUG) {
                // Log信息拦截器
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                //设置 Debug Log 模式
                builder.addInterceptor(loggingInterceptor);
            }
            builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            OkHttpClient okHttpClient = builder.build();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(ArbitraryResponseBodyConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return mRetrofit;
    }

}
