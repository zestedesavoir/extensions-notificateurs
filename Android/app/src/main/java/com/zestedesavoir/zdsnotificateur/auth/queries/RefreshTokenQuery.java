package com.zestedesavoir.zdsnotificateur.auth.queries;

import android.content.Context;

import com.zestedesavoir.zdsnotificateur.auth.Token;
import com.zestedesavoir.zdsnotificateur.auth.actions.RefreshTokenAction;
import com.zestedesavoir.zdsnotificateur.auth.actions.RefreshTokenParameter;
import com.zestedesavoir.zdsnotificateur.auth.database.TokenDao;
import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.ZdSInitializer;
import com.zestedesavoir.zdsnotificateur.internal.exceptions.ZdSException;
import com.zestedesavoir.zdsnotificateur.internal.query.Query;
import com.zestedesavoir.zdsnotificateur.internal.utils.NetworkConnectivity;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Gerard Paligot
 */
@Singleton
public final class RefreshTokenQuery implements Query<Token> {
  private final Context context;
  private final TokenDao tokenDao;
  private final RefreshTokenAction refreshTokenAction;
  private final NetworkConnectivity networkConnectivity;

  @Inject public RefreshTokenQuery(Context context, TokenDao tokenDao, RefreshTokenAction refreshTokenAction, NetworkConnectivity networkConnectivity) {
    this.context = context;
    this.tokenDao = tokenDao;
    this.refreshTokenAction = refreshTokenAction;
    this.networkConnectivity = networkConnectivity;
  }

  @Override public void execute(Callback<Token> callback) {
    final Token token = tokenDao.get(0);
    if (token == null) {
      callback.failure(new ZdSException("You must have token saved in system to refresh it."));
    } else if (token.hasTokens() && token.isValid()) {
      callback.success(token);
    } else if (token.hasTokens() && !token.isValid() && networkConnectivity.isConnected(context)) {
      final RefreshTokenParameter param = new RefreshTokenParameter(ZdSInitializer.get().clientId(), ZdSInitializer.get().clientSecret(), token.refreshToken());
      refreshTokenAction.request(param, new SaveTokenCallback(tokenDao, callback));
    } else {
      callback.failure(new ZdSException("You must be connected to log in."));
    }
  }
}
