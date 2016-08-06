package com.zestedesavoir.zdsnotificateur.auth.actions;

import com.zestedesavoir.zdsnotificateur.auth.Token;
import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.action.AbstractActionParameter;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Call;
import retrofit.Response;

/**
 * @author Gerard Paligot
 */
@Singleton
public final class RefreshTokenAction extends AbstractActionParameter<Token, Void, RefreshTokenParameter> {
  private final TokenService service;

  @Inject public RefreshTokenAction(TokenService service) {
    this.service = service;
  }

  @Override public void request(RefreshTokenParameter param, final Callback<Token> callback) {
    final Call<Token> tokenCall = service.refreshToken(param);
    tokenCall.enqueue(new retrofit.Callback<Token>() {
      @Override public void onResponse(Response<Token> response) {
        callback.success(response.body());
      }

      @Override public void onFailure(Throwable t) {
        callback.failure(t);
      }
    });
  }
}
