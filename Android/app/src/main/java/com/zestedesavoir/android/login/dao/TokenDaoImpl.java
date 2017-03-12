package com.zestedesavoir.android.login.dao;

import android.content.Context;

import com.zestedesavoir.android.login.models.Builder;
import com.zestedesavoir.android.login.models.Query;
import com.zestedesavoir.android.login.models.Token;

import rx.Observable;
import rx.Subscriber;

public class TokenDaoImpl implements TokenDao {
    private final Context context;

    public TokenDaoImpl(Context context) {
        this.context = context;
    }

    @Override
    public Observable<Token> save(Token token) {
        return Observable.create(new Observable.OnSubscribe<Token>() {
            @Override
            public void call(Subscriber<? super Token> subscriber) {
                new Builder(context, token).commit();
                subscriber.onNext(token);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Token> get() {
        return Observable.create(new Observable.OnSubscribe<Token>() {
            @Override
            public void call(Subscriber<? super Token> subscriber) {
                subscriber.onNext(new Query(context).get());
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Void> delete() {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                new Builder(context).clear();
                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        });
    }
}
