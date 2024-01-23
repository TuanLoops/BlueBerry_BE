package com.blueberry.model.dto.converter;

import com.blueberry.model.app.Notification;
import com.blueberry.model.dto.AppUserDTO;
import com.blueberry.model.dto.NotificationDTO;
import com.blueberry.util.ModelMapperUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

@AllArgsConstructor
public class NotificationDTOConverter implements Converter<Notification, NotificationDTO> {
    private ModelMapperUtil modelMapper;

    public NotificationDTO convert(MappingContext<Notification, NotificationDTO> mappingContext) {
        Notification notification = mappingContext.getSource();
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notification.getId());
        notificationDTO.setType(notification.getType());
        notificationDTO.setSender(modelMapper.map(notification.getSender(), AppUserDTO.class));
        notificationDTO.setReceiver(modelMapper.map(notification.getReceiver(), AppUserDTO.class));
        if (notification.getStatus() != null) {
            notificationDTO.setStatusId(notification.getStatus().getId());
            notificationDTO.setStatusAuthorName(modelMapper.map(notification.getStatus().getAuthor(),
                    AppUserDTO.class).getFullName());
        }
        notificationDTO.setTimeStamp(notification.getTimeStamp());
        notificationDTO.setIsRead(notification.getIsRead());
        return notificationDTO;
    }
}
