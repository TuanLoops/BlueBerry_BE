package com.blueberry.controller;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Notification;
import com.blueberry.model.dto.NotificationDTO;
import com.blueberry.model.request.NotificationRequest;
import com.blueberry.service.AppUserService;
import com.blueberry.service.NotificationService;
import com.blueberry.util.ModelMapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @PutMapping("{id}")
    public ResponseEntity<?> updateNotification( @PathVariable Long id, @RequestBody NotificationRequest notificationRequest){
        Optional<Notification> notification = notificationService.findById(id);
        if (notification.isPresent()){
            notification.get().setIsRead(notificationRequest.getIsRead());
        }
        notificationService.save(notification.get());
        return new ResponseEntity<>( notification, HttpStatus.OK);
    }

    @PutMapping("/all")
    public ResponseEntity<?> updateAllNotification(@RequestBody NotificationRequest notificationRequest){
        Iterable<Notification> notifications = notificationService.findAll();
        notifications.forEach(notification -> {
            notification.setIsRead(notificationRequest.getIsRead());
        });
        Iterable<Notification> updatedNotifications = notificationService.saveAll(notifications);
        return new ResponseEntity<>(updatedNotifications, HttpStatus.OK);
    }
}