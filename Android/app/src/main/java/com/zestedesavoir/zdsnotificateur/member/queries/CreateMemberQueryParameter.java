package com.zestedesavoir.zdsnotificateur.member.queries;

import com.zestedesavoir.zdsnotificateur.internal.query.Parameter;

/**
 * @author Gerard Paligot
 */
public final class CreateMemberQueryParameter implements Parameter {
  public final String username;
  public final String email;
  public final String password;

  public CreateMemberQueryParameter(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }
}
