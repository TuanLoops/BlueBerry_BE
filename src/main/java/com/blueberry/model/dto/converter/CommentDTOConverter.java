package com.blueberry.model.dto.converter;

import com.blueberry.model.app.Comment;
import com.blueberry.model.dto.AppUserDTO;
import com.blueberry.model.dto.CommentDTO;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class CommentDTOConverter implements Converter<Comment, CommentDTO> {
    @Override
    public CommentDTO convert(MappingContext<Comment, CommentDTO> mappingContext) {
        Comment comment = mappingContext.getSource();
        CommentDTO commentDTO = new CommentDTO();
        AppUserDTO appUserDTO = AppUserDTOConverter.converter(comment.getAuthor());
        commentDTO.setId(comment.getId());
        commentDTO.setUpdated(comment.isUpdated());
        commentDTO.setBody(comment.getBody());
        commentDTO.setStatusId(comment.getStatusId());
        commentDTO.setAuthor(appUserDTO);
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setImage(comment.getImage());
        return null;
    }
}
