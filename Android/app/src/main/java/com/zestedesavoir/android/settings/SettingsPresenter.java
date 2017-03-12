package com.zestedesavoir.android.settings;

import android.support.annotation.NonNull;

import com.zestedesavoir.android.internal.ui.AbsPresenter;
import com.zestedesavoir.android.login.managers.Session;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class SettingsPresenter extends AbsPresenter implements SettingsContracts.Presenter {
    @NonNull
    private final SettingsContracts.View view;

    @NonNull
    private final Session session;

    SettingsPresenter(@NonNull SettingsContracts.View view, @NonNull Session session) {
        this.view = view;
        this.session = session;
    }

    @Override
    public void disconnect() {
        subscription.add(session.disconnect()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> view.onDisconnected())
        );
    }
}
