package com.zestedesavoir.zdsnotificateur.member.database;

import com.zestedesavoir.zdsnotificateur.internal.exceptions.ZdSException;

/**
 * @author Gerard Paligot
 */
public final class DatabaseMemberException extends ZdSException {
  public DatabaseMemberException() {
  }

  public DatabaseMemberException(String detailMessage) {
    super(detailMessage);
  }

  public DatabaseMemberException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

  public DatabaseMemberException(Throwable throwable) {
    super(throwable);
  }
}
