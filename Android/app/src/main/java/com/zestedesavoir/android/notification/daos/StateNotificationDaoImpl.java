package com.zestedesavoir.android.notification.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;
import com.zestedesavoir.android.internal.db.DatabaseUtil;
import com.zestedesavoir.android.notification.models.Notification;
import com.zestedesavoir.android.notification.models.StateNotification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;

class StateNotificationDaoImpl implements StateNotificationDao {
    private final BriteDatabase database;

    StateNotificationDaoImpl(BriteDatabase database) {
        this.database = database;
    }

    @Override
    public Observable<UnreadNotification> get(Notification notification) {
        return Observable.create(subscriber -> {
            StateNotification item = StateNotification.NOT_EXIST;
            Date pubdate = new Date();
            final Cursor cursor = database.query(Query.QUERY_ONE, String.valueOf(notification.id));
            try {
                if (cursor.moveToNext()) {
                    item = StateNotification.valueOf(DatabaseUtil.getString(cursor, Query.STATE));
                    pubdate = DatabaseUtil.getDate(cursor, Query.PUBDATE);
                }
            } finally {
                cursor.close();
            }
            if (!subscriber.isUnsubscribed()) {
                subscriber.onNext(new UnreadNotification(notification, item, pubdate));
            }
            subscriber.onCompleted();
        });
    }

    @Override
    public void saveOrUpdate(int id, Date pubdate) {
        saveOrUpdate(id, StateNotification.GENERATED, pubdate);
    }

    @Override
    public void saveOrUpdate(int id, StateNotification state, Date pubdate) {
        final ContentValues values = new ContentValues();
        values.put(Query.ID, id);
        values.put(Query.STATE, state.name());
        values.put(Query.PUBDATE, pubdate.getTime());
        database.insert(Query.TABLE, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public void closeAll() {
        final List<Integer> ids = new ArrayList<>();
        final Cursor cursor = database.query(Query.QUERY_STATE, StateNotification.GENERATED.name());
        try {
            while (cursor.moveToNext()) {
                ids.add(DatabaseUtil.getInt(cursor, Query.ID));
            }
        } finally {
            cursor.close();
        }
        final BriteDatabase.Transaction transaction = database.newTransaction();
        try {
            for (Integer id : ids) {
                final ContentValues values = new ContentValues();
                values.put(Query.STATE, StateNotification.CLOSED.name());
                database.update(Query.TABLE, values, Query.ID + " = ?", String.valueOf(id));
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }
}
