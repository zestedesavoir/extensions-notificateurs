package com.zestedesavoir.zdsnotificateur.internal;

import static com.zestedesavoir.zdsnotificateur.internal.utils.Util.checkArgument;

/**
 * @author Gerard Paligot
 */
public final class ZdSInitializer {

  private static final ZdSInitializer INSTANCE = new ZdSInitializer();

  private String clientId;
  private String clientSecret;

  private ZdSInitializer() {
  }

  public static ZdSInitializer get() {
    return INSTANCE;
  }

  public String clientId() {
    checkArgument(clientId != null && !clientId.isEmpty(), "You must specify your client id..");
    return clientId;
  }

  public ZdSInitializer clientId(String clientId) {
    this.clientId = clientId;
    return INSTANCE;
  }

  public String clientSecret() {
    checkArgument(clientSecret != null && !clientSecret.isEmpty(), "You must specify your client secret.");
    return clientSecret;
  }

  public ZdSInitializer clientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
    return INSTANCE;
  }
}
