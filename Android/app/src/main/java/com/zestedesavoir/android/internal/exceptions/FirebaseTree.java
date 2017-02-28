package com.zestedesavoir.android.internal.exceptions;

import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import timber.log.Timber;

public class FirebaseTree extends Timber.Tree {
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return;
        }

        if (t == null) {
            if (priority == Log.ERROR) {
                FirebaseCrash.log(message);
            } else {
                FirebaseCrash.logcat(priority, tag, message);
            }
        } else {
            if (message != null) {
                FirebaseCrash.logcat(priority, tag, message);
            }
            FirebaseCrash.report(t);
        }
    }
}
