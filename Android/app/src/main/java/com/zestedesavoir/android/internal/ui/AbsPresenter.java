package com.zestedesavoir.android.internal.ui;

import android.support.annotation.NonNull;

import rx.subscriptions.CompositeSubscription;

public class AbsPresenter implements BasePresenter {
    @NonNull
    protected CompositeSubscription subscription;

    protected AbsPresenter() {
        subscription = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        if (subscription.isUnsubscribed()) {
            subscription = new CompositeSubscription();
        }
    }

    @Override
    public void unsubscribe() {
        subscription.clear();
    }
}
