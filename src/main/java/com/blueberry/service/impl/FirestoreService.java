package com.blueberry.service.impl;

import com.blueberry.model.app.Notification;
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
        notificationDTO.getReceiver().setLastOnline(null);
        notificationDTO.getSender().setLastOnline(null);

        firestore.collection("notifications").document(String.valueOf(notification.getId()))
                .set(notificationDTO);
    }
}
