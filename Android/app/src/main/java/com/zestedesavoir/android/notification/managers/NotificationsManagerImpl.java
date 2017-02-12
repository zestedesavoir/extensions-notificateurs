package com.zestedesavoir.android.notification.managers;

import com.zestedesavoir.android.internal.exceptions.RetrofitException;
import com.zestedesavoir.android.login.dao.TokenDao;
import com.zestedesavoir.android.notification.models.ListNotification;
import com.zestedesavoir.android.notification.models.Notification;

import java.util.List;

import rx.Observable;

public class NotificationsManagerImpl implements NotificationsManager {
    private final NotificationService service;
    private final TokenDao tokenDao;

    public NotificationsManagerImpl(NotificationService service, TokenDao tokenDao) {
        this.service = service;
        this.tokenDao = tokenDao;
    }

    @Override
    public Observable<List<Notification>> getAll(int page) {
        return tokenDao.get()
                .flatMap(token -> {
                    if (token.hasTokens()) {
                        return service.list(token.token(), page, 50).map(ListNotification::getResults);
                    }
                    return Observable.error(RetrofitException.unexpectedError(new RuntimeException()));
                });
    }
}
