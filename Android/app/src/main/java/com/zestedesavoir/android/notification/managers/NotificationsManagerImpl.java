package com.zestedesavoir.android.notification.managers;

import com.zestedesavoir.android.internal.exceptions.RetrofitException;
import com.zestedesavoir.android.login.dao.TokenDao;
import com.zestedesavoir.android.notification.daos.StateNotificationDao;
import com.zestedesavoir.android.notification.models.ListNotification;
import com.zestedesavoir.android.notification.models.Notification;
import com.zestedesavoir.android.notification.models.StateNotification;
import com.zestedesavoir.android.notification.models.UnreadNotifications;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

public class NotificationsManagerImpl implements NotificationsManager {
    private final NotificationService service;
    private final TokenDao tokenDao;
    private final StateNotificationDao notificationDao;

    public NotificationsManagerImpl(NotificationService service, TokenDao tokenDao, StateNotificationDao notificationDao) {
        this.service = service;
        this.tokenDao = tokenDao;
        this.notificationDao = notificationDao;
    }

    @Override
    public Observable<List<Notification>> getAll(int page) {
        return tokenDao.get()
                .flatMap(token -> {
                    if (token.hasTokens()) {
                        return service.list(token.token(), page, 50).map(ListNotification::getResults);
                    }
                    return Observable.error(RetrofitException.noTokenError());
                });
    }

    @Override
    public Observable<UnreadNotifications> getAllUnread(int page) {
        return getAll(page)
                .flatMapIterable(notifications -> notifications)
                .filter(notification -> !notification.isRead)
                .flatMap(notificationDao::get)
                .filter(unread -> unread.state == StateNotification.NOT_EXIST
                        || unread.state == StateNotification.GENERATED
                        || unread.notification.pubdate.compareTo(unread.pubdate) > 0)
                .doOnNext(unread -> {
                    Timber.i("Unread state found: %s", unread.state.name());
                    if (unread.state != StateNotification.GENERATED) {
                        notificationDao.saveOrUpdate(unread.notification.id, unread.notification.pubdate);
                    }
                })
                .toList()
                .map(notifications -> {
                    final List<Notification> unreads = new ArrayList<>();
                    boolean shouldGenerateNotif = false;
                    for (StateNotificationDao.UnreadNotification unread : notifications) {
                        if (!shouldGenerateNotif && unread.state != StateNotification.GENERATED) {
                            shouldGenerateNotif = true;
                        }
                        unreads.add(unread.notification);
                    }
                    return new UnreadNotifications(unreads, shouldGenerateNotif);
                });
    }

    @Override
    public Observable<Void> clear() {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                notificationDao.closeAll();
                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        });
    }
}
