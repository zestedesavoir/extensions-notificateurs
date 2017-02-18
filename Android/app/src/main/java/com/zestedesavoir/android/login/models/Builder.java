package com.zestedesavoir.android.login.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static com.zestedesavoir.android.login.models.Token.KEY;
import static com.zestedesavoir.android.login.models.Token.KEY_ACCESS_TOKEN;
import static com.zestedesavoir.android.login.models.Token.KEY_EXPIRES_IN;
import static com.zestedesavoir.android.login.models.Token.KEY_REFRESH_TOKEN;
import static com.zestedesavoir.android.login.models.Token.KEY_SCOPE;
import static com.zestedesavoir.android.login.models.Token.KEY_TOKEN_TYPE;

public final class Builder {
    private final SharedPreferences.Editor editor;

    public Builder(Context context) {
        this(context, null);
    }

    @SuppressLint("CommitPrefEdits")
    public Builder(Context context, Token token) {
        editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        if (token != null) {
            accessToken(token.accessToken());
            refreshToken(token.refreshToken());
            tokenType(token.tokenType());
            expiresIn(token.expiresIn());
            scope(token.scope());
        }
    }

    public Builder accessToken(String accessToken) {
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        return this;
    }

    public Builder refreshToken(String refreshToken) {
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        return this;
    }

    public Builder tokenType(String tokenType) {
        editor.putString(KEY_TOKEN_TYPE, tokenType);
        return this;
    }

    public Builder expiresIn(int expiresIn) {
        editor.putInt(KEY_EXPIRES_IN, expiresIn);
        return this;
    }

    public Builder scope(String scope) {
        editor.putString(KEY_SCOPE, scope);
        return this;
    }

    public void clear() {
        editor.clear();
    }

    public void commit() {
        editor.commit();
    }
}