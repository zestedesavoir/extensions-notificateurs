package com.zestedesavoir.zdsnotificateur.ui.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;

import com.zestedesavoir.zdsnotificateur.internal.Config;
import com.zestedesavoir.zdsnotificateur.notifications.Notification;
import com.zestedesavoir.zdsnotificateur.ui.MainActivity;

/**
 * @author Gerard Paligot
 */
public final class IntentUtil {
  public static PendingIntent createBrowserIntent(Context context, Notification notification) {
    final Intent resultIntent = new Intent(Intent.ACTION_VIEW);
    resultIntent.setData(Uri.parse(Config.url() + notification.url));
    return PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  public static PendingIntent createActivityIntent(Context context, Class<? extends Activity> activityClass) {
    final Intent resultIntent = new Intent(context, activityClass);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    stackBuilder.addParentStack(activityClass);
    stackBuilder.addNextIntent(resultIntent);
    return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  private IntentUtil() {
    throw new AssertionError("No instance.");
  }
}
