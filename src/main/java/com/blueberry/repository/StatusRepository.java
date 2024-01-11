package com.blueberry.repository;

import com.blueberry.model.app.PrivacyLevel;
import com.blueberry.model.app.Status;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    Iterable<Status> findAllByAuthorId(Long id);

    Iterable<Status> findAllByAuthorIdAndIsDeleted(Long authorId, boolean isDeleted);
    Iterable<Status> findAllByAuthorIdAndIsDeleted(Long authorId, boolean isDeleted, Sort sort);
    Iterable<Status> findAllByAuthorIdAndIsDeletedAndPrivacyLevelIn(Long authorId, boolean isDeleted, List<PrivacyLevel> privacyLevels, Sort sort);

    Iterable<Status> findAllByAuthorIdAndIsDeletedAndBodyContaining(Long authorId, boolean isDeleted, String query);
}
