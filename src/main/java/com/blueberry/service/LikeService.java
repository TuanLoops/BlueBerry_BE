package com.blueberry.service;

import com.blueberry.model.app.Like;

import java.util.Optional;

public interface LikeService extends GenericService<Like> {

    Optional<Like> findByStatusIdAndAuthorId(Long statusId, Long authorId);

    void deleteByStatusIdAndAuthorId(Long statusId, Long authorId);
}
