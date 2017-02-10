package com.zestedesavoir.zdsnotificateur.ui.notifications;

/**
 * @author Gerard Paligot
 */
public enum Type {
  TOPIC("topic"), POST("post"), PRIVATE_POST("privatepost"), REACTION("contentreaction"), NONE("");

  private final String key;

  Type(String key) {
    this.key = key;
  }

  public String key() {
    return key;
  }
}
