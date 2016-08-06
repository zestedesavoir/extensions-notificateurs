package com.zestedesavoir.zdsnotificateur.notifications.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;
import com.zestedesavoir.zdsnotificateur.internal.database.DbUtils;
import com.zestedesavoir.zdsnotificateur.member.database.MemberDao;
import com.zestedesavoir.zdsnotificateur.member.database.MemberDaoImpl;
import com.zestedesavoir.zdsnotificateur.notifications.Notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Gerard Paligot
 */
public final class NotificationDaoImpl implements NotificationDao {
  public static final String TABLE = "table_notification";

  public static final String _ID = "_id";
  public static final String ID = "id";
  public static final String TITLE = "title";
  public static final String IS_READ = "is_read";
  public static final String URL = "url";
  public static final String SENDER = "sender";
  public static final String PUBDATE = "pubdate";
  public static final String CONTENT_TYPE = "content_type";
  public static final String SUBSCRIPTION = "subscription";

  public static String CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE + " ( "
      + _ID + " integer not null primary key autoincrement,"
      + ID + " integer not null unique check(" + ID + " >= 0), "
      + TITLE + " text not null check(length(" + TITLE + ") > 0), "
      + IS_READ + " integer not null default 0, "
      + URL + " text not null check(length(" + URL + ") > 0), "
      + SENDER + " integer not null check(" + SENDER + " >= 0), "
      + PUBDATE + " text not null, "
      + CONTENT_TYPE + " text not null, "
      + SUBSCRIPTION + " integer not null check(" + SUBSCRIPTION + " >= 0), "
      + " FOREIGN KEY (" + SENDER + ") REFERENCES " + MemberDaoImpl.TABLE + "(" + MemberDaoImpl.ID + ")"
      + " FOREIGN KEY (" + SUBSCRIPTION + ") REFERENCES " + SubscriptionDaoImpl.TABLE + "(" + SubscriptionDaoImpl.ID + ")"
      + ");";

  public static final String DROP = "DROP TABLE IF EXISTS " + TABLE;

  public static String ALL_QUERY = ""
      + "SELECT *"
      + " FROM " + TABLE;

  public static String GET_QUERY = ""
      + "SELECT *"
      + " FROM " + TABLE
      + " WHERE " + ID + " = ?";

  @Inject BriteDatabase db;
  @Inject MemberDao memberDao;
  @Inject SubscriptionDao subscriptionDao;

  @Override public long[] save(boolean cascade, Notification... models) {
    final long[] rows = new long[models.length];
    final BriteDatabase.Transaction transaction = db.newTransaction();
    try {
      for (int i = 0; i < models.length; i++) {
        Notification notification = models[i];
        final long row = db.insert(TABLE, new Builder(notification).build(), SQLiteDatabase.CONFLICT_REPLACE);
        if (row < 0) {
          throw new RuntimeException(String.format("We cannot save the notification %s.", notification.toString()));
        }
        rows[i] = row;
      }
      transaction.markSuccessful();
    } finally {
      transaction.end();
    }
    if (cascade) {
      for (Notification notification : models) {
        memberDao.save(true, notification.sender());
        subscriptionDao.save(true, notification.subscription());
      }
    }
    return rows;
  }

  @Override public int update(boolean cascade, Notification... models) {
    int rowsAffected = 0;
    final BriteDatabase.Transaction transaction = db.newTransaction();
    try {
      for (Notification notification : models) {
        rowsAffected += db.update(TABLE, new Builder(notification).build(), ID + " = ?", String.valueOf(notification.id));
      }
      transaction.markSuccessful();
    } finally {
      transaction.end();
    }
    if (cascade) {
      for (Notification notification : models) {
        memberDao.update(true, notification.sender());
        subscriptionDao.update(true, notification.subscription());
      }
    }
    return rowsAffected;
  }

  @Override public List<Notification> getAll() {
    final List<Notification> notifications = new ArrayList<>();
    Cursor query = null;
    final BriteDatabase.Transaction transaction = db.newTransaction();
    try {
      query = db.query(ALL_QUERY);
      notifications.addAll(getNotificationsFromCursor(query));
      transaction.markSuccessful();
    } finally {
      if (query != null) {
        query.close();
      }
      transaction.end();
    }
    for (Notification notification : notifications) {
      notification.sender(memberDao.get(notification.sender().pk()));
      notification.subscription(subscriptionDao.get(notification.subscription().id));
    }
    return notifications;
  }

  @Override public Notification get(int key) {
    if (key < 0) {
      throw new RuntimeException("Identifier cannot be negative.");
    }
    Cursor query = null;
    final BriteDatabase.Transaction transaction = db.newTransaction();
    try {
      query = db.query(GET_QUERY, String.valueOf(key));
      final List<Notification> notifications = getNotificationsFromCursor(query);
      for (Notification notification : notifications) {
        notification.sender(memberDao.get(notification.sender().pk()));
        notification.subscription(subscriptionDao.get(notification.subscription().id));
      }
      transaction.markSuccessful();
      if (notifications.size() > 0) {
        return notifications.get(0);
      }
      return null;
    } finally {
      if (query != null) {
        query.close();
      }
      transaction.end();
    }
  }

  @Override public int delete(int key) {
    if (key < 0) {
      throw new RuntimeException("Identifier cannot be negative.");
    }
    final BriteDatabase.Transaction transaction = db.newTransaction();
    try {
      final int row = db.delete(TABLE, ID + " = " + key);
      transaction.markSuccessful();
      return row;
    } finally {
      transaction.end();
    }
  }

  @Override public int deleteAll() {
    final BriteDatabase.Transaction transaction = db.newTransaction();
    try {
      final int row = db.delete(TABLE, null);
      transaction.markSuccessful();
      return row;
    } finally {
      transaction.end();
    }
  }

  private static List<Notification> getNotificationsFromCursor(final Cursor cursor) {
    if (cursor == null || cursor.getCount() == 0) {
      return new ArrayList<>();
    }
    final List<Notification> notifications = new ArrayList<>();
    if (cursor.moveToFirst()) {
      do {
        notifications.add(getNotificationFromCursor(cursor));
      } while (cursor.moveToNext());
    }
    return notifications;
  }

  private static Notification getNotificationFromCursor(final Cursor cursor) {
    if (cursor == null || cursor.getCount() == 0) {
      return null;
    }
    final int id = DbUtils.getInt(cursor, ID);
    final String title = DbUtils.getString(cursor, TITLE);
    final boolean isRead = DbUtils.getBoolean(cursor, IS_READ);
    final String url = DbUtils.getString(cursor, URL);
    final int sender = DbUtils.getInt(cursor, SENDER);
    final Date pubdate = DbUtils.getDate(cursor, PUBDATE);
    final String contentType = DbUtils.getString(cursor, CONTENT_TYPE);
    final int subscription = DbUtils.getInt(cursor, SUBSCRIPTION);
    return new Notification(id, title, isRead, url, sender, pubdate, contentType, subscription);
  }

  public static final class Builder {
    private final ContentValues values = new ContentValues();

    public Builder() {
    }

    public Builder(Notification notification) {
      id(notification.id);
      title(notification.title);
      isRead(notification.isRead);
      url(notification.url);
      sender(notification.sender().pk());
      pubdate(notification.pubdate);
      contentType(notification.contentType);
      subscription(notification.subscription().id);
    }

    public Builder id(long id) {
      values.put(ID, id);
      return this;
    }

    public Builder title(String title) {
      values.put(TITLE, title);
      return this;
    }

    private Builder isRead(boolean isRead) {
      values.put(IS_READ, isRead);
      return this;
    }

    private Builder url(String url) {
      values.put(URL, url);
      return this;
    }

    public Builder sender(long sender) {
      values.put(SENDER, sender);
      return this;
    }

    public Builder pubdate(Date pubdate) {
      values.put(PUBDATE, pubdate.getTime());
      return this;
    }

    private Builder contentType(String contentType) {
      values.put(CONTENT_TYPE, contentType);
      return this;
    }

    private Builder subscription(int subscription) {
      values.put(SUBSCRIPTION, subscription);
      return this;
    }

    public ContentValues build() {
      return values;
    }
  }
}
