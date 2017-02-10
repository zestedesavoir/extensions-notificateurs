package com.zestedesavoir.zdsnotificateur.auth;


import com.zestedesavoir.zdsnotificateur.auth.queries.Authentication;
import com.zestedesavoir.zdsnotificateur.auth.queries.AuthenticationQueryParameter;
import com.zestedesavoir.zdsnotificateur.auth.queries.Disconnect;
import com.zestedesavoir.zdsnotificateur.auth.queries.RefreshToken;
import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.exceptions.AuthenticationException;
import com.zestedesavoir.zdsnotificateur.internal.query.Query;
import com.zestedesavoir.zdsnotificateur.internal.query.QueryParameter;

import javax.inject.Inject;

import static com.zestedesavoir.zdsnotificateur.internal.utils.Util.checkArgument;
import static com.zestedesavoir.zdsnotificateur.internal.utils.Util.checkNotNull;

/**
 * @author Gerard Paligot
 */
public final class SessionImpl implements Session {
  private final QueryParameter<Token, AuthenticationQueryParameter> authenticateQuery;
  private final Query<Token> refreshTokenQuery;
  private final Query<Void> disconnectQuery;

  @Inject public SessionImpl(
      @Authentication QueryParameter<Token, AuthenticationQueryParameter> authenticateQuery,
      @RefreshToken Query<Token> refreshTokenQuery,
      @Disconnect Query<Void> disconnectQuery) {
    this.authenticateQuery = authenticateQuery;
    this.refreshTokenQuery = refreshTokenQuery;
    this.disconnectQuery = disconnectQuery;
  }

  @Override public void authenticate(String login, String password, final Callback<Token> callback) {
    checkArgument(login != null && !login.isEmpty(), "Login can't be null or empty.");
    checkArgument(password != null && !password.isEmpty(), "Password can't be null or empty.");
    checkNotNull(callback, "Callback can't be null.");

    authenticateQuery.execute(new AuthenticationQueryParameter(login, password), new Callback<Token>() {
      @Override public void success(final Token token) {
        callback.success(token);
      }

      @Override public void failure(Throwable e) {
        callback.failure(new AuthenticationException(e));
      }
    });
  }

  @Override public void authenticateByToken(Callback<Token> callback) {
    checkNotNull(callback, "Callback can't be null.");

    refreshTokenQuery.execute(callback);
  }

  @Override public void disconnect(Callback<Void> callback) {
    disconnectQuery.execute(callback);
  }
}
