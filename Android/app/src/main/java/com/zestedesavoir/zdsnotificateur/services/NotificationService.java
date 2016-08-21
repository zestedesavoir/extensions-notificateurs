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
import com.zestedesavoir.zdsnotificateur.internal.exceptions.AuthenticationException;
import com.zestedesavoir.zdsnotificateur.notifications.Notification;
import com.zestedesavoir.zdsnotificateur.ui.MainActivity;
import com.zestedesavoir.zdsnotificateur.ui.auth.LoginActivity;
import com.zestedesavoir.zdsnotificateur.ui.utils.IntentUtil;

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
        if (e instanceof AuthenticationException) {
          generateNotificationLogin();
        }
        Timber.e("Fatal error occurred.");
      }
    });
  }

  private void generateNotificationLogin() {
    final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
    manager.notify(NOTIFICATION_ID, new NotificationCompat.Builder(this)
        .setContentTitle(getString(R.string.notif_not_logged))
        .setAutoCancel(true)
        .setColor(getResources().getColor(R.color.accent))
        .setContentText(getString(R.string.notif_not_logged_description))
        .setSmallIcon(R.drawable.ic_notif_clem)
        .setContentIntent(IntentUtil.createActivityIntent(this, LoginActivity.class)).build());
  }

  private void generateNotification(List<Notification> notifications) {
    final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
    if (notifications.size() == 0) {
      manager.cancel(NOTIFICATION_ID);
      return;
    }
    final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
    if (notifications.size() == 1) {
      builder.setContentTitle(notifications.get(0).title)
          .setAutoCancel(true)
          .setColor(getResources().getColor(R.color.accent))
          .setContentText(getString(R.string.notif_author, notifications.get(0).sender().username()))
          .setSmallIcon(R.drawable.ic_notif_clem)
          .setContentIntent(IntentUtil.createBrowserIntent(this, notifications.get(0)));
    } else {
      NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
      inboxStyle.setBigContentTitle(getResources().getQuantityString(R.plurals.notif_title, notifications.size(), notifications.size()));
      for (Notification notification : notifications) {
        inboxStyle.addLine(notification.title);
      }

      builder.setContentTitle(getResources().getQuantityString(R.plurals.notif_title, notifications.size(), notifications.size()))
          .setAutoCancel(true)
          .setColor(getResources().getColor(R.color.accent))
          .setContentText(getString(R.string.notif_description))
          .setSmallIcon(R.drawable.ic_notif_clem)
          .setContentIntent(IntentUtil.createActivityIntent(this, MainActivity.class))
          .setStyle(inboxStyle);
    }
    manager.notify(NOTIFICATION_ID, builder.build());
  }
}
