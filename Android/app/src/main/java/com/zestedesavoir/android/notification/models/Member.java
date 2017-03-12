package com.zestedesavoir.android.notification.models;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author Gerard Paligot
 */
public final class Member implements Parcelable {
    @SerializedName("id")
    public final int pk;
    public final String username;
    @SerializedName("is_active")
    public final boolean isActive;
    @SerializedName("html_url")
    public final String htmlUrl;
    @SerializedName("avatar_url")
    public final String avatar;

    public Member(int pk, String username, boolean isActive, String htmlUrl, String avatar) {
        this.pk = pk;
        this.username = username;
        this.isActive = isActive;
        this.htmlUrl = htmlUrl;
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Member member = (Member) o;

        if (pk != member.pk) {
            return false;
        }
        if (isActive != member.isActive) {
            return false;
        }
        if (username != null ? !username.equals(member.username) : member.username != null) {
            return false;
        }
        if (htmlUrl != null ? !htmlUrl.equals(member.htmlUrl) : member.htmlUrl != null) {
            return false;
        }
        return avatar != null ? avatar.equals(member.avatar) : member.avatar == null;

    }

    @Override
    public int hashCode() {
        int result = pk;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        result = 31 * result + (htmlUrl != null ? htmlUrl.hashCode() : 0);
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        return result;
    }

    private final static ClassLoader CL = Member.class.getClassLoader();

    public Member(android.os.Parcel in) {
        this((Integer) in.readValue(CL), (String) in.readValue(CL), (Boolean) in.readValue(CL), (String) in.readValue(CL), (String) in.readValue(CL));
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
        dest.writeValue(isActive);
        dest.writeValue(htmlUrl);
        dest.writeValue(avatar);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
