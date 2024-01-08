package com.blueberry.repository;

import com.blueberry.model.app.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    Iterable<Status> findAllByAuthorId(Long id);
}
