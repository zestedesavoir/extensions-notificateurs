package com.zestedesavoir.zdsnotificateur.auth.database;

import com.zestedesavoir.zdsnotificateur.internal.exceptions.ZdSException;

/**
 * @author Gerard Paligot
 */
public final class DatabaseTokenException extends ZdSException {
  public DatabaseTokenException() {
  }

  public DatabaseTokenException(String detailMessage) {
    super(detailMessage);
  }

  public DatabaseTokenException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

  public DatabaseTokenException(Throwable throwable) {
    super(throwable);
  }
}
