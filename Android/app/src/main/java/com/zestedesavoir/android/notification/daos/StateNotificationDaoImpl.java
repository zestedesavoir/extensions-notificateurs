package com.zestedesavoir.android.notification.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;
import com.zestedesavoir.android.internal.db.DatabaseUtil;
import com.zestedesavoir.android.notification.models.StateNotification;

import rx.Observable;

class StateNotificationDaoImpl implements StateNotificationDao {
    private final BriteDatabase database;

    StateNotificationDaoImpl(BriteDatabase database) {
        this.database = database;
    }

    @Override
    public Observable<StateNotification> get(int id) {
        return Observable.create(subscriber -> {
            boolean emit = false;
            StateNotification item = null;
            final Cursor cursor = database.query(Query.QUERY_ONE, String.valueOf(id));
            try {
                if (cursor.moveToNext()) {
                    item = StateNotification.valueOf(DatabaseUtil.getString(cursor, Query.STATE));
                    emit = true;
                }
            } finally {
                cursor.close();
            }
            if (!subscriber.isUnsubscribed()) {
                if (emit) {
                    subscriber.onNext(item);
                } else {
                    subscriber.onNext(StateNotification.NOT_EXIST);
                }
            }
            subscriber.onCompleted();
        });
    }

    @Override
    public void saveOrUpdate(int id) {
        saveOrUpdate(id, StateNotification.GENERATED);
    }

    @Override
    public void saveOrUpdate(int id, StateNotification state) {
        final ContentValues values = new ContentValues();
        values.put(Query.ID, id);
        values.put(Query.STATE, state.name());
        database.insert(Query.TABLE, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
