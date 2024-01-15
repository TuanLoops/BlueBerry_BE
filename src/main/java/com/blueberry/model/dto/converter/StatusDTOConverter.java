package com.blueberry.util;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Comment;
import com.blueberry.model.app.Status;
import com.blueberry.model.dto.AppUserDTO;
import com.blueberry.model.dto.StatusDTO;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.List;

public class StatusDTOConverter implements Converter<Status, StatusDTO> {
    @Override
    public StatusDTO convert(MappingContext<Status, StatusDTO> mappingContext) {
        Status status = mappingContext.getSource();
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setId(status.getId());
        statusDTO.setBody(status.getBody());
        statusDTO.setUpdated(status.isUpdated());
        statusDTO.setCreatedAt(status.getCreatedAt());
        statusDTO.setComment(countComments(status.getCommentList()));
        statusDTO.setLike(status.getLikeList().size());
        statusDTO.setImageList(status.getImageList());
        statusDTO.setPrivacyLevel(status.getPrivacyLevel());
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
