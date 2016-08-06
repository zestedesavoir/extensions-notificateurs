package com.zestedesavoir.zdsnotificateur.auth.actions;

import android.content.Context;

import com.zestedesavoir.zdsnotificateur.R;
import com.zestedesavoir.zdsnotificateur.auth.Token;
import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.action.AbstractActionParameter;
import com.zestedesavoir.zdsnotificateur.internal.exceptions.ZdSException;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Call;
import retrofit.Response;

/**
 * @author Gerard Paligot
 */
@Singleton
public final class AccessTokenAction extends AbstractActionParameter<Token, Void, AccessTokenParameter> {
  private final Context context;
  private final TokenService service;

  @Inject public AccessTokenAction(Context context, TokenService service) {
    this.context = context;
    this.service = service;
  }

  @Override public void request(AccessTokenParameter param, final Callback<Token> callback) {
    final Call<Token> tokenCall = service.accessToken(new TokenRequest(param.username, param.password, param.clientId, param.clientSecret));
    tokenCall.enqueue(new retrofit.Callback<Token>() {
      @Override public void onResponse(Response<Token> response) {
        if (response.isSuccess()) {
          callback.success(response.body());
        } else {
          callback.failure(new ZdSException(context.getString(R.string.exception_wrong_login)));
        }
      }

      @Override public void onFailure(Throwable t) {
        callback.failure(new ZdSException(context.getString(R.string.exception_server), t));
      }
    });
  }
}
