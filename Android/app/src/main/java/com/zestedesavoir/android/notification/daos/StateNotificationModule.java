package com.zestedesavoir.android.notification.daos;

import com.squareup.sqlbrite.BriteDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class StateNotificationModule {
    @Provides
    @Singleton
    StateNotificationDao provideNotificationDao(BriteDatabase briteDatabase) {
        return new StateNotificationDaoImpl(briteDatabase);
    }
}