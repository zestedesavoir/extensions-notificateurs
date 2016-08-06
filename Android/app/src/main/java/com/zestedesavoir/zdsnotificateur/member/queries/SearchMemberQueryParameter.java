package com.zestedesavoir.zdsnotificateur.member.queries;

import com.zestedesavoir.zdsnotificateur.internal.query.Parameter;

/**
 * @author Gerard Paligot
 */
public final class SearchMemberQueryParameter implements Parameter {
  public final String token;
  public final String search;
  public final int page;
  public final int sizePage;

  public SearchMemberQueryParameter(String token, String search, int page, int sizePage) {
    this.token = token;
    this.search = search;
    this.page = page;
    this.sizePage = sizePage;
  }
}
