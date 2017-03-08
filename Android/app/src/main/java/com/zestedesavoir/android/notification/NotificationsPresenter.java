package com.zestedesavoir.android.notification;

import android.support.annotation.NonNull;

import com.zestedesavoir.android.internal.exceptions.RetrofitException;
import com.zestedesavoir.android.internal.ui.AbsPresenter;
import com.zestedesavoir.android.notification.managers.NotificationsManager;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

class NotificationsPresenter extends AbsPresenter implements NotificationsContracts.Presenter {
    @NonNull
    private final NotificationsContracts.View view;

    @NonNull
    private final NotificationsManager manager;

    NotificationsPresenter(@NonNull NotificationsContracts.View view, @NonNull NotificationsManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public void getNotifications(int page) {
        Timber.i("Load page: %d", page);
        view.showProgress(true);
        subscription.add(manager.getAll(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notifications -> {
                    view.showProgress(false);
                    if (page == 1) {
                        view.updateNotifications(notifications);
                    } else {
                        view.addAllNotifications(notifications);
                    }
                }, throwable -> {
                    view.showProgress(false);
                    view.showError((RetrofitException) throwable);
                })
        );
    }
}
