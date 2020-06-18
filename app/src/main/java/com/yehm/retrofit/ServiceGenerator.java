package com.yehm.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.yehm.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ServiceGenerator {

    private static final String TAG = "ServiceGenerator";
    private static final String CACHE_CONTROL = "Cache-Control";
    private static ServiceGenerator serviceGenerator;

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit.Builder builderFile = new Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson));


    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(provideOkHttpClient())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }


    private static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder().readTimeout(1, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES)
                .build();
    }

    public static ServiceGenerator getInstance() {
        if (serviceGenerator == null) serviceGenerator = new ServiceGenerator();
        return serviceGenerator;
    }

    public static <S> S createServiceFile(Class<S> serviceClass) {
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(120, TimeUnit.SECONDS);
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder;
            requestBuilder = original.newBuilder()
                    .header("Content-Type", "multipart/form-data")
                    .method(original.method(), original.body());
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builderFile.client(client).build();
        return retrofit.create(serviceClass);
    }
}