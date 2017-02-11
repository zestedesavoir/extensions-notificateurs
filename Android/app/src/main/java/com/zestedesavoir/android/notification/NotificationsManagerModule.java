package com.zestedesavoir.android.notification;

import com.zestedesavoir.android.login.dao.TokenDao;
import com.zestedesavoir.android.notification.managers.NotificationManager;
import com.zestedesavoir.android.notification.managers.NotificationManagerImpl;
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
    NotificationManager provideManager(NotificationService service, TokenDao tokenDao) {
        return new NotificationManagerImpl(service, tokenDao);
    }
}