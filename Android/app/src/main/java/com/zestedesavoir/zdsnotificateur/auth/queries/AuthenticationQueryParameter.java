package com.zestedesavoir.zdsnotificateur.auth.queries;

import com.zestedesavoir.zdsnotificateur.internal.query.Parameter;

/**
 * @author Gerard Paligot
 */
public final class AuthenticationQueryParameter implements Parameter {
  public final String username;
  public final String password;

  public AuthenticationQueryParameter(String username, String password) {
    this.username = username;
    this.password = password;
  }
}
