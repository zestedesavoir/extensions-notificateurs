package com.zestedesavoir.zdsnotificateur.notifications.actions;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Retrofit;

/**
 * @author Gerard Paligot
 */
@Module(library = true, complete = false)
public final class NotificationActionModule {
  @Provides @Singleton NotificationService providesService(Retrofit retrofit) {
    return retrofit.create(NotificationService.class);
  }
}
