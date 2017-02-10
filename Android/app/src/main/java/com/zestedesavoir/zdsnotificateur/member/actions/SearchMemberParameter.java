package com.zestedesavoir.zdsnotificateur.member.actions;

import com.zestedesavoir.zdsnotificateur.internal.action.Parameter;

/**
 * @author Gerard Paligot
 */
public final class SearchMemberParameter implements Parameter<String> {
  public final String search;
  public final int page;
  public final int pageSize;

  public SearchMemberParameter(String search, int page, int pageSize) {
    this.search = search;
    this.page = page;
    this.pageSize = pageSize;
  }

  @Override public String getKey() {
    return search + page;
  }
}
