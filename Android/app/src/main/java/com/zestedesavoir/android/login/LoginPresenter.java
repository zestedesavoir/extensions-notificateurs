package com.zestedesavoir.android.login;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.zestedesavoir.android.internal.ui.AbsPresenter;
import com.zestedesavoir.android.login.managers.Session;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class LoginPresenter extends AbsPresenter implements LoginContracts.Presenter {
    @NonNull
    private final LoginContracts.View view;

    @NonNull
    private final Session session;

    LoginPresenter(@NonNull LoginContracts.View view, @NonNull Session session) {
        super();
        this.view = view;
        this.session = session;
    }

    @Override
    public void connect(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            view.showUsernameError();
        } else if (TextUtils.isEmpty(password)) {
            view.showPasswordError();
        } else {
            subscription.add(session.authenticate(username, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(token -> view.authenticated(), view::showServerError)
            );
        }
    }
}
