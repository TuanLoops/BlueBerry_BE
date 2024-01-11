package com.blueberry.util;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Status;
import com.blueberry.model.dto.AppUserDTO;
import com.blueberry.model.dto.StatusDTO;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class StatusDTOConverter implements Converter<Status, StatusDTO> {
    @Override
    public StatusDTO convert(MappingContext<Status, StatusDTO> mappingContext) {
        Status status = mappingContext.getSource();
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setId(status.getId());
        statusDTO.setBody(status.getBody());
        statusDTO.setUpdated(status.isUpdated());
        statusDTO.setCreatedAt(status.getCreatedAt());
        statusDTO.setComment(status.getCommentList().size());
        statusDTO.setLike(status.getLikeList().size());
        statusDTO.setImageList(status.getImageList());
        statusDTO.setPrivacyLevel(status.getPrivacyLevel());
        statusDTO.setAuthor(getAppUserDTO(status.getAuthor()));
        return statusDTO;
    }
    private AppUserDTO getAppUserDTO(AppUser appUser) {
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setFirstName(appUser.getFirstName());
        appUserDTO.setId(appUser.getId());
        appUserDTO.setLastName(appUser.getLastName());
        appUserDTO.setBannerImage(appUser.getBannerImage());
        appUserDTO.setAvatarImage(appUser.getAvatarImage());
        appUserDTO.setFullName(appUser.getFirstName() +" "+ appUser.getLastName());
        return appUserDTO;
    }
}
