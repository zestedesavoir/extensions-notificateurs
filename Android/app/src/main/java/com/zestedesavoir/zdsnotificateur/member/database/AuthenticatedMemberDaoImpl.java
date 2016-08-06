package com.zestedesavoir.zdsnotificateur.member.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.zestedesavoir.zdsnotificateur.auth.database.DatabaseTokenException;
import com.zestedesavoir.zdsnotificateur.member.Member;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import static com.zestedesavoir.zdsnotificateur.member.database.MemberDaoImpl.AVATAR;
import static com.zestedesavoir.zdsnotificateur.member.database.MemberDaoImpl.BIOGRAPHY;
import static com.zestedesavoir.zdsnotificateur.member.database.MemberDaoImpl.DATE_JOINED;
import static com.zestedesavoir.zdsnotificateur.member.database.MemberDaoImpl.EMAIL;
import static com.zestedesavoir.zdsnotificateur.member.database.MemberDaoImpl.EMAIL_FOR_ANSWER;
import static com.zestedesavoir.zdsnotificateur.member.database.MemberDaoImpl.ID;
import static com.zestedesavoir.zdsnotificateur.member.database.MemberDaoImpl.IS_ACTIVE;
import static com.zestedesavoir.zdsnotificateur.member.database.MemberDaoImpl.LAST_VISIT;
import static com.zestedesavoir.zdsnotificateur.member.database.MemberDaoImpl.SHOW_EMAIL;
import static com.zestedesavoir.zdsnotificateur.member.database.MemberDaoImpl.SIGN;
import static com.zestedesavoir.zdsnotificateur.member.database.MemberDaoImpl.SITE;
import static com.zestedesavoir.zdsnotificateur.member.database.MemberDaoImpl.TABLE;
import static com.zestedesavoir.zdsnotificateur.member.database.MemberDaoImpl.USERNAME;

/**
 * @author Gerard Paligot
 */
public final class AuthenticatedMemberDaoImpl implements AuthenticatedMemberDao {
  private final Context context;

  @Inject public AuthenticatedMemberDaoImpl(Context context) {
    this.context = context;
  }

  @Override public long[] save(boolean cascade, Member... model) {
    saveMember(model);
    return new long[0];
  }

  @Override public int update(boolean cascade, Member... model) {
    saveMember(model);
    return 0;
  }

  private Member saveMember(Member... model) {
    if (model == null) {
      throw new DatabaseTokenException("We cannot save a member null");
    }
    if (model.length > 1) {
      throw new DatabaseTokenException("We cannot save a list of members authenticated.");
    }
    Member member = model[0];
    new Builder(context, member).build();
    return member;
  }

  @Override public List<Member> getAll() {
    return Collections.unmodifiableList(Collections.singletonList(get(0)));
  }

  @Override public Member get(int key) {
    SharedPreferences preferences = context.getSharedPreferences(TABLE, Context.MODE_PRIVATE);
    if (preferences.getAll().isEmpty()) {
      return null;
    }
    int id = preferences.getInt(ID, 0);
    String username = preferences.getString(USERNAME, "");
    String email = preferences.getString(EMAIL, "");
    boolean showEmail = preferences.getBoolean(SHOW_EMAIL, false);
    boolean isActive = preferences.getBoolean(IS_ACTIVE, false);
    String site = preferences.getString(SITE, "");
    String sign = preferences.getString(SIGN, "");
    String avatar = preferences.getString(AVATAR, "");
    String biography = preferences.getString(BIOGRAPHY, "");
    boolean emailForAnswer = preferences.getBoolean(EMAIL_FOR_ANSWER, false);
    final Calendar instance = Calendar.getInstance();
    instance.setTimeInMillis(preferences.getLong(LAST_VISIT, 0));
    Date lastVisit = instance.getTime();
    instance.setTimeInMillis(preferences.getLong(DATE_JOINED, 0));
    Date dateJoined = instance.getTime();
    return new Member(id, username, email, showEmail, isActive, site, avatar, biography, sign, emailForAnswer, lastVisit, dateJoined);
  }

  @Override public int delete(int key) {
    context.getSharedPreferences(TABLE, Context.MODE_PRIVATE).edit().clear().commit();
    return 0;
  }

  @Override public int deleteAll() {
    return delete(0);
  }

  public static final class Builder {
    private final SharedPreferences.Editor edit;

    public Builder(Context context) {
      edit = context.getSharedPreferences(TABLE, Context.MODE_PRIVATE).edit();
    }

    public Builder(Context context, Member member) {
      this(context);
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
      edit.putInt(ID, id);
      return this;
    }

    public Builder username(String username) {
      edit.putString(USERNAME, username);
      return this;
    }

    public Builder email(String email) {
      edit.putString(EMAIL, email);
      return this;
    }

    public Builder showEmail(boolean showEmail) {
      edit.putBoolean(SHOW_EMAIL, showEmail);
      return this;
    }

    public Builder isActive(boolean isActive) {
      edit.putBoolean(IS_ACTIVE, isActive);
      return this;
    }

    public Builder site(String site) {
      edit.putString(SITE, site);
      return this;
    }

    public Builder avatar(String avatar) {
      edit.putString(AVATAR, avatar);
      return this;
    }

    public Builder biography(String biography) {
      edit.putString(BIOGRAPHY, biography);
      return this;
    }

    public Builder sign(String sign) {
      edit.putString(SIGN, sign);
      return this;
    }

    public Builder emailForAnswer(boolean emailForAnswer) {
      edit.putBoolean(EMAIL_FOR_ANSWER, emailForAnswer);
      return this;
    }

    public Builder lastVisit(Date lastVisit) {
      edit.putLong(LAST_VISIT, lastVisit.getTime());
      return this;
    }

    public Builder dateJoined(Date dateJoined) {
      edit.putLong(DATE_JOINED, dateJoined.getTime());
      return this;
    }

    public void build() {
      edit.apply();
    }
  }
}
