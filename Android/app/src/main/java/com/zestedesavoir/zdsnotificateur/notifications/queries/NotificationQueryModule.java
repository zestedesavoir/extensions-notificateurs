package com.zestedesavoir.zdsnotificateur.notifications.queries;

import android.content.Context;

import com.zestedesavoir.zdsnotificateur.internal.query.QueryParameter;
import com.zestedesavoir.zdsnotificateur.internal.utils.NetworkConnectivity;
import com.zestedesavoir.zdsnotificateur.notifications.Notification;
import com.zestedesavoir.zdsnotificateur.notifications.actions.ListNotificationsAction;
import com.zestedesavoir.zdsnotificateur.notifications.database.NotificationDao;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Gerard Paligot
 */
@Module(library = true, complete = false)
public final class NotificationQueryModule {
  @Provides @ListNotification @Singleton QueryParameter<List<Notification>, ListNotificationQueryParameter> providesListNotificationQuery(
      Context context, NotificationDao topicDao, ListNotificationsAction listPrivateTopicsAction, NetworkConnectivity networkConnectivity) {
    return new ListNotificationQuery(context, topicDao, listPrivateTopicsAction, networkConnectivity);
  }
}
