package com.zestedesavoir.android.notification;

import android.support.annotation.NonNull;

import com.zestedesavoir.android.internal.ui.AbsPresenter;
import com.zestedesavoir.android.notification.managers.NotificationManager;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class NotificationsPresenter extends AbsPresenter implements NotificationsContracts.Presenter {
    @NonNull
    private final NotificationsContracts.View view;

    @NonNull
    private final NotificationManager manager;

    NotificationsPresenter(@NonNull NotificationsContracts.View view, @NonNull NotificationManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public void getNotifications(int page) {
        view.showProgress(true);
        subscription.add(manager.getAll(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notifications -> {
                    view.showProgress(false);
                    view.updateNotifications(notifications);
                }, throwable -> {
                    view.showProgress(false);
                    view.showError(throwable);
                })
        );
    }
}
