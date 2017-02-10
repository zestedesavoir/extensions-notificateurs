package com.zestedesavoir.zdsnotificateur.auth.queries;

import android.content.Context;

import com.zestedesavoir.zdsnotificateur.auth.Token;
import com.zestedesavoir.zdsnotificateur.auth.actions.AccessTokenAction;
import com.zestedesavoir.zdsnotificateur.auth.actions.RefreshTokenAction;
import com.zestedesavoir.zdsnotificateur.auth.database.TokenDao;
import com.zestedesavoir.zdsnotificateur.internal.query.Query;
import com.zestedesavoir.zdsnotificateur.internal.query.QueryParameter;
import com.zestedesavoir.zdsnotificateur.internal.utils.NetworkConnectivity;
import com.zestedesavoir.zdsnotificateur.member.database.AuthenticatedMemberDao;
import com.zestedesavoir.zdsnotificateur.notifications.database.NotificationDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Gerard Paligot
 */
@Module(library = true, complete = false)
public final class TokenQueryModule {
  @Provides @Authentication @Singleton QueryParameter<Token, AuthenticationQueryParameter> providesAuthenticationQuery(
      Context context, TokenDao tokenDao, AccessTokenAction accessTokenAction,
      NetworkConnectivity networkConnectivity) {
    return new AuthenticationQuery(context, tokenDao, accessTokenAction, networkConnectivity);
  }

  @Provides @RefreshToken @Singleton Query<Token> providesRefreshTokenQuery(
      Context context, TokenDao tokenDao, RefreshTokenAction refreshTokenAction,
      NetworkConnectivity networkConnectivity) {
    return new RefreshTokenQuery(context, tokenDao, refreshTokenAction, networkConnectivity);
  }

  @Provides @Disconnect @Singleton Query<Void> providesDisconnectQuery(
      TokenDao tokenDao, AuthenticatedMemberDao memberDao, NotificationDao notificationDao) {
    return new DisconnectionQuery(tokenDao, memberDao, notificationDao);
  }
}
