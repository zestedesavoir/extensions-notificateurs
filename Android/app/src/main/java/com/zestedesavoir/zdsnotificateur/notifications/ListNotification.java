package com.zestedesavoir.zdsnotificateur.notifications;

import java.util.List;

/**
 * @author Gerard Paligot
 */
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

  public int getCount() {
    return count;
  }

  public String getNext() {
    return next;
  }

  public String getPrevious() {
    return previous;
  }

  public List<Notification> getResults() {
    return results;
  }

  @Override public String toString() {
    return "ListNotification{" +
        "count=" + count +
        ", next='" + next + '\'' +
        ", previous='" + previous + '\'' +
        ", results=" + results +
        '}';
  }
}
