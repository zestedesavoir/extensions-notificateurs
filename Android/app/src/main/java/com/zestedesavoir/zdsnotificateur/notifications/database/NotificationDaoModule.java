package com.zestedesavoir.zdsnotificateur.notifications.database;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Gerard Paligot
 */
@Module(library = true, complete = false)
public final class NotificationDaoModule {
  @Provides @Singleton NotificationDao providesPrivateTopicDao(NotificationDaoImpl dao) {
    return dao;
  }

  @Provides @Singleton SubscriptionDao providesPrivateMessageDao(SubscriptionDaoImpl dao) {
    return dao;
  }
}
