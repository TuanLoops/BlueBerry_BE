package com.blueberry.service;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.ChatRoom;
import com.blueberry.model.app.ChatRoomType;
import com.blueberry.model.app.Message;

import java.util.List;

public interface ChatService {
    ChatRoom createChatRoom(List<AppUser> participants, ChatRoomType chatRoomType);

    void addParticipant(ChatRoom chatRoom, AppUser newParticipant);

    List<ChatRoom> getAllChatRoomByUserId(Long userId);

    Message sendMessage(String chatRoomId, String content);

    ChatRoom createNewEmptyChatRoomWithUser(AppUser user);

    ChatRoom findPrivateChatRoomWithUser(AppUser user);
}
