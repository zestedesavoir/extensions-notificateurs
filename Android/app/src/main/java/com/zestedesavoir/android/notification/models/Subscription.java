package com.zestedesavoir.android.notification.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author Gerard Paligot
 */
public final class Subscription implements Parcelable {
    public final int id;
    public final Member user;
    @SerializedName("is_active")
    public final boolean isActive;
    @SerializedName("by_email")
    public final boolean byEmail;
    @SerializedName("content_type")
    public final String contentType;
    public final Date pubdate;

    public Subscription(int id, Member user, boolean isActive, boolean byEmail, String contentType, Date pubdate) {
        this.id = id;
        this.user = user;
        this.isActive = isActive;
        this.byEmail = byEmail;
        this.contentType = contentType;
        this.pubdate = pubdate;
    }

    private final static ClassLoader CL = Notification.class.getClassLoader();

    private Subscription(Parcel in) {
        this.id = (Integer) in.readValue(CL);
        this.user = in.readParcelable(Member.class.getClassLoader());
        this.isActive = (Boolean) in.readValue(CL);
        this.byEmail = (Boolean) in.readValue(CL);
        this.contentType = (String) in.readValue(CL);
        this.pubdate = (Date) in.readSerializable();
    }

    public static final Parcelable.Creator<Subscription> CREATOR = new Parcelable.Creator<Subscription>() {
        @Override
        public Subscription createFromParcel(Parcel in) {
            return new Subscription(in);
        }

        @Override
        public Subscription[] newArray(int size) {
            return new Subscription[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeParcelable(user, flags);
        dest.writeValue(isActive);
        dest.writeValue(byEmail);
        dest.writeSerializable(pubdate);
        dest.writeValue(contentType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Subscription that = (Subscription) o;

        if (id != that.id) {
            return false;
        }
        if (isActive != that.isActive) {
            return false;
        }
        if (byEmail != that.byEmail) {
            return false;
        }
        if (user != null ? !user.equals(that.user) : that.user != null) {
            return false;
        }
        if (contentType != null ? !contentType.equals(that.contentType) : that.contentType != null) {
            return false;
        }
        return pubdate != null ? pubdate.equals(that.pubdate) : that.pubdate == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        result = 31 * result + (byEmail ? 1 : 0);
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + (pubdate != null ? pubdate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", user=" + user +
                ", isActive=" + isActive +
                ", byEmail=" + byEmail +
                ", contentType='" + contentType + '\'' +
                ", pubdate=" + pubdate +
                '}';
    }
}
