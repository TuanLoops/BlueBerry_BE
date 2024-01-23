package com.blueberry.service;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Notification;
import com.blueberry.model.app.NotificationType;
import com.blueberry.model.app.Status;

import java.util.List;

public interface NotificationService {

    Notification saveNotification(AppUser sender, AppUser receiver, NotificationType type, Status status);
    Notification saveNotification(AppUser sender, AppUser receiver, NotificationType type);

    List<Notification> getNotificationsForUser(AppUser receiver);

    void sendNotification(String userId, Notification notification);
}