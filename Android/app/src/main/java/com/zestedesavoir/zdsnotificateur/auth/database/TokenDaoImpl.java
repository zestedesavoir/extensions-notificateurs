package com.zestedesavoir.zdsnotificateur.auth.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.zestedesavoir.zdsnotificateur.auth.Token;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Gerard Paligot
 */
public final class TokenDaoImpl implements TokenDao {
  public static final String TABLE = "token";

  public static final String ACCESS_TOKEN = "access_token";
  public static final String REFRESH_TOKEN = "refresh_token";
  public static final String TOKEN_TYPE = "token_type";
  public static final String TIME_GENERATION = "time_generate";
  public static final String EXPIRES_IN = "expires_in";
  public static final String SCOPE = "scope";

  private final Context context;

  @Inject public TokenDaoImpl(Context context) {
    this.context = context;
  }

  @Override public long[] save(boolean cascade, Token... model) {
    saveToken(model);
    return new long[0];
  }

  @Override public int update(boolean cascade, Token... model) {
    saveToken(model);
    return 0;
  }

  private Token saveToken(Token... model) {
    if (model == null) {
      throw new DatabaseTokenException("We cannot save a token null");
    }
    if (model.length > 1) {
      throw new DatabaseTokenException("We cannot save a list of tokens.");
    }
    Token currentToken = model[0];
    new Builder(context, currentToken).build();
    return currentToken;
  }

  @Override public List<Token> getAll() {
    return Collections.unmodifiableList(Collections.singletonList(get(0)));
  }

  @Override public Token get(int key) {
    SharedPreferences preferences = context.getSharedPreferences(TABLE, Context.MODE_PRIVATE);
    final String accessToken = preferences.getString(ACCESS_TOKEN, "");
    final String refreshToken = preferences.getString(REFRESH_TOKEN, "");
    final String tokenType = preferences.getString(TOKEN_TYPE, "");
    final long timeGenerate = preferences.getLong(TIME_GENERATION, 0);
    final int expiresIn = preferences.getInt(EXPIRES_IN, 0);
    final String scope = preferences.getString(SCOPE, "");
    return new Token(accessToken, refreshToken, tokenType, timeGenerate, expiresIn, scope);
  }

  @Override public int delete(int key) {
    context.getSharedPreferences(TABLE, Context.MODE_PRIVATE).edit().clear().commit();
    return 0;
  }

  @Override public int deleteAll() {
    return delete(0);
  }

  public static final class Builder {
    private final SharedPreferences.Editor edit;

    public Builder(Context context) {
      edit = context.getSharedPreferences(TABLE, Context.MODE_PRIVATE).edit();
    }

    public Builder(Context context, Token token) {
      this(context);
      accessToken(token.accessToken());
      refreshToken(token.refreshToken());
      tokenType(token.tokenType());
      timeGenerate(token.timeGenerate());
      expiresIn(token.expiresIn());
      scope(token.scope());
    }

    public Builder accessToken(String accessToken) {
      edit.putString(ACCESS_TOKEN, accessToken);
      return this;
    }

    public Builder refreshToken(String refreshToken) {
      edit.putString(REFRESH_TOKEN, refreshToken);
      return this;
    }

    public Builder tokenType(String tokenType) {
      edit.putString(TOKEN_TYPE, tokenType);
      return this;
    }

    public Builder timeGenerate(long timeGenerate) {
      edit.putLong(TIME_GENERATION, timeGenerate);
      return this;
    }

    public Builder expiresIn(int expiresIn) {
      edit.putInt(EXPIRES_IN, expiresIn);
      return this;
    }

    public Builder scope(String scope) {
      edit.putString(SCOPE, scope);
      return this;
    }

    public void build() {
      edit.apply();
    }
  }
}
