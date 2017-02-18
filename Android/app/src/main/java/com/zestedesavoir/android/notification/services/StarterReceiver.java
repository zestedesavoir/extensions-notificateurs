package com.zestedesavoir.android.notification.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import timber.log.Timber;

import static android.app.AlarmManager.INTERVAL_FIFTEEN_MINUTES;
import static android.app.AlarmManager.RTC;

public class StarterReceiver extends BroadcastReceiver {
    public static Intent getStartIntent(Context context) {
        return new Intent(context, StarterReceiver.class);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        setupAlarm(context);
    }

    /**
     * Setup alarm used to make a HTTP request to ZdS and create a notification if necessary.
     *
     * @param context Application context.
     */
    public static void setupAlarm(Context context) {
        Timber.tag(NotificationService.TAG).i("Alarm setup for 15 minutes");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = NotificationEventReceiver.getStartPendingIntent(context);
        alarmManager.setRepeating(RTC, getTriggerAt(0), INTERVAL_FIFTEEN_MINUTES, alarmIntent);
    }

    /**
     * Returns time in millisecond for a given date.
     *
     * @param minute
     * @return date transformed.
     */
    private static long getTriggerAt(int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTimeInMillis();
    }
}
