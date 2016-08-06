package com.zestedesavoir.zdsnotificateur.internal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zestedesavoir.zdsnotificateur.member.database.MemberDaoImpl;
import com.zestedesavoir.zdsnotificateur.notifications.database.NotificationDaoImpl;
import com.zestedesavoir.zdsnotificateur.notifications.database.SubscriptionDaoImpl;

/**
 * @author Gerard Paligot
 */
public final class DatabaseOpenHelper extends SQLiteOpenHelper {
  private static final int VERSION = 1;

  public DatabaseOpenHelper(Context context) {
    super(context, "zds.db", null, VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(MemberDaoImpl.CREATE);
    db.execSQL(SubscriptionDaoImpl.CREATE);
    db.execSQL(NotificationDaoImpl.CREATE);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL(MemberDaoImpl.DROP);
    db.execSQL(SubscriptionDaoImpl.DROP);
    db.execSQL(NotificationDaoImpl.DROP);
    this.onCreate(db);
  }
}
