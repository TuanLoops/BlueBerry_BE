package com.blueberry.model.dto.converter;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.dto.UserDetails;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

public class UserDetailsConverter implements Converter<AppUser, UserDetails> {
    @Override
    public UserDetails convert(MappingContext<AppUser, UserDetails> mappingContext) {
        ModelMapper mapper = new ModelMapper();
        AppUser user = mappingContext.getSource();
        UserDetails userDetails = mapper.map(user,UserDetails.class);
        userDetails.setFullName(user.getFirstName()+" "+user.getLastName());
        userDetails.setEmail(user.getUser().getEmail());
        return userDetails;
    }
}
