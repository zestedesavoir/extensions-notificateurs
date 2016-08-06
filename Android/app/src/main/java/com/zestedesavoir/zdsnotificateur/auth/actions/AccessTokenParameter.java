package com.zestedesavoir.zdsnotificateur.auth.actions;

import com.zestedesavoir.zdsnotificateur.internal.action.Parameter;

/**
 * @author Gerard Paligot
 */
public final class AccessTokenParameter implements Parameter<Void> {
  public final String clientId;
  public final String clientSecret;
  public final String username;
  public final String password;
  public final String grantType;

  public AccessTokenParameter(String clientId, String clientSecret, String username, String password) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.username = username;
    this.password = password;
    this.grantType = "password";
  }

  @Override public Void getKey() {
    return null;
  }
}
