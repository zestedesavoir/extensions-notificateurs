package com.zestedesavoir.android.notification.daos;

import com.zestedesavoir.android.notification.models.StateNotification;

import rx.Observable;

public interface StateNotificationDao {
    Observable<StateNotification> get(int id);

    void saveOrUpdate(int id);

    void saveOrUpdate(int id, StateNotification state);
}
