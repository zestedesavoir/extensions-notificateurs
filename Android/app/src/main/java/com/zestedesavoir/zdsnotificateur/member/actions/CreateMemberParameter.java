package com.zestedesavoir.zdsnotificateur.member.actions;

import com.zestedesavoir.zdsnotificateur.internal.action.Parameter;

/**
 * @author Gerard Paligot
 */
public final class CreateMemberParameter implements Parameter<Void> {
  public final String username;
  public final String email;
  public final String password;

  public CreateMemberParameter(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  @Override public Void getKey() {
    return null;
  }
}
