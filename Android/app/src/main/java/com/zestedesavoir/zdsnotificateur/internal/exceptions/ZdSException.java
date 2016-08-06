package com.zestedesavoir.zdsnotificateur.internal.exceptions;

/**
 * @author Gerard Paligot
 */
public class ZdSException extends RuntimeException {
  public ZdSException() {
  }

  public ZdSException(String detailMessage) {
    super(detailMessage);
  }

  public ZdSException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

  public ZdSException(Throwable throwable) {
    super(throwable);
  }
}
