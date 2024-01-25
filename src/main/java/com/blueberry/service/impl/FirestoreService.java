package com.blueberry.service.impl;

import com.blueberry.model.app.ChatRoom;
import com.blueberry.model.app.Notification;
import com.blueberry.model.dto.ChatRoomDTO;
import com.blueberry.model.dto.NotificationDTO;
import com.blueberry.model.dto.NotificationFireStoreDTO;
import com.blueberry.util.ModelMapperUtil;
import com.google.cloud.firestore.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FirestoreService {

    private final Firestore firestore;
    private ModelMapperUtil modelMapperUtil;

    public void saveNotification(Notification notification) {
        NotificationFireStoreDTO notificationDTO = modelMapperUtil.map(notification, NotificationFireStoreDTO.class);

        firestore.collection("notifications").document(notification.getId())
                .set(notificationDTO);
    }

    public void saveChatroom(ChatRoom chatRoom) {
        ChatRoomDTO chatRoomDTO = modelMapperUtil.map(chatRoom, ChatRoomDTO.class);

        firestore.collection("chat_rooms").document(chatRoom.getId()).set(chatRoomDTO);
    }
}
