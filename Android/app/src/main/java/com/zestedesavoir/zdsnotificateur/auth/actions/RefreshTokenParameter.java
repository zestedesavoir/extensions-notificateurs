package com.zestedesavoir.zdsnotificateur.auth.actions;

import com.zestedesavoir.zdsnotificateur.internal.action.Parameter;

/**
 * @author Gerard Paligot
 */
public final class RefreshTokenParameter implements Parameter<Void> {
  public final String client_id;
  public final String client_secret;
  public final String refresh_token;
  public final String grant_type = "refresh_token";

  public RefreshTokenParameter(String client_id, String client_secret, String refresh_token) {
    this.client_id = client_id;
    this.client_secret = client_secret;
    this.refresh_token = refresh_token;
  }

  @Override public Void getKey() {
    return null;
  }
}
