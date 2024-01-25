package com.blueberry.model.dto;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.ChatRoomType;
import com.blueberry.model.app.Message;
import lombok.Data;
import java.util.List;

@Data
public class ChatRoomDTO {
    private String id;
    private List<AppUserDTO> participants;
    private List<Long> participantIds;
    private List<MessageDTO> messages;
    private MessageDTO lastMessage;
    private ChatRoomType chatRoomType;
    private String lastActivity;
}
