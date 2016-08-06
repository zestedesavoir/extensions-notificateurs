package com.zestedesavoir.zdsnotificateur.auth.queries;

import com.zestedesavoir.zdsnotificateur.auth.Token;
import com.zestedesavoir.zdsnotificateur.auth.database.TokenDao;
import com.zestedesavoir.zdsnotificateur.internal.Callback;

/**
 * @author Gerard Paligot
 */
public final class SaveTokenCallback implements Callback<Token> {
  private final TokenDao tokenDao;
  private final Callback<Token> next;

  SaveTokenCallback(TokenDao tokenDao, Callback<Token> next) {
    this.tokenDao = tokenDao;
    this.next = next;
  }

  @Override public void success(Token token) {
    token.setTimeGenerate(System.currentTimeMillis());
    tokenDao.save(true, token);
    next.success(token);
  }

  @Override public void failure(Throwable e) {
    next.failure(e);
  }
}