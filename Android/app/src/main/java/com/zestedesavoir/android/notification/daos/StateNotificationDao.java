package com.zestedesavoir.android.notification.daos;

import com.zestedesavoir.android.notification.models.Notification;
import com.zestedesavoir.android.notification.models.StateNotification;

import java.util.Date;

import rx.Observable;

public interface StateNotificationDao {
    Observable<UnreadNotification> get(Notification notification);

    void saveOrUpdate(int id, Date pubdate);

    void saveOrUpdate(int id, StateNotification state, Date pubdate);

    void closeAll();

    class UnreadNotification {
        public final Notification notification;
        public final StateNotification state;
        public final Date pubdate;

        UnreadNotification(Notification notification, StateNotification state, Date pubdate) {
            this.notification = notification;
            this.state = state;
            this.pubdate = pubdate;
        }
    }
}
