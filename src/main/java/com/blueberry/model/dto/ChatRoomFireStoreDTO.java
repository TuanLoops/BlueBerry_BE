package com.blueberry.model.dto;

import com.blueberry.model.app.ChatRoomType;
import com.google.cloud.Timestamp;
import lombok.Data;

import java.util.List;

@Data
public class ChatRoomFireStoreDTO {
    private String id;
    private List<AppUserDTO> participants;
    private List<Long> participantIds;
    private List<MessageDTO> messages;
    private MessageDTO lastMessage;
    private ChatRoomType chatRoomType;
    private Timestamp lastActivity;
}
