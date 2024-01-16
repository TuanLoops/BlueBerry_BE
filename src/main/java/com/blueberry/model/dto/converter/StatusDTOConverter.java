package com.blueberry.model.dto.converter;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Comment;
import com.blueberry.model.app.Like;
import com.blueberry.model.app.Status;
import com.blueberry.model.dto.StatusDTO;
import com.blueberry.service.AppUserService;
import com.blueberry.service.impl.AppUserServiceImpl;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

public class StatusDTOConverter implements Converter<Status, StatusDTO> {
    @Override
    public StatusDTO convert(MappingContext<Status, StatusDTO> mappingContext) {
        ModelMapper mapper = new ModelMapper();
        Status status = mappingContext.getSource();
        StatusDTO statusDTO = mapper.map(status,StatusDTO.class);
        statusDTO.setCountComments(countComments(status.getCommentList()));
        statusDTO.setCountLikes(status.getLikeList().size());
        statusDTO.setAuthor(AppUserDTOConverter.converter(status.getAuthor()));
        return statusDTO;
    }
    private int countComments(List<Comment> list){
        int count = 0;
        for (Comment comment : list){
            if(comment.isDeleted()){
                break;
            }
            count++;
        }
        return count;
    }
}
