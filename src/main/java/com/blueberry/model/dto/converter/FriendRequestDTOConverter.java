package com.blueberry.model.dto.converter;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.FriendRequest;
import com.blueberry.model.dto.AppUserDTO;
import com.blueberry.model.dto.FriendRequestDTO;
import com.blueberry.util.ModelMapperUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

@AllArgsConstructor
public class FriendRequestDTOConverter implements Converter<FriendRequest, FriendRequestDTO> {

    @Override
    public FriendRequestDTO convert(MappingContext<FriendRequest, FriendRequestDTO> mappingContext) {
        FriendRequest friendRequest = mappingContext.getSource();
        FriendRequestDTO friendRequestDTO = new FriendRequestDTO();
        friendRequestDTO.setId(friendRequest.getId());
        friendRequestDTO.setStatus(friendRequest.getStatus());
        if (friendRequest.getSender()!=null){
            friendRequestDTO.setSender(AppUserDTOConverter.converter(friendRequest.getSender()));
        }
        if (friendRequest.getReceiver()!=null){
            friendRequestDTO.setReceiver(AppUserDTOConverter.converter(friendRequest.getReceiver()));
        }
        return friendRequestDTO;
    }
}
