package com.zestedesavoir.android.internal.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;

import com.zestedesavoir.android.BuildConfig;
import com.zestedesavoir.android.MainActivity;

public final class IntentUtil {
    public static Intent createBrowserIntent(String url) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.BASE_URL + url));
    }

    public static Intent createAppIntent(Context context) {
        final Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static PendingIntent createActivityPendingIntent(Context context, Class<? extends Activity> activityClass) {
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
