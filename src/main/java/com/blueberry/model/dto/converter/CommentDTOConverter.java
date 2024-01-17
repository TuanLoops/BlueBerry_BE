package com.blueberry.model.dto.converter;

import com.blueberry.model.app.Comment;
import com.blueberry.model.dto.AppUserDTO;
import com.blueberry.model.dto.CommentDTO;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

public class CommentDTOConverter implements Converter<Comment, CommentDTO> {
    @Override
    public CommentDTO convert(MappingContext<Comment, CommentDTO> mappingContext) {
        ModelMapper mapper = new ModelMapper();
        Comment comment = mappingContext.getSource();
        CommentDTO commentDTO = mapper.map(comment,CommentDTO.class);
        AppUserDTO appUserDTO = AppUserDTOConverter.converter(comment.getAuthor());
        commentDTO.setAuthor(appUserDTO);
        commentDTO.setLiked(comment.isLiked());
        commentDTO.setCountLikes(comment.getLikes().size());
        return commentDTO;
    }
}
