package com.zestedesavoir.android.login.dao;

import com.zestedesavoir.android.login.models.Token;

import rx.Observable;

public interface TokenDao {
    Observable<Token> save(Token token);

    Observable<Token> get();

    Observable<Void> delete();
}
