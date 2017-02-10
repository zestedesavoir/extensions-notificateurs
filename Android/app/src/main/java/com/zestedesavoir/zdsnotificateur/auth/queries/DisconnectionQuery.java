package com.zestedesavoir.zdsnotificateur.auth.queries;

import com.zestedesavoir.zdsnotificateur.auth.database.TokenDao;
import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.query.Query;
import com.zestedesavoir.zdsnotificateur.member.database.AuthenticatedMemberDao;
import com.zestedesavoir.zdsnotificateur.notifications.database.NotificationDao;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Gerard Paligot
 */
@Singleton
public final class DisconnectionQuery implements Query<Void> {
  private final TokenDao tokenDao;
  private final AuthenticatedMemberDao memberDao;
  private final NotificationDao notificationDao;

  @Inject public DisconnectionQuery(TokenDao tokenDao, AuthenticatedMemberDao memberDao, NotificationDao notificationDao) {
    this.tokenDao = tokenDao;
    this.memberDao = memberDao;
    this.notificationDao = notificationDao;
  }

  @Override public void execute(Callback<Void> callback) {
    tokenDao.deleteAll();
    memberDao.deleteAll();
    notificationDao.deleteAll();
    callback.success(null);
  }
}