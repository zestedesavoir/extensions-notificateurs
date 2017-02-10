package com.zestedesavoir.zdsnotificateur.ui.notifications;

import com.zestedesavoir.zdsnotificateur.notifications.Notification;

import java.util.Comparator;

/**
 * @author Gerard Paligot
 */
public class NotificationComparator implements Comparator<Notification> {
  @Override public int compare(Notification lhs, Notification rhs) {
    final int compareToRead = lhs.isRead == rhs.isRead ? 0 : lhs.isRead ? 1 : -1;
    if (compareToRead != 0) {
      return compareToRead;
    }
    return rhs.pubdate.compareTo(lhs.pubdate);
  }
}
