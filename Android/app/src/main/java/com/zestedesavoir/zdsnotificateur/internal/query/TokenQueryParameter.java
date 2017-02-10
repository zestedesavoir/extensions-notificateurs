package com.zestedesavoir.zdsnotificateur.internal.query;

/**
 * @author Gerard Paligot
 */
public final class TokenQueryParameter implements Parameter {
  public final String token;

  public TokenQueryParameter(String token) {
    this.token = token;
  }
}
