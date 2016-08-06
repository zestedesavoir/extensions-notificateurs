package com.zestedesavoir.zdsnotificateur.internal.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Gerard Paligot
 */
public final class NetworkConnectivity {
  private final ConnectivityManager systemService;

  @Inject public NetworkConnectivity(Context context) {
    systemService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  }

  public boolean isConnected(Context context) {
    NetworkInfo activeNetwork = systemService.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
  }

  public boolean isConnectedWithWifi(Context context) {
    NetworkInfo activeNetwork = systemService.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting() && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
  }

  public boolean isConnectedWithMobileData(Context context) {
    NetworkInfo activeNetwork = systemService.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting() && activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
  }
}
