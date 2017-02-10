package com.zestedesavoir.zdsnotificateur.auth.actions;

import com.zestedesavoir.zdsnotificateur.auth.Token;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * @author Gerard Paligot
 */
public interface TokenService {
  @Headers("Content-Type: application/json")
  @POST("/oauth2/token/") Call<Token> accessToken(
      @Body TokenRequest tokenRequest
  );

  @Headers("Content-Type: application/json")
  @POST("/oauth2/token/") Call<Token> refreshToken(
      @Body RefreshTokenParameter refreshTokenRequest
  );
}
