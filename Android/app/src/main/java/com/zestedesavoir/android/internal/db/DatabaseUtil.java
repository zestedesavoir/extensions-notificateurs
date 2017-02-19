package com.zestedesavoir.android.internal.db;

import android.database.Cursor;

import java.util.Calendar;
import java.util.Date;

public final class DatabaseUtil {
    public static final int BOOLEAN_FALSE = 0;
    public static final int BOOLEAN_TRUE = 1;

    public static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
    }

    public static boolean getBoolean(Cursor cursor, String columnName) {
        return getInt(cursor, columnName) == BOOLEAN_TRUE;
    }

    public static long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
    }

    public static int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
    }

    public static Date getDate(Cursor cursor, String columnName) {
        final long timeToFormat = getLong(cursor, columnName);
        final Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(timeToFormat);
        return instance.getTime();
    }

    private DatabaseUtil() {
        throw new AssertionError("No instances.");
    }
}
