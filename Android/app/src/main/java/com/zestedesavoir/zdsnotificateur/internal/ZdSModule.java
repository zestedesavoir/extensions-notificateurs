package com.zestedesavoir.zdsnotificateur.internal;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zestedesavoir.zdsnotificateur.auth.SessionModule;
import com.zestedesavoir.zdsnotificateur.internal.database.DatabaseModule;
import com.zestedesavoir.zdsnotificateur.internal.utils.NetworkConnectivity;
import com.zestedesavoir.zdsnotificateur.member.MemberModule;
import com.zestedesavoir.zdsnotificateur.notifications.NotificationModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * @author Gerard Paligot
 */
@Module(
    library = true,
    includes = {
        DatabaseModule.class,
        SessionModule.class,
        MemberModule.class,
        NotificationModule.class
    },
    injects = {
        ZdSLibrary.class
    }
)
public final class ZdSModule {
  private final Context context;

  public ZdSModule(Context context) {
    this.context = context;
  }

  @Provides @Singleton NetworkConnectivity providesNetworkConnectivity() {
    return new NetworkConnectivity(context);
  }

  @Provides @Singleton Gson providesGsonConverter() {
    return new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .create();
  }

  @Provides @Singleton Retrofit providesRestAdapter(Gson gson) {
    return new Retrofit.Builder()
        .baseUrl(Config.url())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
  }

  @Provides @Singleton Context provideApplicationContext() {
    return context;
  }
}
