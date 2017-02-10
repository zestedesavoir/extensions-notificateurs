package com.zestedesavoir.zdsnotificateur.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;

import static android.app.AlarmManager.*;

/**
 * @author Gerard Paligot
 */
public class StarterReceiver extends BroadcastReceiver {
  public static Intent getStartIntent(Context context) {
    return new Intent(context, StarterReceiver.class);
  }

  @Override public void onReceive(Context context, Intent intent) {
    setupAlarm(context);
  }

  /**
   * Setup alarm used to make a HTTP request to ZdS and create a notification if necessary.
   *
   * @param context Application context.
   */
  public static void setupAlarm(Context context) {
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    PendingIntent alarmIntent = NotificationEventReceiver.getStartPendingIntent(context);
    alarmManager.setRepeating(RTC, getTriggerAt(new Date()), INTERVAL_FIFTEEN_MINUTES, alarmIntent);
  }

  /**
   * Returns time in millisecond for a given date.
   *
   * @param date Date to transform in millisecond.
   * @return date transformed.
   */
  private static long getTriggerAt(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.getTimeInMillis();
  }
}
