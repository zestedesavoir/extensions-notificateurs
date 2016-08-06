package com.zestedesavoir.zdsnotificateur.auth.actions;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Retrofit;

/**
 * @author Gerard Paligot
 */
@Module(library = true, complete = false)
public final class TokenActionModule {
  @Provides @Singleton TokenService providesTokenService(Retrofit retrofit) {
    return retrofit.create(TokenService.class);
  }
}
