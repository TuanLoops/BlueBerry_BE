package com.blueberry.model.dto;

import com.blueberry.util.AppUserDTOConverter;
import com.blueberry.util.StatusDTOConverter;
import com.blueberry.util.UserDetailsConverter;
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
        return modelMapper;
    }

}
