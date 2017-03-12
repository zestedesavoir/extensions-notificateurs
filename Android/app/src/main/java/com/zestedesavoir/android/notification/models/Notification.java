package com.zestedesavoir.android.notification.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author Gerard Paligot
 */
public final class Notification implements Parcelable {
    public final int id;
    public final String title;
    @SerializedName("is_read")
    public final boolean isRead;
    public final String url;
    public final Member sender;
    public final Date pubdate;
    @SerializedName("content_type")
    public final String contentType;
    public final Subscription subscription;

    public Notification(int id, String title, boolean isRead, String url, Member sender, Date pubdate, String contentType, Subscription subscription) {
        this.id = id;
        this.title = title;
        this.isRead = isRead;
        this.url = url;
        this.sender = sender;
        this.pubdate = pubdate;
        this.contentType = contentType;
        this.subscription = subscription;
    }

    private final static ClassLoader CL = Notification.class.getClassLoader();

    private Notification(Parcel in) {
        this.id = (Integer) in.readValue(CL);
        this.title = (String) in.readValue(CL);
        this.isRead = (Boolean) in.readValue(CL);
        this.url = (String) in.readValue(CL);
        this.sender = in.readParcelable(Member.class.getClassLoader());
        this.pubdate = (Date) in.readSerializable();
        this.contentType = (String) in.readValue(CL);
        this.subscription = in.readParcelable(Subscription.class.getClassLoader());
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(title);
        dest.writeValue(isRead);
        dest.writeValue(url);
        dest.writeParcelable(sender, flags);
        dest.writeSerializable(pubdate);
        dest.writeValue(contentType);
        dest.writeParcelable(subscription, flags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Notification that = (Notification) o;

        if (id != that.id) {
            return false;
        }
        if (isRead != that.isRead) {
            return false;
        }
        if (title != null ? !title.equals(that.title) : that.title != null) {
            return false;
        }
        if (url != null ? !url.equals(that.url) : that.url != null) {
            return false;
        }
        if (sender != null ? !sender.equals(that.sender) : that.sender != null) {
            return false;
        }
        if (pubdate != null ? !pubdate.equals(that.pubdate) : that.pubdate != null) {
            return false;
        }
        if (contentType != null ? !contentType.equals(that.contentType) : that.contentType != null) {
            return false;
        }
        return subscription != null ? subscription.equals(that.subscription) : that.subscription == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (isRead ? 1 : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (pubdate != null ? pubdate.hashCode() : 0);
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + (subscription != null ? subscription.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", isRead=" + isRead +
                ", url='" + url + '\'' +
                ", sender=" + sender +
                ", pubdate=" + pubdate +
                ", contentType='" + contentType + '\'' +
                ", subscription=" + subscription +
                '}';
    }
}
