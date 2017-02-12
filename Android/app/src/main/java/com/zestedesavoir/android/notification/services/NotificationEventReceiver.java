package com.zestedesavoir.android.notification.services;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationEventReceiver extends BroadcastReceiver {
    private static final String ACTION_START_NOTIFICATION_SERVICE = "ACTION_START_NOTIFICATION_SERVICE";

    /**
     * Builds an intent to launch {@link NotificationEventReceiver}.
     *
     * @param context Application context.
     * @return {@link PendingIntent} which launch {@link NotificationEventReceiver}.
     */
    public static PendingIntent getStartPendingIntent(Context context) {
        Intent intent = new Intent(context, NotificationEventReceiver.class);
        intent.setAction(ACTION_START_NOTIFICATION_SERVICE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_START_NOTIFICATION_SERVICE.equals(intent.getAction())) {
            context.startService(NotificationService.createIntentStartNotificationService(context));
        }
    }
}
