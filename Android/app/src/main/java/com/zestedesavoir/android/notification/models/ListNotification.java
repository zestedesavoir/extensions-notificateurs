package com.zestedesavoir.android.notification.models;

import java.util.List;

public final class ListNotification {
    public final int count;
    public final String next;
    public final String previous;
    public final List<Notification> results;

    public ListNotification(int count, String next, String previous, List<Notification> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public List<Notification> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "ListNotification{" +
                "count=" + count +
                ", next='" + next + '\'' +
                ", previous='" + previous + '\'' +
                ", results=" + results +
                '}';
    }
}
