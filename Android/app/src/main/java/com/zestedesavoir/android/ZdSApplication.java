package com.zestedesavoir.android;

import android.app.Application;

import com.zestedesavoir.android.internal.ioc.AppComponent;
import com.zestedesavoir.android.internal.ioc.AppModule;
import com.zestedesavoir.android.internal.ioc.DaggerAppComponent;
import com.zestedesavoir.android.internal.ioc.NetworkModule;

import net.danlew.android.joda.JodaTimeAndroid;

import timber.log.Timber;

public class ZdSApplication extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        JodaTimeAndroid.init(this);
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}