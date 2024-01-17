package com.blueberry.service;

import com.blueberry.model.app.Comment;

public interface CommentService extends GenericService<Comment> {

    void deleteByStatusIdAndAuthorId(Long statusId, Long authorId);
    Iterable<Comment> findAllByStatusIdAndIsDeleted(Long statusId, Boolean deleted);

    int likeComment(Long commentId);
}
