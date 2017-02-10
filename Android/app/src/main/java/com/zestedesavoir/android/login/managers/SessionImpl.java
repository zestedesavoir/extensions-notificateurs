package com.zestedesavoir.android.login.managers;

import com.zestedesavoir.android.BuildConfig;
import com.zestedesavoir.android.login.dao.TokenDao;
import com.zestedesavoir.android.login.models.Token;

import rx.Observable;

public class SessionImpl implements Session {
    private final TokenService service;
    private final TokenDao dao;

    public SessionImpl(TokenService service, TokenDao tokenDao) {
        this.service = service;
        dao = tokenDao;
    }

    @Override
    public Observable<Token> authenticate(String login, String password) {
        return this.service.accessToken(new TokenService.TokenRequest(login, password, BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET)).flatMap(dao::save);
    }

    @Override
    public Observable<Void> disconnect() {
        return dao.delete();
    }

    @Override
    public Observable<Boolean> isAuthenticated() {
        return dao.get().map(Token::hasTokens);
    }
}