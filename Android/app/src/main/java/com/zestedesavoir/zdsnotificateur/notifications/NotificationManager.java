package com.zestedesavoir.zdsnotificateur.notifications;

import com.zestedesavoir.zdsnotificateur.internal.Callback;

import java.util.List;

/**
 * Notification is created by Zeste de Savoir due to some actions like a new message posted by
 * another member or a new private message so you can't create a notification by yourself. This
 * manager is a read only manager.
 *
 * @author Gerard Paligot
 */
public interface NotificationManager {
  /**
   * Get all notifications of the user authentified.
   *
   * @param callback Callback for the response with the list of notifications.
   */
  void getAll(Callback<List<Notification>> callback);
}
