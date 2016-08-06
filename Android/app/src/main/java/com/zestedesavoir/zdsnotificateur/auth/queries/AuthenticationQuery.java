package com.zestedesavoir.zdsnotificateur.auth.queries;

import android.content.Context;

import com.zestedesavoir.zdsnotificateur.R;
import com.zestedesavoir.zdsnotificateur.auth.Token;
import com.zestedesavoir.zdsnotificateur.auth.actions.AccessTokenAction;
import com.zestedesavoir.zdsnotificateur.auth.actions.AccessTokenParameter;
import com.zestedesavoir.zdsnotificateur.auth.database.TokenDao;
import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.ZdSInitializer;
import com.zestedesavoir.zdsnotificateur.internal.exceptions.ZdSException;
import com.zestedesavoir.zdsnotificateur.internal.query.QueryParameter;
import com.zestedesavoir.zdsnotificateur.internal.utils.NetworkConnectivity;

import javax.inject.Inject;

/**
 * @author Gerard Paligot
 */
public final class AuthenticationQuery implements QueryParameter<Token, AuthenticationQueryParameter> {
  private final Context context;
  private final TokenDao tokenDao;
  private final AccessTokenAction accessTokenAction;
  private final NetworkConnectivity networkConnectivity;

  @Inject public AuthenticationQuery(Context context, TokenDao tokenDao, AccessTokenAction accessTokenAction, NetworkConnectivity networkConnectivity) {
    this.context = context;
    this.tokenDao = tokenDao;
    this.accessTokenAction = accessTokenAction;
    this.networkConnectivity = networkConnectivity;
  }

  @Override public void execute(AuthenticationQueryParameter parameterQuery, Callback<Token> callback) {
    final Token token = tokenDao.get(0);
    if (token != null && token.hasTokens() && token.isValid()) {
      callback.success(token);
    } else if (networkConnectivity.isConnected(context)) {
      final AccessTokenParameter param = new AccessTokenParameter(
          ZdSInitializer.get().clientId(), ZdSInitializer.get().clientSecret(),
          parameterQuery.username, parameterQuery.password);
      accessTokenAction.request(param, new SaveTokenCallback(tokenDao, callback));
    } else {
      callback.failure(new ZdSException(context.getString(R.string.exception_connection)));
    }
  }
}
