package com.zestedesavoir.zdsnotificateur.member;

import android.os.Parcelable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.zestedesavoir.zdsnotificateur.internal.action.JsonUtils;

import java.util.Date;

/**
 * @author Gerard Paligot
 */
public final class Member implements Parcelable {
  @SerializedName("id")
  private int pk;
  private String username;
  private String email;
  @SerializedName("show_email")
  private boolean showEmail;
  @SerializedName("is_active")
  private boolean isActive;
  private String site;
  @SerializedName("avatar_url")
  private String avatar;
  private String biography;
  private String sign;
  @SerializedName("email_for_answer")
  private boolean emailForAnswer;
  @SerializedName("last_visit")
  private Date lastVisit;
  @SerializedName("date_joined")
  private Date dateJoined;

  public Member(int pk) {
    this.pk = pk;
  }

  public Member(int pk, String username, String email, boolean showEmail, boolean isActive, String site, String avatar, String biography, String sign, boolean emailForAnswer, Date lastVisit, Date dateJoined) {
    this.pk = pk;
    this.username = username;
    this.email = email;
    this.showEmail = showEmail;
    this.isActive = isActive;
    this.site = site;
    this.avatar = avatar;
    this.biography = biography;
    this.sign = sign;
    this.emailForAnswer = emailForAnswer;
    this.lastVisit = lastVisit;
    this.dateJoined = dateJoined;
  }

  private final static ClassLoader CL = Member.class.getClassLoader();

  private Member(android.os.Parcel in) {
    this((Integer) in.readValue(CL), (String) in.readValue(CL), (String) in.readValue(CL), (Boolean) in.readValue(CL), (Boolean) in.readValue(CL), (String) in.readValue(CL), (String) in.readValue(CL), (String) in.readValue(CL), (String) in.readValue(CL), (Boolean) in.readValue(CL), (Date) in.readSerializable(), (Date) in.readSerializable());
  }

  public Member(JsonObject json) {
    this(JsonUtils.getInt(json, "id"),
        JsonUtils.getString(json, "username"),
        JsonUtils.getString(json, "email"),
        JsonUtils.getBoolean(json, "show_email"),
        JsonUtils.getBoolean(json, "is_active"),
        JsonUtils.getString(json, "site"),
        JsonUtils.getString(json, "avatar_url"),
        JsonUtils.getString(json, "biography"),
        JsonUtils.getString(json, "sign"),
        JsonUtils.getBoolean(json, "email_for_answer"),
        JsonUtils.getDate(json, "last_visit"),
        JsonUtils.getDate(json, "date_joined"));
  }

  public int pk() {
    return pk;
  }

  public void pk(int pk) {
    this.pk = pk;
  }

  public String username() {
    return username;
  }

  public void username(String username) {
    this.username = username;
  }

  public String email() {
    return email;
  }

  public void email(String email) {
    this.email = email;
  }

  public boolean showEmail() {
    return showEmail;
  }

  public void showEmail(boolean showEmail) {
    this.showEmail = showEmail;
  }

  public boolean active() {
    return isActive;
  }

  public void isActive(boolean isActive) {
    this.isActive = isActive;
  }

  public String site() {
    return site;
  }

  public void site(String site) {
    this.site = site;
  }

  public String avatar() {
    return avatar;
  }

  public void avatar(String avatar) {
    this.avatar = avatar;
  }

  public String biography() {
    return biography;
  }

  public void biography(String biography) {
    this.biography = biography;
  }

  public String sign() {
    return sign;
  }

  public void sign(String sign) {
    this.sign = sign;
  }

  public boolean emailForAnswer() {
    return emailForAnswer;
  }

  public void emailForAnswer(boolean emailForAnswer) {
    this.emailForAnswer = emailForAnswer;
  }

  public Date lastVisit() {
    return lastVisit;
  }

  public void lastVisit(Date lastVisit) {
    this.lastVisit = lastVisit;
  }

  public Date dateJoined() {
    return dateJoined;
  }

  public void dateJoined(Date dateJoined) {
    this.dateJoined = dateJoined;
  }

  public boolean isComplete() {
    return pk != 0
        && username != null
        && avatar != null
        && dateJoined != null;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Member member = (Member) o;

    if (pk != member.pk) return false;
    if (showEmail != member.showEmail) return false;
    if (isActive != member.isActive) return false;
    if (emailForAnswer != member.emailForAnswer) return false;
    if (username != null ? !username.equals(member.username) : member.username != null)
      return false;
    if (email != null ? !email.equals(member.email) : member.email != null) return false;
    if (site != null ? !site.equals(member.site) : member.site != null) return false;
    if (avatar != null ? !avatar.equals(member.avatar) : member.avatar != null) return false;
    if (biography != null ? !biography.equals(member.biography) : member.biography != null)
      return false;
    if (sign != null ? !sign.equals(member.sign) : member.sign != null) return false;
    if (lastVisit != null ? !lastVisit.equals(member.lastVisit) : member.lastVisit != null)
      return false;
    return !(dateJoined != null ? !dateJoined.equals(member.dateJoined) : member.dateJoined != null);

  }

  @Override public int hashCode() {
    int result = pk;
    result = 31 * result + (username != null ? username.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (showEmail ? 1 : 0);
    result = 31 * result + (isActive ? 1 : 0);
    result = 31 * result + (site != null ? site.hashCode() : 0);
    result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
    result = 31 * result + (biography != null ? biography.hashCode() : 0);
    result = 31 * result + (sign != null ? sign.hashCode() : 0);
    result = 31 * result + (emailForAnswer ? 1 : 0);
    result = 31 * result + (lastVisit != null ? lastVisit.hashCode() : 0);
    result = 31 * result + (dateJoined != null ? dateJoined.hashCode() : 0);
    return result;
  }

  @Override public String toString() {
    return "Member{" +
        "pk=" + pk +
        ", username='" + username + '\'' +
        ", email='" + email + '\'' +
        ", showEmail=" + showEmail +
        ", isActive=" + isActive +
        ", site='" + site + '\'' +
        ", avatar='" + avatar + '\'' +
        ", biography='" + biography + '\'' +
        ", sign='" + sign + '\'' +
        ", emailForAnswer=" + emailForAnswer +
        ", lastVisit=" + lastVisit +
        ", dateJoined=" + dateJoined +
        '}';
  }

  public static final Creator<Member> CREATOR = new Creator<Member>() {
    @Override
    public Member createFromParcel(android.os.Parcel in) {
      return new Member(in);
    }

    @Override
    public Member[] newArray(int size) {
      return new Member[size];
    }
  };

  @Override
  public void writeToParcel(android.os.Parcel dest, int flags) {
    dest.writeValue(pk);
    dest.writeValue(username);
    dest.writeValue(email);
    dest.writeValue(showEmail);
    dest.writeValue(isActive);
    dest.writeValue(site);
    dest.writeValue(avatar);
    dest.writeValue(biography);
    dest.writeValue(sign);
    dest.writeValue(emailForAnswer);
    dest.writeSerializable(lastVisit);
    dest.writeSerializable(dateJoined);
  }

  @Override
  public int describeContents() {
    return 0;
  }
}
