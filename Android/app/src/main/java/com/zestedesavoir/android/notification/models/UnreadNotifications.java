package com.zestedesavoir.android.notification.models;

import java.util.List;

public final class UnreadNotifications {
    public final List<Notification> notifications;
    public final boolean shouldGenerateNotification;

    public UnreadNotifications(List<Notification> notifications, boolean shouldGenerateNotification) {
        this.notifications = notifications;
        this.shouldGenerateNotification = shouldGenerateNotification;
    }
}