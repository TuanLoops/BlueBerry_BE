package com.blueberry.service;

import com.blueberry.model.app.Status;

public interface StatusService extends GenericService<Status>{

    Iterable<Status> findAllByAuthorId(Long id);
}
