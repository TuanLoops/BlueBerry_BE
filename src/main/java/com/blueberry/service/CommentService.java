package com.blueberry.service;

import com.blueberry.model.app.Comment;
import org.springframework.http.ResponseEntity;

public interface CommentService extends GenericService<Comment> {

    void deleteByStatusIdAndAuthorId(Long statusId, Long authorId);
    Iterable<Comment> findAllByStatusIdAndIsDeleted(Long statusId, Boolean deleted);

    ResponseEntity<?> likeComment(Long commentId);
}
