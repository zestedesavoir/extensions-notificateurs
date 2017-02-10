package com.zestedesavoir.android.login.models;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public final class Token implements Parcelable {
    public static final String KEY = "token";
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_REFRESH_TOKEN = "refresh_token";
    public static final String KEY_TOKEN_TYPE = "token_type";
    public static final String KEY_EXPIRES_IN = "expires_in";
    public static final String KEY_SCOPE = "scope";

    @SerializedName("access_token")
    private final String accessToken;
    @SerializedName("refresh_token")
    private final String refreshToken;
    @SerializedName("token_type")
    private final String tokenType;
    @SerializedName("expires_in")
    private final int expiresIn;
    @SerializedName("scope")
    private final String scope;

    public Token(String accessToken, String refreshToken, String tokenType, int expiresIn, String scope) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.scope = scope;
    }

    public Token(String accessToken, String refreshToken, String tokenType, long timeGenerate, int expiresIn, String scope) {
        this.scope = scope;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    private final static ClassLoader CL = Token.class.getClassLoader();

    private Token(android.os.Parcel in) {
        this((String) in.readValue(CL), (String) in.readValue(CL), (String) in.readValue(CL), (Long) in.readValue(CL), (Integer) in.readValue(CL), (String) in.readValue(CL));
    }

    public String accessToken() {
        return accessToken;
    }

    public String refreshToken() {
        return refreshToken;
    }

    public String tokenType() {
        return tokenType;
    }

    public int expiresIn() {
        return expiresIn;
    }

    public String scope() {
        return scope;
    }

    public boolean hasTokens() {
        return accessToken != null && !accessToken.isEmpty() && refreshToken != null && !refreshToken.isEmpty();
    }

    @Override
    public String toString() {
        return "Token{"
                + "accessToken=" + accessToken + ", "
                + "refresh_token=" + refreshToken + ", "
                + "tokenType=" + tokenType + ", "
                + "expiresIn=" + expiresIn + ", "
                + "scope=" + scope
                + "}";
    }

    public static final Creator<Token> CREATOR = new Creator<Token>() {
        @Override
        public Token createFromParcel(android.os.Parcel in) {
            return new Token(in);
        }

        @Override
        public Token[] newArray(int size) {
            return new Token[size];
        }
    };

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(accessToken);
        dest.writeValue(refreshToken);
        dest.writeValue(tokenType);
        dest.writeValue(expiresIn);
        dest.writeValue(scope);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
