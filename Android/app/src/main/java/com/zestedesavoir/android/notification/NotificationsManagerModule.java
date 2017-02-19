package com.zestedesavoir.android.notification;

import com.zestedesavoir.android.login.dao.TokenDao;
import com.zestedesavoir.android.notification.daos.StateNotificationDao;
import com.zestedesavoir.android.notification.managers.NotificationsManager;
import com.zestedesavoir.android.notification.managers.NotificationsManagerImpl;
import com.zestedesavoir.android.notification.managers.NotificationService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class NotificationsManagerModule {
    @Provides
    @Singleton
    NotificationService provideService(Retrofit retrofit) {
        return retrofit.create(NotificationService.class);
    }

    @Provides
    @Singleton
    NotificationsManager provideManager(NotificationService service, TokenDao tokenDao, StateNotificationDao notificationDao) {
        return new NotificationsManagerImpl(service, tokenDao, notificationDao);
    }
}