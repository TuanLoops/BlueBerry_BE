package com.blueberry.service.impl;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Notification;
import com.blueberry.model.app.NotificationType;
import com.blueberry.model.app.Status;
import com.blueberry.repository.NotificationRepository;
import com.blueberry.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;
    private FirestoreService firestoreService;

    public Notification saveNotification(AppUser sender, AppUser receiver, NotificationType type, Status status) {
        Notification notification = new Notification();
        notification.setSender(sender);
        notification.setReceiver(receiver);
        notification.setStatus(status);
        notification.setType(type);
        notification.setTimeStamp(LocalDateTime.now());
        notification.setIsRead(false);
        Notification savedNotification = notificationRepository.save(notification);

        sendNotification(String.valueOf(receiver.getId()), savedNotification);
        return savedNotification;
    }

    public Notification saveNotification(AppUser sender, AppUser receiver, NotificationType type) {
        Notification notification = new Notification();
        notification.setSender(sender);
        notification.setReceiver(receiver);
        notification.setType(type);
        notification.setTimeStamp(LocalDateTime.now());
        notification.setIsRead(false);
        Notification savedNotification = notificationRepository.save(notification);

        sendNotification(String.valueOf(receiver.getId()), savedNotification);
        return savedNotification;
    }

    public List<Notification> getNotificationsForUser(AppUser receiver) {
        return notificationRepository.findByReceiverOrderByTimeStampDesc(receiver);
    }


    public void sendNotification(String userId, Notification notification) {
        firestoreService.saveNotification(notification);
    }
}
