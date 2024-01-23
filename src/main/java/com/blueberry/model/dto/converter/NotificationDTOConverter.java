package com.blueberry.model.dto.converter;

import com.blueberry.model.app.Notification;
import com.blueberry.model.dto.AppUserDTO;
import com.blueberry.model.dto.NotificationDTO;
import com.blueberry.util.ModelMapperUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class NotificationDTOConverter implements Converter<Notification, NotificationDTO> {

    public NotificationDTO convert(MappingContext<Notification, NotificationDTO> mappingContext) {
        Notification notification = mappingContext.getSource();
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notification.getId());
        notificationDTO.setType(notification.getType());
        notificationDTO.setSender(AppUserDTOConverter.converter(notification.getSender()));
        notificationDTO.setReceiver(AppUserDTOConverter.converter(notification.getReceiver()));
        if (notification.getStatus() != null) {
            notificationDTO.setStatusId(notification.getStatus().getId());
            notificationDTO.setStatusAuthorName(notification.getStatus().getAuthor().getFirstName()+' '+notification.getStatus().getAuthor().getLastName());
        }
        notificationDTO.setTimeStamp(notification.getTimeStamp());
        notificationDTO.setIsRead(notification.getIsRead());
        return notificationDTO;
    }
}
