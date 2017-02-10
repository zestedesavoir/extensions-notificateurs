package com.zestedesavoir.zdsnotificateur.member.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;
import com.zestedesavoir.zdsnotificateur.internal.database.DbUtils;
import com.zestedesavoir.zdsnotificateur.member.Member;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Gerard Paligot
 */
public final class MemberDaoImpl implements MemberDao {
  public static final String TABLE = "member";

  public static final String ID = "_id";
  public static final String USERNAME = "username";
  public static final String EMAIL = "email";
  public static final String SHOW_EMAIL = "show_email";
  public static final String IS_ACTIVE = "isActive";
  public static final String SITE = "site";
  public static final String AVATAR = "avatar";
  public static final String BIOGRAPHY = "biography";
  public static final String SIGN = "sign";
  public static final String EMAIL_FOR_ANSWER = "emailForAnswer";
  public static final String LAST_VISIT = "lastVisit";
  public static final String DATE_JOINED = "dateJoined";

  public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE + " ( "
      + ID + " integer not null primary key unique check(" + ID + " >= 0), "
      + USERNAME + " text not null unique check(length(" + USERNAME + ") > 0), "
      + EMAIL + " text, "
      + SHOW_EMAIL + " integer, "
      + IS_ACTIVE + " integer not null default 1, "
      + SITE + " text, "
      + AVATAR + " text not null, "
      + BIOGRAPHY + " text, "
      + SIGN + " text, "
      + EMAIL_FOR_ANSWER + " integer, "
      + LAST_VISIT + " text, "
      + DATE_JOINED + " text not null);";

  public static final String DROP = "DROP TABLE IF EXISTS " + TABLE;

  public static String ALL_QUERY = ""
      + "SELECT *"
      + " FROM " + TABLE;

  public static String GET_QUERY = ""
      + "SELECT *"
      + " FROM " + TABLE
      + " WHERE " + ID
      + " = ?";

  public static String SEARCH_QUERY = ""
      + "SELECT *"
      + " FROM " + TABLE
      + " WHERE " + USERNAME
      + " LIKE ?";

  @Inject BriteDatabase db;

  @Override
  public long[] save(boolean cascade, Member... members) {
    final long[] rows = new long[members.length];
    final BriteDatabase.Transaction transaction = db.newTransaction();
    try {
      for (int i = 0; i < members.length; i++) {
        final Member member = members[i];
        if (member == null) {
          throw new DatabaseMemberException("We cannot save a list of members with an item null.");
        }
        if (!member.isComplete()) {
          continue;
        }
        final int row = (int) db.insert(TABLE, new Builder(member).build(), SQLiteDatabase.CONFLICT_REPLACE);
        if (row < 0) {
          throw new DatabaseMemberException(String.format("We cannot save the member %s.", member.toString()));
        }
        rows[i] = row;
      }
      transaction.markSuccessful();
    } finally {
      transaction.end();
    }
    return rows;
  }

  @Override
  public int update(boolean cascade, Member... members) {
    int rowsAffected = 0;
    final BriteDatabase.Transaction transaction = db.newTransaction();
    try {
      for (final Member member : members) {
        if (member == null) {
          throw new DatabaseMemberException("We cannot update a list of members with an item null.");
        }
        rowsAffected += db.update(TABLE, new Builder(member).build(), ID + " = ?", String.valueOf(member.pk()));
      }
      transaction.markSuccessful();
    } finally {
      transaction.end();
    }
    return rowsAffected;
  }

  @Override
  public List<Member> getAll() {
    final BriteDatabase.Transaction transaction = db.newTransaction();
    Cursor query = null;
    try {
      query = db.query(ALL_QUERY);
      final List<Member> members = getMembersFromCursor(query);
      transaction.markSuccessful();
      return members;
    } finally {
      if (query != null) {
        query.close();
      }
      transaction.end();
    }
  }

  @Override
  public List<Member> search(String username) {
    if (username == null || username.isEmpty()) {
      throw new DatabaseMemberException("Username cannot be null or empty.");
    }
    final BriteDatabase.Transaction transaction = db.newTransaction();
    Cursor query = null;
    try {
      query = db.query(SEARCH_QUERY, "%" + username + "%");
      final List<Member> members = getMembersFromCursor(query);
      transaction.markSuccessful();
      return members;
    } finally {
      if (query != null) {
        query.close();
      }
      transaction.end();
    }
  }

  @Override
  public Member get(int key) {
    if (key < 0) {
      throw new DatabaseMemberException("Pk cannot be negative.");
    }
    final BriteDatabase.Transaction transaction = db.newTransaction();
    Cursor query = null;
    try {
      query = db.query(GET_QUERY, String.valueOf(key));
      final List<Member> members = getMembersFromCursor(query);
      transaction.markSuccessful();
      if (members.size() > 0) {
        return members.get(0);
      }
      return null;
    } finally {
      if (query != null) {
        query.close();
      }
      transaction.end();
    }
  }

  @Override
  public int delete(int key) {
    if (key < 0) {
      throw new DatabaseMemberException("Pk cannot be negative.");
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

  private static List<Member> getMembersFromCursor(final Cursor cursor) {
    if (cursor == null || cursor.getCount() == 0) {
      return new ArrayList<>();
    }
    final List<Member> members = new ArrayList<>();
    if (cursor.moveToFirst()) {
      do {
        members.add(getMemberFromCursor(cursor));
      } while (cursor.moveToNext());
    }
    return members;
  }

  private static Member getMemberFromCursor(final Cursor cursor) {
    if (cursor == null || cursor.getCount() == 0) {
      return null;
    }
    final int pk = DbUtils.getInt(cursor, ID);
    final String username = DbUtils.getString(cursor, USERNAME);
    final String email = DbUtils.getString(cursor, EMAIL);
    final boolean showEmail = DbUtils.getBoolean(cursor, SHOW_EMAIL);
    final boolean isActive = DbUtils.getBoolean(cursor, IS_ACTIVE);
    final String site = DbUtils.getString(cursor, SITE);
    final String avatar = DbUtils.getString(cursor, AVATAR);
    final String biography = DbUtils.getString(cursor, BIOGRAPHY);
    final String sign = DbUtils.getString(cursor, SIGN);
    final boolean emailForAnswer = DbUtils.getBoolean(cursor, EMAIL_FOR_ANSWER);
    final Date lastVisit = DbUtils.getDate(cursor, LAST_VISIT);
    final Date dateJoined = DbUtils.getDate(cursor, DATE_JOINED);
    return new Member(pk, username, email, showEmail, isActive, site, avatar, biography, sign, emailForAnswer, lastVisit, dateJoined);
  }

  public static final class Builder {
    private final ContentValues values = new ContentValues();

    public Builder() {
    }

    public Builder(Member member) {
      id(member.pk());
      username(member.username());
      email(member.email());
      showEmail(member.showEmail());
      isActive(member.active());
      site(member.site());
      avatar(member.avatar());
      biography(member.biography());
      sign(member.sign());
      emailForAnswer(member.emailForAnswer());
      if (member.lastVisit() != null) {
        lastVisit(member.lastVisit());
      }
      if (member.dateJoined() != null) {
        dateJoined(member.dateJoined());
      }
    }

    public Builder id(int id) {
      values.put(ID, id);
      return this;
    }

    public Builder username(String username) {
      values.put(USERNAME, username);
      return this;
    }

    public Builder email(String email) {
      values.put(EMAIL, email);
      return this;
    }

    public Builder showEmail(boolean showEmail) {
      values.put(SHOW_EMAIL, showEmail);
      return this;
    }

    public Builder isActive(boolean isActive) {
      values.put(IS_ACTIVE, isActive);
      return this;
    }

    public Builder site(String site) {
      values.put(SITE, site);
      return this;
    }

    public Builder avatar(String avatar) {
      values.put(AVATAR, avatar);
      return this;
    }

    public Builder biography(String biography) {
      values.put(BIOGRAPHY, biography);
      return this;
    }

    public Builder sign(String sign) {
      values.put(SIGN, sign);
      return this;
    }

    public Builder emailForAnswer(boolean emailForAnswer) {
      values.put(EMAIL_FOR_ANSWER, emailForAnswer);
      return this;
    }

    public Builder lastVisit(Date lastVisit) {
      values.put(LAST_VISIT, lastVisit.getTime());
      return this;
    }

    public Builder dateJoined(Date dateJoined) {
      values.put(DATE_JOINED, dateJoined.getTime());
      return this;
    }

    public ContentValues build() {
      return values;
    }
  }
}
