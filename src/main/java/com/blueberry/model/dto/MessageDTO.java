package com.blueberry.model.dto;

import lombok.Data;

@Data
public class MessageDTO {
    private String id;
    private String chatRoomId;
    private AppUserDTO sender;
    private String content;
    private String timeStamp;
    private Boolean isEdited;
}
