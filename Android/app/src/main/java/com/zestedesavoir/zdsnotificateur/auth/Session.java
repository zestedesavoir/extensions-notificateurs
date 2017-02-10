package com.zestedesavoir.zdsnotificateur.auth;

import com.zestedesavoir.zdsnotificateur.internal.Callback;

/**
 * Allow the authentication to Zeste de Savoir and save all tokens of the current
 * user authenticated necessary to re-authenticate him automatically.
 *
 * @author Gerard Paligot
 */
public interface Session {
  /**
   * Authenticate a user by login and password.
   *
   * @param login    User's login.
   * @param password User's password.
   * @param callback Callback with the tokens.
   */
  void authenticate(String login, String password, Callback<Token> callback);

  /**
   * Authenticate a user by its refresh token. This token is saved in intern.
   * If the refresh token isn't valid, think to redirect the user to the login
   * page.
   *
   * @param callback Callback with the new tokens.
   */
  void authenticateByToken(Callback<Token> callback);

  /**
   * Disconnect the current authenticated user.
   *
   * @param callback Callback called when the user is disconnected.
   */
  void disconnect(Callback<Void> callback);
}
