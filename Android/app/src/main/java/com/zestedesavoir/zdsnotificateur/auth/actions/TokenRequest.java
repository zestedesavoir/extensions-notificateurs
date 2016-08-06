package com.zestedesavoir.zdsnotificateur.auth.actions;

/**
 * @author Gerard Paligot
 */
final class TokenRequest {
  public final String username;
  public final String password;
  public final String client_id;
  public final String client_secret;
  public final String grant_type = "password";

  public TokenRequest(String username, String password, String client_id, String client_secret) {
    this.username = username;
    this.password = password;
    this.client_id = client_id;
    this.client_secret = client_secret;
  }
}
