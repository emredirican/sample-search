package com.kallavistudios.samplesearch.di.module;

import com.google.gson.Gson;
import com.kallavistudios.samplesearch.rest.SearchService;
import com.kallavistudios.samplesearch.rest.logging.LoggingInterceptor;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class RestModule {

    public static final String BASE_URL = "https://www.googleapis.com/";

    @Provides
    @Singleton
    public SearchService provideSearchService(OkHttpClient retrofitClient){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(retrofitClient)
                .build();
        return retrofit.create(SearchService.class);
    }

    @Provides
    @Singleton
    public OkHttpClient provideRetrofitClient(){
        OkHttpClient client = new OkHttpClient();
        List<Interceptor> interceptors = client.interceptors();
        interceptors.add(new LoggingInterceptor());
        return client;
    }
}
