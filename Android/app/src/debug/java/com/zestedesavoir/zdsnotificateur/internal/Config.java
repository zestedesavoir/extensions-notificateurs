package com.zestedesavoir.zdsnotificateur.internal;

/**
 * @author Gerard Paligot
 */
public enum Config {
  ;

  public static String url() {
    return "http://192.168.0.11:80";
  }

  public static String clientId() {
    return "CLIENT_ID";
  }

  public static String clientSecret() {
    return "CLIENT_SECRET";
  }
}