package com.blueberry.controller;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Notification;
import com.blueberry.model.dto.NotificationDTO;
import com.blueberry.service.AppUserService;
import com.blueberry.service.NotificationService;
import com.blueberry.util.ModelMapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
@RequestMapping("/auth/api/notification")
public class NotificationController {

    private NotificationService notificationService;
    private AppUserService appUserService;
    private ModelMapperUtil modelMapperUtil;

//    @MessageMapping("/sendNotification")
//    @SendToUser("/queue/notifications")
//    public Notification sendNotification(Notification notification) {
//        return notificationService.saveNotification(notification);
//    }

    @GetMapping("")
    public ResponseEntity<List<NotificationDTO>> getNotificationsForUser() {
        AppUser user = appUserService.getCurrentAppUser();
        return new ResponseEntity<>(modelMapperUtil.mapList(notificationService.getNotificationsForUser(user),
                NotificationDTO.class), HttpStatus.OK);
    }
}