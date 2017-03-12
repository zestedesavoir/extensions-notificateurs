package com.zestedesavoir.android.notification.managers;

import com.zestedesavoir.android.notification.models.Notification;
import com.zestedesavoir.android.notification.models.UnreadNotifications;

import java.util.List;

import rx.Observable;

/**
 * Notification is created by Zeste de Savoir due to some actions like a new message posted by
 * another member or a new private message so you can't create a notification by yourself. This
 * manager is a read only manager.
 *
 * @author Gerard Paligot
 */
public interface NotificationsManager {
    /**
     * Get all notifications of the user authenticated.
     *
     * @param page
     */
    Observable<List<Notification>> getAll(int page);

    /**
     * Gets all unread notifications of the user authenticated and not generated in an android
     * notification.
     *
     * @param page
     */
    Observable<UnreadNotifications> getAllUnread(int page);

    /**
     * Marks all notifications as closed.
     */
    Observable<Void> clear();
}