package com.zestedesavoir.zdsnotificateur.notifications;

import com.zestedesavoir.zdsnotificateur.auth.Session;
import com.zestedesavoir.zdsnotificateur.internal.query.QueryParameter;
import com.zestedesavoir.zdsnotificateur.notifications.actions.NotificationActionModule;
import com.zestedesavoir.zdsnotificateur.notifications.database.NotificationDaoModule;
import com.zestedesavoir.zdsnotificateur.notifications.queries.ListNotification;
import com.zestedesavoir.zdsnotificateur.notifications.queries.ListNotificationQueryParameter;
import com.zestedesavoir.zdsnotificateur.notifications.queries.NotificationQueryModule;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Gerard on 22/02/15.
 */
@Module(
    library = true,
    complete = false,
    includes = {
        NotificationDaoModule.class,
        NotificationActionModule.class,
        NotificationQueryModule.class
    }
)
public final class NotificationModule {
  @Provides @Singleton NotificationManager providesManager(
      Session session,
      @ListNotification QueryParameter<List<Notification>, ListNotificationQueryParameter> query) {
    return new NotificationManagerImpl(session, query);
  }
}
