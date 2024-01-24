package com.blueberry.model.dto;

import com.blueberry.model.app.NotificationType;
import com.google.cloud.Timestamp;
import lombok.Data;

@Data
public class NotificationFireStoreDTO {
    private Long id;
    private NotificationType type;
    private AppUserDTO sender;
    private AppUserDTO receiver;
    private Long statusId;
    private String statusAuthorName;
    private Timestamp timeStamp;
    private Boolean isRead;
}
