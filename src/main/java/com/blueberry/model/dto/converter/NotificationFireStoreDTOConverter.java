package com.blueberry.model.dto.converter;

import com.blueberry.model.app.Notification;
import com.blueberry.model.dto.NotificationDTO;
import com.blueberry.model.dto.NotificationFireStoreDTO;
import com.google.cloud.Timestamp;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class NotificationFireStoreDTOConverter implements Converter<Notification, NotificationFireStoreDTO> {
    public NotificationFireStoreDTO convert(MappingContext<Notification, NotificationFireStoreDTO> mappingContext) {
        Notification notification = mappingContext.getSource();
        NotificationFireStoreDTO notificationDTO = new NotificationFireStoreDTO();
        notificationDTO.setId(notification.getId());
        notificationDTO.setType(notification.getType());
        notificationDTO.setSender(AppUserDTOConverter.converter(notification.getSender()));
        notificationDTO.setReceiver(AppUserDTOConverter.converter(notification.getReceiver()));
        if (notification.getStatus() != null) {
            notificationDTO.setStatusId(notification.getStatus().getId());
            notificationDTO.setStatusAuthorName(notification.getStatus().getAuthor().getFirstName() + ' ' + notification.getStatus().getAuthor().getLastName());
        }
        notificationDTO.setTimeStamp(Timestamp.of(java.sql.Timestamp.valueOf(notification.getTimeStamp())));
        notificationDTO.setIsRead(notification.getIsRead());
        return notificationDTO;
    }
}
