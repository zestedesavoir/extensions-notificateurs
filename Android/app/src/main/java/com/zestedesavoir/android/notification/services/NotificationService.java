package com.zestedesavoir.android.notification.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;

import com.zestedesavoir.android.MainActivity;
import com.zestedesavoir.android.R;
import com.zestedesavoir.android.ZdSApplication;
import com.zestedesavoir.android.internal.exceptions.RetrofitException;
import com.zestedesavoir.android.internal.utils.IntentUtil;
import com.zestedesavoir.android.login.managers.Session;
import com.zestedesavoir.android.notification.managers.NotificationsManager;
import com.zestedesavoir.android.notification.models.Notification;
import com.zestedesavoir.android.notification.models.UnreadNotifications;

import java.util.List;

import javax.inject.Inject;

import rx.schedulers.Schedulers;
import timber.log.Timber;

public class NotificationService extends IntentService {
    public static final String TAG = "ZdSNotificationService";
    public static final int NOTIFICATION_ID = 42;
    private static final String ACTION_START = "ACTION_START";

    public static Intent createIntentStartNotificationService(Context context) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    @Inject
    NotificationsManager manager;

    @Inject
    Session session;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((ZdSApplication) getApplication()).getAppComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Timber.tag(TAG).i("Handle intent in NotificationService with action %s", intent.getAction());
        String action = intent.getAction();
        if (ACTION_START.equals(action)) {
            manager.getAllUnread(1)
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::generateNotification, throwable -> generateNotificationLogin((RetrofitException) throwable));
        }
    }

    private void generateNotificationLogin(RetrofitException throwable) {
        if (throwable.getKind() == RetrofitException.Kind.HTTP && throwable.getResponse().code() == 401) {
            session.disconnect()
                    .subscribeOn(Schedulers.io())
                    .subscribe(aVoid -> generateNotificationLogin(), Timber::e);
        }
    }

    private void generateNotificationLogin() {
        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.notif_not_logged))
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(this, R.color.accent))
                .setContentText(getString(R.string.notif_not_logged_description))
                .setSmallIcon(R.drawable.ic_notif_clem)
                .setContentIntent(IntentUtil.createActivityPendingIntent(this, MainActivity.class)).build());
    }

    private void generateNotification(UnreadNotifications unreadNotifications) {
        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        final List<Notification> notifications = unreadNotifications.notifications;
        if (notifications.size() == 0) {
            manager.cancel(NOTIFICATION_ID);
            return;
        }
        if (!unreadNotifications.shouldGenerateNotification) {
            return;
        }
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        if (notifications.size() == 1) {
            builder.setContentTitle(notifications.get(0).title)
                    .setAutoCancel(true)
                    .setColor(ContextCompat.getColor(this, R.color.accent))
                    .setContentText(getString(R.string.notif_author, notifications.get(0).sender.username))
                    .setSmallIcon(R.drawable.ic_notif_clem)
                    .setDeleteIntent(OperationNotificationReceiver.getCancelPendingIntent(this, notifications))
                    .setContentIntent(OperationNotificationReceiver.getOpenListPendingIntent(this, notifications));
        } else {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle(getResources().getQuantityString(R.plurals.notif_title, notifications.size(), notifications.size()));
            for (Notification notification : notifications) {
                inboxStyle.addLine(notification.title);
            }

            builder.setContentTitle(getResources().getQuantityString(R.plurals.notif_title, notifications.size(), notifications.size()))
                    .setAutoCancel(true)
                    .setColor(ContextCompat.getColor(this, R.color.accent))
                    .setContentText(getString(R.string.notif_description))
                    .setSmallIcon(R.drawable.ic_notif_clem)
                    .setDeleteIntent(OperationNotificationReceiver.getCancelPendingIntent(this, notifications))
                    .setContentIntent(OperationNotificationReceiver.getOpenListPendingIntent(this, notifications))
                    .setStyle(inboxStyle);
        }
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
