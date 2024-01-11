package com.blueberry.service;

import com.blueberry.model.app.Comment;

public interface CommentService extends GenericService<Comment> {

    Iterable<Comment> findAllByStatusIdAndAuthorId(Long statusId, Long authorId);

    void deleteByStatusIdAndAuthorId(Long statusId, Long authorId);
}
