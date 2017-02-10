package com.zestedesavoir.zdsnotificateur.notifications.actions;

import com.zestedesavoir.zdsnotificateur.internal.action.Parameter;

/**
 * @author Gerard Paligot
 */
public final class ListNotificationsParameter implements Parameter<Integer> {
  public final int page;
  public final int pageSize;

  public ListNotificationsParameter(int page, int pageSize) {
    this.page = page;
    this.pageSize = pageSize;
  }

  @Override public Integer getKey() {
    return page;
  }
}
