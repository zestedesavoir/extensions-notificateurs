package com.zestedesavoir.zdsnotificateur.notifications;

import com.zestedesavoir.zdsnotificateur.auth.Session;
import com.zestedesavoir.zdsnotificateur.auth.Token;
import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.exceptions.AuthenticationException;
import com.zestedesavoir.zdsnotificateur.internal.query.QueryParameter;
import com.zestedesavoir.zdsnotificateur.notifications.queries.ListNotification;
import com.zestedesavoir.zdsnotificateur.notifications.queries.ListNotificationQueryParameter;

import java.util.List;

import javax.inject.Inject;

import static com.zestedesavoir.zdsnotificateur.internal.utils.Util.checkNotNull;

/**
 * @author Gerard Paligot
 */
public final class NotificationManagerImpl implements NotificationManager {
  private final Session session;
  private final QueryParameter<List<Notification>, ListNotificationQueryParameter> query;

  @Inject public NotificationManagerImpl(
      Session session, @ListNotification QueryParameter<List<Notification>, ListNotificationQueryParameter> query) {
    this.session = session;
    this.query = query;
  }

  @Override public void getAll(final Callback<List<Notification>> callback) {
    checkNotNull(callback, "Callback can't be null.");

    session.authenticateByToken(new Callback<Token>() {
      @Override public void success(Token token) {
        query.execute(new ListNotificationQueryParameter(token.accessToken(), 1, 50), callback);
      }

      @Override public void failure(Throwable e) {
        callback.failure(new AuthenticationException(e));
      }
    });
  }
}
