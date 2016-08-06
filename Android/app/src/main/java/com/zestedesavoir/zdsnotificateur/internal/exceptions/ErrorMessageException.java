package com.zestedesavoir.zdsnotificateur.internal.exceptions;

/**
 * @author Gerard Paligot
 */
public final class ErrorMessageException extends ZdSException {
  private ErrorResponse response;

  public ErrorMessageException() {
  }

  public ErrorMessageException(String detailMessage) {
    super(detailMessage);
  }

  public ErrorMessageException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

  public ErrorMessageException(Throwable throwable) {
    super(throwable);
  }

  public void setResponse(ErrorResponse response) {
    this.response = response;
  }

  public ErrorResponse getResponse() {
    return response;
  }
}
