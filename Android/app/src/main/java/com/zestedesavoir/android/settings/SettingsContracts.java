package com.zestedesavoir.android.settings;

import com.zestedesavoir.android.internal.ui.BasePresenter;
import com.zestedesavoir.android.internal.ui.BaseView;

interface SettingsContracts {
    interface View extends BaseView<Presenter> {
        void onDisconnected();
    }

    interface Presenter extends BasePresenter {
        void disconnect();
    }
}
