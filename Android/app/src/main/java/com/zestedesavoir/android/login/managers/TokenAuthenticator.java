package com.zestedesavoir.android.login.managers;

import android.content.Context;

import com.zestedesavoir.android.login.models.Builder;
import com.zestedesavoir.android.login.models.Token;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {
    private final Context context;
    private final AutoLoginManager manager;

    public TokenAuthenticator(Context context, AutoLoginManager manager) {
        this.context = context;
        this.manager = manager;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        final Token newToken = manager.authenticateByToken().execute().body();
        new Builder(context, newToken).commit();
        return response.request().newBuilder()
                .header("Authorization", newToken.accessToken())
                .build();
    }
}
