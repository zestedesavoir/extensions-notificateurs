package com.zestedesavoir.android.notification.services;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zestedesavoir.android.ZdSApplication;
import com.zestedesavoir.android.internal.utils.IntentUtil;
import com.zestedesavoir.android.notification.daos.StateNotificationDao;
import com.zestedesavoir.android.notification.models.Notification;
import com.zestedesavoir.android.notification.models.StateNotification;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class OperationNotificationReceiver extends BroadcastReceiver {
    private static final String ACTION_OPEN_NOTIFICATION_SERVICE = "ACTION_OPEN_NOTIFICATION_SERVICE";
    private static final String ACTION_CLOSE_NOTIFICATION_SERVICE = "ACTION_CLOSE_NOTIFICATION_SERVICE";
    private static final String KEY_NOTIFICATIONS = "NOTIFICATIONS";

    @Inject
    StateNotificationDao dao;

    public static PendingIntent getOpenListPendingIntent(Context context, List<Notification> notifications) {
        Intent intent = new Intent(context, OperationNotificationReceiver.class);
        intent.setAction(ACTION_OPEN_NOTIFICATION_SERVICE);
        intent.putParcelableArrayListExtra(KEY_NOTIFICATIONS, new ArrayList<>(notifications));
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getCancelPendingIntent(Context context, List<Notification> notifications) {
        Intent intent = new Intent(context, OperationNotificationReceiver.class);
        intent.setAction(ACTION_CLOSE_NOTIFICATION_SERVICE);
        intent.putParcelableArrayListExtra(KEY_NOTIFICATIONS, new ArrayList<>(notifications));
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ((ZdSApplication) context.getApplicationContext()).getAppComponent().inject(this);
        Timber.tag(NotificationService.TAG).i("Start OperationNotificationReceiver with action %s", intent.getAction());
        if (ACTION_OPEN_NOTIFICATION_SERVICE.equals(intent.getAction())) {
            if (intent.hasExtra(KEY_NOTIFICATIONS)) {
                updateNotificationRead(intent, StateNotification.OPENED, notifications -> {
                    if (notifications.size() == 1) {
                        context.startActivity(IntentUtil.createBrowserIntent(notifications.get(0).url));
                    } else {
                        context.startActivity(IntentUtil.createAppIntent(context));
                    }
                });
            }
        } else if (ACTION_CLOSE_NOTIFICATION_SERVICE.equals(intent.getAction())) {
            if (intent.hasExtra(KEY_NOTIFICATIONS)) {
                updateNotificationRead(intent, StateNotification.CLOSED, notifications -> {
                });
            }
        }
    }

    private void updateNotificationRead(Intent intent, StateNotification state, Action1<List<Notification>> action) {
        Observable.from(intent.<Notification>getParcelableArrayListExtra(KEY_NOTIFICATIONS))
                .doOnNext(notification -> dao.saveOrUpdate(notification.id, state, notification.pubdate))
                .toList()
                .subscribeOn(Schedulers.io())
                .subscribe(action, Timber::e);
    }
}
