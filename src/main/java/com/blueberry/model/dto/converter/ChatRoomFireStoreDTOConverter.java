package com.blueberry.model.dto.converter;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.ChatRoom;
import com.blueberry.model.dto.ChatRoomDTO;
import com.blueberry.model.dto.ChatRoomFireStoreDTO;
import com.google.cloud.Timestamp;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.util.stream.Collectors;

public class ChatRoomFireStoreDTOConverter implements Converter<ChatRoom, ChatRoomFireStoreDTO> {

    @Override
    public ChatRoomFireStoreDTO convert(MappingContext<ChatRoom, ChatRoomFireStoreDTO> mappingContext) {
        ModelMapper mapper = new ModelMapper();
        ChatRoom chatRoom = mappingContext.getSource();
        ChatRoomFireStoreDTO chatRoomDTO = new ChatRoomFireStoreDTO();
        chatRoomDTO.setId(chatRoom.getId());
        chatRoomDTO.setChatRoomType(chatRoom.getChatRoomType());
        chatRoomDTO.setMessages(chatRoom.getMessages().stream().map(MessageDTOConverter::converter).collect(Collectors.toList()));
        if (!chatRoom.getMessages().isEmpty()) {
            chatRoomDTO.setLastMessage(MessageDTOConverter.converter(chatRoom.getLastMessage()));
            chatRoomDTO.setLastActivity(Timestamp.of(java.sql.Timestamp.valueOf(chatRoom.getLastActivity())));
        }
        chatRoomDTO.setParticipants(chatRoom.getParticipants().stream().map(AppUserDTOConverter::converter).collect(Collectors.toList()));
        chatRoomDTO.setParticipantIds(chatRoom.getParticipants().stream().map(AppUser::getId).collect(Collectors.toList()));
        return chatRoomDTO;
    }
}