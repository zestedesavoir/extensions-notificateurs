package com.zestedesavoir.zdsnotificateur.internal;

import com.zestedesavoir.zdsnotificateur.BuildConfig;

/**
 * Created by gerard on 30/11/2015.
 */
public enum Config {
  ;

  public static String url() {
    return "https://beta.zestedesavoir.com";
  }

  public static String clientId() {
    return BuildConfig.CLIENT_ID;
  }

  public static String clientSecret() {
    return BuildConfig.CLIENT_SECRET;
  }
}