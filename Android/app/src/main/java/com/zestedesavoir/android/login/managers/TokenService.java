package com.zestedesavoir.android.login.managers;

import com.zestedesavoir.android.login.models.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

public interface TokenService {
    @Headers("Content-Type: application/json")
    @POST("/oauth2/token/")
    Observable<Token> accessToken(@Body TokenRequest tokenRequest);

    @Headers("Content-Type: application/json")
    @POST("/oauth2/token/")
    Call<Token> refreshToken(@Body RefreshTokenParameter refreshTokenRequest);

    final class TokenRequest {
        final String username;
        final String password;
        final String client_id;
        final String client_secret;
        final String grant_type = "password";

        public TokenRequest(String username, String password, String client_id, String client_secret) {
            this.username = username;
            this.password = password;
            this.client_id = client_id;
            this.client_secret = client_secret;
        }
    }

    final class RefreshTokenParameter {
        final String client_id;
        final String client_secret;
        final String refresh_token;
        final String grant_type = "refresh_token";

        public RefreshTokenParameter(String client_id, String client_secret, String refresh_token) {
            this.client_id = client_id;
            this.client_secret = client_secret;
            this.refresh_token = refresh_token;
        }
    }
}
