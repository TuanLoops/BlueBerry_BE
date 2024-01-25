package com.blueberry.model.dto.converter;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.dto.AppUserDTO;
import com.blueberry.model.dto.UserDTO;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;


public class AppUserDTOConverter implements Converter<AppUser, AppUserDTO> {

    @Override
    public AppUserDTO convert(MappingContext<AppUser, AppUserDTO> mappingContext) {
        AppUser source = mappingContext.getSource();
        return converter(source);
    }

    public static AppUserDTO converter(AppUser appUser) {
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setId(appUser.getId());
        appUserDTO.setFirstName(appUser.getFirstName());
        appUserDTO.setLastName(appUser.getLastName());
        appUserDTO.setBannerImage(appUser.getBannerImage());
        appUserDTO.setAvatarImage(appUser.getAvatarImage());
        appUserDTO.setFullName(appUser.getFirstName() + " " + appUser.getLastName());
        appUserDTO.setLastOnline(appUser.getLastOnline().toString());
        return appUserDTO;
    }
}
