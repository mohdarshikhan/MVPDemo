package com.mak.app.mvpdemo.dagger;

import android.util.Log;

import com.google.gson.Gson;
import com.mak.app.mvpdemo.apicalls.APIConstants;
import com.mak.app.mvpdemo.apicalls.APIServices;
import com.mak.app.mvpdemo.common.BaseApplication;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class is to declare Modules. Which used by Dagger Component
 * <p>
 * Copyright (c) 2018 <ClientName>. All rights reserved.
 * Created by mak on 20/9/18.
 */
@Module
public class AppModules {
    private BaseApplication mBaseApplication;

    public AppModules(BaseApplication baseApplication) {
        this.mBaseApplication = baseApplication;
    }

    @Provides
    @Singleton
    public Gson providesGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    @Named(APIConstants.Globals.MAIN_THREAD)
    Scheduler provideMainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Singleton
    @Named(APIConstants.Globals.NEW_THREAD)
    Scheduler provideNewThreadScheduler() {
        return Schedulers.newThread();
    }

//    @Provides
//    @Singleton
//    @Named(APIConstants.Globals.IMMEDIATE_THREAD)
//    Scheduler provideImmediateScheduler() {
//        return Schedulers.immediate();
//    }

    @Provides
    @Named(APIConstants.Globals.UNAUTHORIZED)
    @Singleton
    APIServices providesAppServiceUn() {
        final long NETWORK_TIMEOUT = 60;
        final long READ_TIMEOUT = 10;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIConstants.APIS.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        Log.d("DOMAIN", APIConstants.APIS.DOMAIN);

        return retrofit.create(APIServices.class);
    }

}
