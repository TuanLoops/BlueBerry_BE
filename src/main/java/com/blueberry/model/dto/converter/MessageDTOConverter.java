package com.blueberry.model.dto.converter;

import com.blueberry.model.app.Message;
import com.blueberry.model.dto.AppUserDTO;
import com.blueberry.model.dto.MessageDTO;
import com.blueberry.util.ModelMapperUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class MessageDTOConverter implements Converter<Message, MessageDTO> {
    @Override
    public MessageDTO convert(MappingContext<Message, MessageDTO> mappingContext) {
        Message message = mappingContext.getSource();
        return converter(message);
    }

    public static MessageDTO converter(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setContent(message.getContent());
        messageDTO.setSender(AppUserDTOConverter.converter(message.getSender()));
        messageDTO.setIsEdited(message.getIsEdited());
        messageDTO.setTimeStamp(message.getTimeStamp().toString());
        messageDTO.setChatRoomId(message.getChatRoomId());
        return messageDTO;
    }
}
