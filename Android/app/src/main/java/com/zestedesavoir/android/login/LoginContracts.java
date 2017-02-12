package com.zestedesavoir.android.login;

import com.zestedesavoir.android.internal.exceptions.RetrofitException;
import com.zestedesavoir.android.internal.ui.BasePresenter;
import com.zestedesavoir.android.internal.ui.BaseView;

interface LoginContracts {
    interface View extends BaseView<Presenter> {
        void showUsernameError();

        void showPasswordError();

        void showServerError(RetrofitException throwable);

        void authenticated();
    }

    interface Presenter extends BasePresenter {
        void connect(String username, String password);
    }
}
