package com.blueberry.util;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.dto.AppUserDTO;
import com.blueberry.model.dto.UserDTO;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;



public class AppUserConverter implements Converter<AppUser, AppUserDTO> {

    @Override
    public AppUserDTO convert(MappingContext<AppUser, AppUserDTO> mappingContext) {
      AppUser source = mappingContext.getSource();
        AppUserDTO destination = new AppUserDTO();
        destination.setId(source.getId());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setDob(source.getDob());
        destination.setBannerImage(source.getBannerImage());
        destination.setAvatarImage(source.getAvatarImage());
        destination.setPhoneNumber(source.getPhoneNumber());
        destination.setAddress(source.getAddress());
        destination.setHobbies(source.getHobbies());
        destination.setFullName(source.getFirstName() +" "+ source.getLastName());
        UserDTO userDTO = new UserDTO(source.getUser().getId(),source.getUser().getEmail());
        destination.setUser(userDTO);
        return destination;
    }
}
