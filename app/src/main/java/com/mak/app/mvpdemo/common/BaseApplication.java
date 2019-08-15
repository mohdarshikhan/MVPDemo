package com.mak.app.mvpdemo.common;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.mak.app.mvpdemo.dagger.AppComponents;
import com.mak.app.mvpdemo.dagger.AppModules;
import com.mak.app.mvpdemo.dagger.DaggerAppComponents;

/**
 * This is the Base Application, It includes things which will use in whole app or activities.
 * <p>
 * Copyright (c) 2018 <ClientName>. All rights reserved.
 * Created by mak on 20/9/18.
 */
public class BaseApplication extends MultiDexApplication {
    public static AppComponents appComponents;
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mContext = base;
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        injectDependencies(BaseApplication.this);

    }

    public static void injectDependencies(BaseApplication context) {
        appComponents = DaggerAppComponents.builder()
                .appModules(new AppModules(context))
                .build();
        appComponents.inject(context);
    }

    public static AppComponents getAppComponents() {
        return appComponents;
    }

}
