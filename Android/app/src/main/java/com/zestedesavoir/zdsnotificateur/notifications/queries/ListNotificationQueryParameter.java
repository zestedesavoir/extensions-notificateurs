package com.zestedesavoir.zdsnotificateur.notifications.queries;

import com.zestedesavoir.zdsnotificateur.internal.query.Parameter;

/**
 * @author Gerard Paligot
 */
public final class ListNotificationQueryParameter implements Parameter {
  public final String token;
  public final int page;
  public final int sizePage;

  public ListNotificationQueryParameter(String token, int page, int sizePage) {
    this.token = token;
    this.page = page;
    this.sizePage = sizePage;
  }
}
