package com.blueberry.model.dto;

import com.blueberry.model.dto.converter.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.addConverter(new AppUserDTOConverter());
        modelMapper.addConverter(new UserDetailsConverter());
        modelMapper.addConverter(new StatusDTOConverter());
        modelMapper.addConverter(new CommentDTOConverter());
        modelMapper.addConverter(new NotificationDTOConverter());
        modelMapper.addConverter(new ChatRoomDTOConverter());
        modelMapper.addConverter(new MessageDTOConverter());
        return modelMapper;
    }

}
