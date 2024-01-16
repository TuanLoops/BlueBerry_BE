package com.blueberry.model.dto;

import com.blueberry.model.dto.converter.AppUserDTOConverter;
import com.blueberry.model.dto.converter.StatusDTOConverter;
import com.blueberry.model.dto.converter.UserDetailsConverter;
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
