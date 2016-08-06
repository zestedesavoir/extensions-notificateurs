package com.zestedesavoir.zdsnotificateur.notifications.actions;

import android.content.Context;

import com.zestedesavoir.zdsnotificateur.R;
import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.action.AbstractActionParameter;
import com.zestedesavoir.zdsnotificateur.internal.exceptions.ZdSException;
import com.zestedesavoir.zdsnotificateur.notifications.ListNotification;
import com.zestedesavoir.zdsnotificateur.notifications.Notification;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Call;
import retrofit.Response;

/**
 * @author Gerard Paligot
 */
@Singleton
public final class ListNotificationsAction extends AbstractActionParameter<List<Notification>, Integer, ListNotificationsParameter> {
  private final Context context;
  private final NotificationService service;

  @Inject public ListNotificationsAction(Context context, NotificationService service) {
    this.context = context;
    this.service = service;
  }

  @Override protected void request(ListNotificationsParameter param, Callback<List<Notification>> callback) {
    throw new ZdSException("Request to get all notifications of a member must be a request authenticated.");
  }

  @Override protected void requestAuthenticated(String token, final ListNotificationsParameter param, final Callback<List<Notification>> callback) {
    final Call<ListNotification> list = service.list("Bearer " + token, param.page, param.pageSize);
    list.enqueue(new retrofit.Callback<ListNotification>() {
      @Override public void onResponse(Response<ListNotification> response) {
        if (response.isSuccess()) {
          callback.success(response.body().results);
        } else {
          callback.failure(new ZdSException(context.getString(R.string.exception_internal)));
        }
      }

      @Override public void onFailure(Throwable t) {
        callback.failure(new ZdSException(context.getString(R.string.exception_server), t));
      }
    });
  }
}
