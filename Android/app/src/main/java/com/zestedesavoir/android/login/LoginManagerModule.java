package com.zestedesavoir.android.login;

import android.content.Context;

import com.zestedesavoir.android.login.dao.TokenDao;
import com.zestedesavoir.android.login.dao.TokenDaoImpl;
import com.zestedesavoir.android.login.managers.Session;
import com.zestedesavoir.android.login.managers.SessionImpl;
import com.zestedesavoir.android.login.managers.TokenService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class LoginManagerModule {
    @Provides
    @Singleton
    TokenDao provideTokenDao(Context context) {
        return new TokenDaoImpl(context);
    }

    @Provides
    @Singleton
    TokenService provideService(Retrofit retrofit) {
        return retrofit.create(TokenService.class);
    }

    @Provides
    @Singleton
    Session provideSession(TokenService service, TokenDao tokenDao) {
        return new SessionImpl(service, tokenDao);
    }
}
