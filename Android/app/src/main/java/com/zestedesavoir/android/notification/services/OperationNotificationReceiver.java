package com.zestedesavoir.android.notification.services;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zestedesavoir.android.internal.utils.IntentUtil;
import com.zestedesavoir.android.notification.models.Notification;

import java.util.List;

import timber.log.Timber;

public class OperationNotificationReceiver extends BroadcastReceiver {
    private static final String ACTION_OPEN_NOTIFICATION_SERVICE = "ACTION_OPEN_NOTIFICATION_SERVICE";
    private static final String ACTION_CLOSE_NOTIFICATION_SERVICE = "ACTION_CLOSE_NOTIFICATION_SERVICE";
    private static final String KEY_NOTIFICATION = "NOTIFICATION";
    private static final String KEY_NOTIFICATIONS = "NOTIFICATIONS";

    public static PendingIntent getOpenPendingIntent(Context context, Notification notification) {
        Intent intent = new Intent(context, OperationNotificationReceiver.class);
        intent.setAction(ACTION_OPEN_NOTIFICATION_SERVICE);
        intent.putExtra(KEY_NOTIFICATION, notification);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getOpenListPendingIntent(Context context, List<Notification> notifications) {
        Intent intent = new Intent(context, OperationNotificationReceiver.class);
        intent.setAction(ACTION_OPEN_NOTIFICATION_SERVICE);
        intent.putExtra(KEY_NOTIFICATIONS, notifications.toArray(new Notification[0]));
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getCancelPendingIntent(Context context) {
        Intent intent = new Intent(context, OperationNotificationReceiver.class);
        intent.setAction(ACTION_CLOSE_NOTIFICATION_SERVICE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.tag(NotificationService.TAG).i("Start OperationNotificationReceiver with action %s", intent.getAction());
        if (ACTION_OPEN_NOTIFICATION_SERVICE.equals(intent.getAction())) {
            if (intent.hasExtra(KEY_NOTIFICATION)) {
                context.startActivity(IntentUtil.createBrowserIntent(intent.<Notification>getParcelableExtra(KEY_NOTIFICATION).url));
            } else if (intent.hasExtra(KEY_NOTIFICATIONS)) {
                context.startActivity(IntentUtil.createAppIntent(context));
            }
        }
    }
}
