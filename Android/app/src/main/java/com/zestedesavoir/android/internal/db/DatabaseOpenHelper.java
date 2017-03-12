package com.zestedesavoir.android.internal.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zestedesavoir.android.notification.daos.Query;

public final class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;

    public DatabaseOpenHelper(Context context) {
        super(context, "zds.db", null /* factory */, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Query.CREATE_STATE_NOTIFICATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Query.DROP);
        this.onCreate(db);
    }
}
