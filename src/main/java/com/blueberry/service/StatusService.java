package com.blueberry.service;

import com.blueberry.model.app.Status;
import org.springframework.data.domain.Sort;

public interface StatusService extends GenericService<Status>{

    Iterable<Status> findAllByAuthorId(Long id);

    Iterable<Status> findAllByAuthorIdAndIsDeleted(Long authorId, boolean isDeleted);
    Iterable<Status> findAllByAuthorId(Long authorId, Sort sort);

    Iterable<Status> findAllByAuthorIdAndIsDeletedAndBodyContaining(Long authorId, boolean isDeleted, String query);
}
