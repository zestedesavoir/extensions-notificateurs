package com.zestedesavoir.zdsnotificateur.internal;

import android.content.Context;

import com.zestedesavoir.zdsnotificateur.auth.Session;
import com.zestedesavoir.zdsnotificateur.member.MemberManager;
import com.zestedesavoir.zdsnotificateur.notifications.NotificationManager;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 * @author Gerard Paligot
 */
public final class ZdSLibrary {
  @Inject Session session;
  @Inject MemberManager memberManager;
  @Inject NotificationManager notificationManager;

  private static ZdSLibrary INSTANCE;

  private ZdSLibrary(Context context) {
    ObjectGraph.create(new ZdSModule(context)).inject(this);
  }

  public static synchronized ZdSLibrary get(Context context) {
    if (INSTANCE == null) {
      INSTANCE = new ZdSLibrary(context);
    }
    return INSTANCE;
  }

  public Session getSession() {
    return session;
  }

  public MemberManager getMemberManager() {
    return memberManager;
  }

  public NotificationManager getNotificationManager() {
    return notificationManager;
  }
}
