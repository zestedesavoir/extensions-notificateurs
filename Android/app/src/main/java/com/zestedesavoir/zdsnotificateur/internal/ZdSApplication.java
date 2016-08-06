package com.zestedesavoir.zdsnotificateur.internal;

import android.app.Application;

import com.zestedesavoir.zdsnotificateur.BuildConfig;

import net.danlew.android.joda.JodaTimeAndroid;

import timber.log.Timber;

/**
 * This application is necessary to use all managers. If you want a custom <code>Application</code>
 * class, extends this one.
 *
 * @author Gerard Paligot
 */
public class ZdSApplication extends Application {
  @Override public void onCreate() {
    super.onCreate();

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }

    ZdSInitializer.get()
        .clientId(Config.clientId())
        .clientSecret(Config.clientSecret());

    JodaTimeAndroid.init(this);
  }
}
