package com.blueberry.util;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.dto.UserDetails;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class UserDetailsConverter implements Converter<AppUser, UserDetails> {
    @Override
    public UserDetails convert(MappingContext<AppUser, UserDetails> mappingContext) {
        AppUser user = mappingContext.getSource();
        UserDetails userDetails = new UserDetails();
        userDetails.setId(user.getId());
        userDetails.setBannerImage(user.getBannerImage());
        userDetails.setAvatarImage(user.getAvatarImage());
        userDetails.setAddress(user.getAddress());
        userDetails.setFirstName(user.getFirstName());
        userDetails.setLastName(user.getLastName());
        userDetails.setDob(user.getDob());
        userDetails.setPhoneNumber(user.getPhoneNumber());
        userDetails.setFullName(user.getFirstName()+" "+user.getLastName());
        userDetails.setHobbies(user.getHobbies());
        userDetails.setEmail(user.getUser().getEmail());
        return userDetails;
    }
}
