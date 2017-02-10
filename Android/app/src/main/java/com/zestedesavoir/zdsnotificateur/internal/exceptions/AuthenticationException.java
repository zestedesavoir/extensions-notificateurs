package com.zestedesavoir.zdsnotificateur.internal.exceptions;

/**
 * @author Gerard Paligot
 */
public final class AuthenticationException extends ZdSException {
  public AuthenticationException() {
  }

  public AuthenticationException(String detailMessage) {
    super(detailMessage);
  }

  public AuthenticationException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

  public AuthenticationException(Throwable throwable) {
    super(throwable);
  }
}
