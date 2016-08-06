package com.zestedesavoir.zdsnotificateur.notifications.queries;

import android.content.Context;

import com.zestedesavoir.zdsnotificateur.R;
import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.exceptions.ZdSException;
import com.zestedesavoir.zdsnotificateur.internal.query.QueryParameter;
import com.zestedesavoir.zdsnotificateur.internal.utils.NetworkConnectivity;
import com.zestedesavoir.zdsnotificateur.notifications.Notification;
import com.zestedesavoir.zdsnotificateur.notifications.actions.ListNotificationsAction;
import com.zestedesavoir.zdsnotificateur.notifications.actions.ListNotificationsParameter;
import com.zestedesavoir.zdsnotificateur.notifications.database.NotificationDao;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Gerard Paligot
 */
public final class ListNotificationQuery implements QueryParameter<List<Notification>, ListNotificationQueryParameter> {
  private final Context context;
  private final NotificationDao notificationDao;
  private final ListNotificationsAction action;
  private final NetworkConnectivity networkConnectivity;

  @Inject public ListNotificationQuery(Context context, NotificationDao notificationDao, ListNotificationsAction action, NetworkConnectivity networkConnectivity) {
    this.context = context;
    this.notificationDao = notificationDao;
    this.action = action;
    this.networkConnectivity = networkConnectivity;
  }

  @Override public void execute(final ListNotificationQueryParameter parameterQuery, final Callback<List<Notification>> callback) {
    if (networkConnectivity.isConnected(context)) {
      final ListNotificationsParameter param = new ListNotificationsParameter(parameterQuery.page, parameterQuery.sizePage);
      action.execute(parameterQuery.token, param, new Callback<List<Notification>>() {
        @Override public void success(List<Notification> messages) {
          notificationDao.save(true, messages.toArray(new Notification[messages.size()]));
          callback.success(messages);
        }

        @Override public void failure(Throwable e) {
          callback.failure(e);
        }
      });
      return;
    }
    final List<Notification> notifications = notificationDao.getAll();
    if (notifications != null && notifications.size() > 0) {
      callback.success(notifications);
    } else {
      callback.failure(new ZdSException(context.getString(R.string.exception_internal)));
    }
  }
}
