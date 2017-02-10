package com.zestedesavoir.zdsnotificateur.notifications.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;
import com.zestedesavoir.zdsnotificateur.internal.database.DbUtils;
import com.zestedesavoir.zdsnotificateur.member.database.MemberDao;
import com.zestedesavoir.zdsnotificateur.member.database.MemberDaoImpl;
import com.zestedesavoir.zdsnotificateur.notifications.Subscription;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Gerard Paligot
 */
public final class SubscriptionDaoImpl implements SubscriptionDao {
  public static final String TABLE = "table_subscription";

  public static final String _ID = "_id";
  public static final String ID = "id";
  public static final String USER = "user";
  public static final String IS_ACTIVE = "is_active";
  public static final String BY_EMAIL = "by_email";
  public static final String CONTENT_TYPE = "content_type";
  public static final String PUBDATE = "pubdate";

  public static String CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE + " ( "
      + _ID + " integer not null primary key autoincrement,"
      + ID + " integer not null unique check(" + ID + " >= 0), "
      + USER + " integer not null check(" + USER + " >= 0), "
      + IS_ACTIVE + " integer not null default 1, "
      + BY_EMAIL + " integer not null default 0, "
      + CONTENT_TYPE + " text not null, "
      + PUBDATE + " text not null, "
      + " FOREIGN KEY (" + USER + ") REFERENCES " + MemberDaoImpl.TABLE + "(" + MemberDaoImpl.ID + ")"
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

  @Override public long[] save(boolean cascade, Subscription... models) {
    final long[] rows = new long[models.length];
    final BriteDatabase.Transaction transaction = db.newTransaction();
    try {
      for (int i = 0; i < models.length; i++) {
        Subscription subscription = models[i];
        final long row = db.insert(TABLE, new Builder(subscription).build(), SQLiteDatabase.CONFLICT_REPLACE);
        if (row < 0) {
          throw new RuntimeException(String.format("We cannot save the subscription %s.", subscription.toString()));
        }
        rows[i] = row;
      }
      transaction.markSuccessful();
    } finally {
      transaction.end();
    }
    if (cascade) {
      for (Subscription subscription : models) {
        memberDao.save(true, subscription.user());
      }
    }
    return rows;
  }

  @Override public int update(boolean cascade, Subscription... models) {
    int rowsAffected = 0;
    final BriteDatabase.Transaction transaction = db.newTransaction();
    try {
      for (Subscription subscription : models) {
        rowsAffected += db.update(TABLE, new Builder(subscription).build(), ID + " = ?", String.valueOf(subscription.id));
      }
      transaction.markSuccessful();
    } finally {
      transaction.end();
    }
    if (cascade) {
      for (Subscription subscription : models) {
        memberDao.update(true, subscription.user());
      }
    }
    return rowsAffected;
  }

  @Override public List<Subscription> getAll() {
    final List<Subscription> subscriptions = new ArrayList<>();
    Cursor query = null;
    final BriteDatabase.Transaction transaction = db.newTransaction();
    try {
      query = db.query(ALL_QUERY);
      subscriptions.addAll(getSubscriptionsFromCursor(query));
      transaction.markSuccessful();
    } finally {
      if (query != null) {
        query.close();
      }
      transaction.end();
    }
    for (Subscription notification : subscriptions) {
      notification.user(memberDao.get(notification.user().pk()));
    }
    return subscriptions;
  }

  @Override public Subscription get(int key) {
    if (key < 0) {
      throw new RuntimeException("Identifier cannot be negative.");
    }
    Cursor query = null;
    final BriteDatabase.Transaction transaction = db.newTransaction();
    try {
      query = db.query(GET_QUERY, String.valueOf(key));
      final List<Subscription> subscriptions = getSubscriptionsFromCursor(query);
      for (Subscription subscription : subscriptions) {
        subscription.user(memberDao.get(subscription.user().pk()));
      }
      transaction.markSuccessful();
      if (subscriptions.size() > 0) {
        return subscriptions.get(0);
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

  private static List<Subscription> getSubscriptionsFromCursor(final Cursor cursor) {
    if (cursor == null || cursor.getCount() == 0) {
      return new ArrayList<>();
    }
    final List<Subscription> subscriptions = new ArrayList<>();
    if (cursor.moveToFirst()) {
      do {
        subscriptions.add(getSubscriptionFromCursor(cursor));
      } while (cursor.moveToNext());
    }
    return subscriptions;
  }

  private static Subscription getSubscriptionFromCursor(final Cursor cursor) {
    if (cursor == null || cursor.getCount() == 0) {
      return null;
    }
    final int id = DbUtils.getInt(cursor, ID);
    final int user = DbUtils.getInt(cursor, USER);
    final boolean isActive = DbUtils.getBoolean(cursor, IS_ACTIVE);
    final boolean byEmail = DbUtils.getBoolean(cursor, BY_EMAIL);
    final String contentType = DbUtils.getString(cursor, CONTENT_TYPE);
    final Date pubdate = DbUtils.getDate(cursor, PUBDATE);
    return new Subscription(id, user, isActive, byEmail, contentType, pubdate);
  }

  public static final class Builder {
    private final ContentValues values = new ContentValues();

    public Builder() {
    }

    public Builder(Subscription subscription) {
      id(subscription.id);
      user(subscription.user().pk());
      isActive(subscription.isActive);
      byEmail(subscription.byEmail);
      contentType(subscription.contentType);
      pubdate(subscription.pubdate);
    }

    public Builder id(long id) {
      values.put(ID, id);
      return this;
    }

    public Builder user(long user) {
      values.put(USER, user);
      return this;
    }

    private Builder isActive(boolean isActive) {
      values.put(IS_ACTIVE, isActive);
      return this;
    }

    private Builder byEmail(boolean byEmail) {
      values.put(BY_EMAIL, byEmail);
      return this;
    }

    private Builder contentType(String contentType) {
      values.put(CONTENT_TYPE, contentType);
      return this;
    }

    public Builder pubdate(Date pubdate) {
      values.put(PUBDATE, pubdate.getTime());
      return this;
    }

    public ContentValues build() {
      return values;
    }
  }
}
