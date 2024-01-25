package com.blueberry.model.dto.converter;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.ChatRoom;
import com.blueberry.model.dto.AppUserDTO;
import com.blueberry.model.dto.ChatRoomDTO;
import com.blueberry.model.dto.MessageDTO;
import com.blueberry.util.ModelMapperUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.util.stream.Collectors;

public class ChatRoomDTOConverter implements Converter<ChatRoom, ChatRoomDTO> {

    @Override
    public ChatRoomDTO convert(MappingContext<ChatRoom, ChatRoomDTO> mappingContext) {
        ModelMapper mapper = new ModelMapper();
        ChatRoom chatRoom = mappingContext.getSource();
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        chatRoomDTO.setId(chatRoom.getId());
        chatRoomDTO.setChatRoomType(chatRoom.getChatRoomType());
        chatRoomDTO.setMessages(chatRoom.getMessages().stream().map(MessageDTOConverter::converter).collect(Collectors.toList()));
        if (!chatRoom.getMessages().isEmpty()) {
            chatRoomDTO.setLastMessage(MessageDTOConverter.converter(chatRoom.getLastMessage()));
            chatRoomDTO.setLastActivity(chatRoom.getLastActivity().toString());
        }
        chatRoomDTO.setParticipants(chatRoom.getParticipants().stream().map(AppUserDTOConverter::converter).collect(Collectors.toList()));
        chatRoomDTO.setParticipantIds(chatRoom.getParticipants().stream().map(AppUser::getId).collect(Collectors.toList()));
        return chatRoomDTO;
    }
}
