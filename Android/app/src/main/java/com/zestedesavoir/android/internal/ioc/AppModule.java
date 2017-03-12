package com.zestedesavoir.android.internal.ioc;

import android.content.Context;

import com.zestedesavoir.android.ZdSApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final ZdSApplication mContext;

    public AppModule(ZdSApplication context) {
        mContext = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mContext;
    }
}
