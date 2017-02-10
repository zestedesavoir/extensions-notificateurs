package com.zestedesavoir.zdsnotificateur.auth;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

/**
 * @author Gerard Paligot
 */
public final class Token implements Parcelable {
  @SerializedName("access_token")
  private final String accessToken;
  @SerializedName("refresh_token")
  private final String refreshToken;
  @SerializedName("token_type")
  private final String tokenType;
  private long timeGenerate;
  @SerializedName("expires_in")
  private final int expiresIn;
  private final String scope;

  public Token(String accessToken, String refreshToken, String tokenType, int expiresIn, String scope) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.tokenType = tokenType;
    this.timeGenerate = System.currentTimeMillis();
    this.expiresIn = expiresIn;
    this.scope = scope;
  }

  public Token(String accessToken, String refreshToken, String tokenType, long timeGenerate, int expiresIn, String scope) {
    this.scope = scope;
    this.expiresIn = expiresIn;
    this.timeGenerate = timeGenerate;
    this.tokenType = tokenType;
    this.refreshToken = refreshToken;
    this.accessToken = accessToken;
  }

  private final static ClassLoader CL = Token.class.getClassLoader();

  private Token(android.os.Parcel in) {
    this((String) in.readValue(CL), (String) in.readValue(CL), (String) in.readValue(CL), (Long) in.readValue(CL), (Integer) in.readValue(CL), (String) in.readValue(CL));
  }

  public boolean hasTokens() {
    return accessToken != null && !accessToken.isEmpty() && refreshToken != null && !refreshToken.isEmpty();
  }

  public boolean isValid() {
    final Calendar now = Calendar.getInstance();

    final Calendar expires = Calendar.getInstance();
    expires.setTimeInMillis(this.timeGenerate);
    expires.add(Calendar.SECOND, expiresIn);

    return now.compareTo(expires) < 0;
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

  public long timeGenerate() {
    return timeGenerate;
  }

  public void setTimeGenerate(long timeGenerate) {
    this.timeGenerate = timeGenerate;
  }

  public int expiresIn() {
    return expiresIn;
  }

  public String scope() {
    return scope;
  }

  @Override
  public String toString() {
    return "Token{"
        + "accessToken=" + accessToken + ", "
        + "refresh_token=" + refreshToken + ", "
        + "tokenType=" + tokenType + ", "
        + "timeGenerate=" + timeGenerate + ", "
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
    dest.writeValue(timeGenerate);
    dest.writeValue(expiresIn);
    dest.writeValue(scope);
  }

  @Override
  public int describeContents() {
    return 0;
  }
}
