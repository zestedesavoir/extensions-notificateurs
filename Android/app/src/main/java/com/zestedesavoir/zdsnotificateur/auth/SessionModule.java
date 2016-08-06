package com.zestedesavoir.zdsnotificateur.auth;

import com.zestedesavoir.zdsnotificateur.auth.actions.TokenActionModule;
import com.zestedesavoir.zdsnotificateur.auth.database.TokenDaoModule;
import com.zestedesavoir.zdsnotificateur.auth.queries.Authentication;
import com.zestedesavoir.zdsnotificateur.auth.queries.AuthenticationQueryParameter;
import com.zestedesavoir.zdsnotificateur.auth.queries.RefreshToken;
import com.zestedesavoir.zdsnotificateur.auth.queries.TokenQueryModule;
import com.zestedesavoir.zdsnotificateur.internal.query.Query;
import com.zestedesavoir.zdsnotificateur.internal.query.QueryParameter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Gerard Paligot
 */
@Module(
    library = true,
    complete = false,
    includes = {
        TokenDaoModule.class,
        TokenQueryModule.class,
        TokenActionModule.class
    }
)
public final class SessionModule {
  @Provides @Singleton Session providesSession(
      @Authentication QueryParameter<Token, AuthenticationQueryParameter> authenticateQuery,
      @RefreshToken Query<Token> refreshTokenQuery) {
    return new SessionImpl(authenticateQuery, refreshTokenQuery);
  }
}
