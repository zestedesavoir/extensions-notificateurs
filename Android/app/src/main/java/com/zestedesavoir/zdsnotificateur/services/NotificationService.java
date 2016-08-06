package com.zestedesavoir.zdsnotificateur.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import com.zestedesavoir.zdsnotificateur.R;
import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.ZdSLibrary;
import com.zestedesavoir.zdsnotificateur.notifications.Notification;

import java.util.List;

import timber.log.Timber;

/**
 * @author Gerard Paligot
 */
public class NotificationService extends IntentService {
  private static final int NOTIFICATION_ID = 42;
  private static final String ACTION_START = "ACTION_START";

  public static Intent createIntentStartNotificationService(Context context) {
    Intent intent = new Intent(context, NotificationService.class);
    intent.setAction(ACTION_START);
    return intent;
  }

  public NotificationService() {
    super("NotificationService");
  }

  @Override public IBinder onBind(Intent intent) {
    return null;
  }

  @Override protected void onHandleIntent(Intent intent) {
    String action = intent.getAction();
    if (ACTION_START.equals(action)) {
      getPrivateTopics();
    }
  }

  private void getPrivateTopics() {
    ZdSLibrary.get(getApplicationContext()).getNotificationManager().getAll(new Callback<List<Notification>>() {
      @Override public void success(List<Notification> notifications) {
        generateNotification(notifications);
      }

      @Override public void failure(Throwable e) {
        Timber.e("Fatal error occurred.");
      }
    });
  }

  private void generateNotification(List<Notification> notifications) {
    if (notifications.size() == 0) {
      return;
    }
    final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
    if (notifications.size() == 1) {
      builder.setContentTitle(getString(R.string.app_name))
          .setAutoCancel(true)
          .setContentText(notifications.get(0).title)
          .setSmallIcon(R.drawable.ic_notif_clem);
    } else {
      builder.setContentTitle(getString(R.string.app_name))
          .setAutoCancel(true)
          .setColor(getResources().getColor(R.color.accent))
          .setContentText(getResources().getQuantityString(R.plurals.notif_title, notifications.size(), notifications.size()))
          .setSmallIcon(R.drawable.ic_notif_clem);
    }

    final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
    manager.notify(NOTIFICATION_ID, builder.build());
  }
}
