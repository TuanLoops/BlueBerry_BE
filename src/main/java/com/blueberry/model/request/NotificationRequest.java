package com.blueberry.model.request;

import lombok.Data;

@Data
public class NotificationRequest {
    private Long id;
    private Long statusId;
    private Boolean isRead;
}
