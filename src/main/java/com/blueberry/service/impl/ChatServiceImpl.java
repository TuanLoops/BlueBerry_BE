package com.blueberry.service.impl;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.ChatRoom;
import com.blueberry.model.app.ChatRoomType;
import com.blueberry.model.app.Message;
import com.blueberry.repository.ChatRoomRepository;
import com.blueberry.repository.MessageRepository;
import com.blueberry.service.AppUserService;
import com.blueberry.service.ChatService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {
    private ChatRoomRepository chatRoomRepository;
    private MessageRepository messageRepository;
    private AppUserService appUserService;
    private FirestoreService firestoreService;

    public ChatRoom createChatRoom(List<AppUser> participants, ChatRoomType chatRoomType) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(UUID.randomUUID().toString());
        chatRoom.setParticipants(participants);
        chatRoom.setChatRoomType(chatRoomType);
        return chatRoomRepository.save(chatRoom);
    }

    public void addParticipant(ChatRoom chatRoom, AppUser newParticipant) {
        chatRoom.getParticipants().add(newParticipant);
        chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoom> getAllChatRoomByUserId(Long userId) {
        return chatRoomRepository.findAllChatRoomsByUserId(userId, "lastActivity");
    }

    public Message sendMessage(String chatRoomId, String content) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElse(null);
        if (chatRoom == null) throw new EntityNotFoundException("Chat room not found");
        Message message = new Message();
        message.setId(UUID.randomUUID().toString());
        message.setChatRoomId(chatRoomId);
        message.setSender(appUserService.getCurrentAppUser());
        message.setContent(content);
        message.setIsEdited(false);
        message.setTimeStamp(LocalDateTime.now());
        chatRoom.getMessages().add(message);
        chatRoom.setLastActivity(LocalDateTime.now());
        messageRepository.save(message);
        chatRoom.setLastMessage(message);
        chatRoomRepository.save(chatRoom);
        firestoreService.saveChatroom(chatRoom);
        return message;
    }

    public ChatRoom createNewEmptyChatRoomWithUser(AppUser user) {
        ChatRoom chatRoom = findPrivateChatRoomWithUser(user);
        if (chatRoom != null) {
            return chatRoom;
        }
        return createChatRoom(List.of(appUserService.getCurrentAppUser(), user), ChatRoomType.PRIVATE_CHAT);
    }

    public ChatRoom findPrivateChatRoomWithUser(AppUser user) {
        List<ChatRoom> chatRooms =
                chatRoomRepository.findAllPrivateChatRoomsByUserId(appUserService.getCurrentAppUser().getId(),
                        ChatRoomType.PRIVATE_CHAT);
        for (ChatRoom chatRoom : chatRooms) {
            if (checkUserInRoom(user, chatRoom)) {
                return chatRoom;
            }
        }
        return null;
    }

    public boolean checkUserInRoom(AppUser appUser, ChatRoom chatRoom) {
        return chatRoom.getParticipants().stream().anyMatch(u -> u.getId().equals(appUser.getId()));
    }

}
