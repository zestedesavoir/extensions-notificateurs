package com.zestedesavoir.zdsnotificateur.ui.notifications;

import android.widget.Filter;

import com.zestedesavoir.zdsnotificateur.internal.list.Stream;
import com.zestedesavoir.zdsnotificateur.notifications.Notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Gerard Paligot
 */
public class TypeFilter extends Filter {
  private static final List<String> FILTERS = Stream.of(Type.values()).map(Type::key).filter(value -> !"".equals(value)).toList();
  private final NotificationsAdapter adapter;

  public TypeFilter(NotificationsAdapter adapter) {
    this.adapter = adapter;
  }

  @Override protected FilterResults performFiltering(CharSequence constraint) {
    final FilterResults results = new FilterResults();
    final Stream<Notification> stream = Stream.of(new ArrayList<>(adapter.getNotifications()));

    final List<String> filters = Arrays.asList(constraint.toString().split(","));
    if (filters.size() != 0 && FILTERS.containsAll(filters)) {
      stream.filter(value -> filters.contains(value.contentType));
    }
    stream.sorted((lhs, rhs) -> new NotificationComparator().compare(lhs, rhs));

    final List<Notification> notifications = stream.toList();
    results.count = notifications.size();
    results.values = notifications;
    return results;
  }

  @Override protected void publishResults(CharSequence constraint, FilterResults results) {
    adapter.updateNotifications((List<Notification>) results.values);
  }
}
