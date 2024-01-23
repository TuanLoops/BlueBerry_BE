package com.blueberry.model.dto;

import com.blueberry.model.app.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO{
    private Long id;
    private NotificationType type;
    private AppUserDTO sender;
    private AppUserDTO receiver;
    private Long statusId;
    private String statusAuthorName;
    private LocalDateTime timeStamp;
    private Boolean isRead;
}
