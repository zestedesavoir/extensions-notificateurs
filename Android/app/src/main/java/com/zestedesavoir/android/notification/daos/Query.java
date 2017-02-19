package com.zestedesavoir.android.notification.daos;

public interface Query {
    String TABLE = "state_notification";
    String ID = "id";
    String STATE = "state";

    String CREATE_STATE_NOTIFICATION = ""
            + "CREATE TABLE " + TABLE + "("
            + ID + " INTEGER NOT NULL UNIQUE,"
            + STATE + " TEXT NOT NULL"
            + ")";

    String QUERY_ONE = "SELECT * FROM " + TABLE + " WHERE " + ID + " = ?";
}
