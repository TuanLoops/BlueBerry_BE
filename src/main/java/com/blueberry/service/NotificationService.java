package com.blueberry.service;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Notification;
import com.blueberry.model.app.NotificationType;
import com.blueberry.model.app.Status;

import java.util.List;
import java.util.Optional;

public interface NotificationService {

    Notification saveNotification(AppUser sender, AppUser receiver, NotificationType type, Status status);
    Notification saveNotification(AppUser sender, AppUser receiver, NotificationType type);

    List<Notification> getNotificationsForUser(AppUser receiver);

    void sendNotification(String userId, Notification notification);

    Optional<Notification> findById(Long id);

    Notification save( Notification notification);
    Iterable<Notification> saveAll(Iterable<Notification> notifications);

    Iterable<Notification> findAll();

}