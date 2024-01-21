package com.blueberry.service.impl;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Notification;
import com.blueberry.model.app.NotificationType;
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
//    private final SimpMessagingTemplate messagingTemplate;

    public Notification saveNotification(AppUser sender, AppUser receiver, NotificationType type) {
        Notification notification = new Notification();
        notification.setSender(sender);
        notification.setReceiver(receiver);
        notification.setType(type);
        notification.setTimeStamp(LocalDateTime.now());
        notification.setIsRead(false);
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsForUser(AppUser receiver) {
        return notificationRepository.findByReceiver(receiver);
    }


//    public void sendNotification(String username, Notification notification) {
//        messagingTemplate.convertAndSendToUser(username, "/topic/notifications", notification);
//    }
}
