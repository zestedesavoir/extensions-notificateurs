package com.zestedesavoir.android.notification.daos;

public interface Query {
    String TABLE = "state_notification";
    String ID = "id";
    String STATE = "state";
    String PUBDATE = "pubdate";

    String CREATE_STATE_NOTIFICATION = ""
            + "CREATE TABLE " + TABLE + "("
            + ID + " INTEGER NOT NULL UNIQUE,"
            + STATE + " TEXT NOT NULL,"
            + PUBDATE + " INTEGER NOT NULL"
            + ")";

    String QUERY_ONE = "SELECT * FROM " + TABLE + " WHERE " + ID + " = ?";
}
