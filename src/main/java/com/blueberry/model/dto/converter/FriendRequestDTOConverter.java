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

    private ModelMapperUtil modelMapper;

    @Override
    public FriendRequestDTO convert(MappingContext<FriendRequest, FriendRequestDTO> mappingContext) {
        FriendRequest friendRequest = mappingContext.getSource();
        FriendRequestDTO friendRequestDTO = new FriendRequestDTO();
        friendRequestDTO.setId(friendRequest.getId());
        friendRequestDTO.setStatus(friendRequest.getStatus());
        friendRequestDTO.setSender(modelMapper.map(friendRequest.getSender(),
                AppUserDTO.class));
        friendRequestDTO.setReceiver(modelMapper.map(friendRequest.getReceiver(),
                AppUserDTO.class));
        return friendRequestDTO;
    }
}
