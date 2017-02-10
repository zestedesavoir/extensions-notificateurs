package com.zestedesavoir.android.login;

import com.zestedesavoir.android.internal.ui.BasePresenter;
import com.zestedesavoir.android.internal.ui.BaseView;

interface LoginContracts {
    interface View extends BaseView<Presenter> {
        void showUsernameError();

        void showPasswordError();

        void showServerError(Throwable throwable);

        void authenticated();
    }

    interface Presenter extends BasePresenter {
        void connect(String username, String password);
    }
}
