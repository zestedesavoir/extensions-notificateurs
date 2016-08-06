package com.zestedesavoir.zdsnotificateur.member.actions;

import com.zestedesavoir.zdsnotificateur.internal.exceptions.ZdSException;

/**
 * @author Gerard Paligot
 */
public final class ActionMemberException extends ZdSException {
  public ActionMemberException() {
  }

  public ActionMemberException(String detailMessage) {
    super(detailMessage);
  }

  public ActionMemberException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

  public ActionMemberException(Throwable throwable) {
    super(throwable);
  }
}
