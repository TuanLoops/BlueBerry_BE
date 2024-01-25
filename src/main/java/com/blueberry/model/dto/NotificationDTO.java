package com.blueberry.model.dto;

import com.blueberry.model.app.NotificationType;
import lombok.Data;

@Data
public class NotificationDTO{
    private String id;
    private NotificationType type;
    private AppUserDTO sender;
    private AppUserDTO receiver;
    private Long statusId;
    private String statusAuthorName;
    private String timeStamp;
    private Boolean isRead;
}
