package com.zestedesavoir.android.login.models;

import android.content.Context;
import android.content.SharedPreferences;

import static com.zestedesavoir.android.login.models.Token.KEY;
import static com.zestedesavoir.android.login.models.Token.KEY_ACCESS_TOKEN;
import static com.zestedesavoir.android.login.models.Token.KEY_EXPIRES_IN;
import static com.zestedesavoir.android.login.models.Token.KEY_REFRESH_TOKEN;
import static com.zestedesavoir.android.login.models.Token.KEY_SCOPE;
import static com.zestedesavoir.android.login.models.Token.KEY_TOKEN_TYPE;

public final class Query {
    private final SharedPreferences preferences;

    public Query(Context context) {
        preferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }

    public Token get() {
        return new Token(
                preferences.getString(KEY_ACCESS_TOKEN, ""),
                preferences.getString(KEY_REFRESH_TOKEN, ""),
                preferences.getString(KEY_TOKEN_TYPE, ""),
                preferences.getInt(KEY_EXPIRES_IN, 0),
                preferences.getString(KEY_SCOPE, "")
        );
    }
}