package com.blueberry.controller;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Notification;
import com.blueberry.service.AppUserService;
import com.blueberry.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/auth/api/notifications")
public class NotificationController {

    private NotificationService notificationService;
    private AppUserService appUserService;

//    @MessageMapping("/sendNotification")
//    @SendTo("/topic/notifications")
//    public Notification sendNotification(Notification notification) {
//        return notificationService.saveNotification(notification);
//    }

    @GetMapping("")
    public List<Notification> getNotificationsForUser() {
        AppUser user = appUserService.getCurrentAppUser();
        return notificationService.getNotificationsForUser(user.getId());
    }
}