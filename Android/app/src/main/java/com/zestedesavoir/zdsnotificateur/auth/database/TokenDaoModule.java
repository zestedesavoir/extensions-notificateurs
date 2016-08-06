package com.zestedesavoir.zdsnotificateur.auth.database;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Gerard Paligot
 */
@Module(library = true, complete = false)
public final class TokenDaoModule {
  @Provides @Singleton TokenDao provideTokenDao(TokenDaoImpl dao) {
    return dao;
  }
}
