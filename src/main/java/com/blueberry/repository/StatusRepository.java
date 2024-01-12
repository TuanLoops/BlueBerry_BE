package com.blueberry.repository;

import com.blueberry.model.app.PrivacyLevel;
import com.blueberry.model.app.Status;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    Iterable<Status> findAllByAuthorId(Long id);

    Iterable<Status> findAllByAuthorIdAndIsDeleted(Long authorId, boolean isDeleted);

    Iterable<Status> findAllByAuthorIdAndIsDeleted(Long authorId, boolean isDeleted, Sort sort);

    Iterable<Status> findAllByAuthorIdAndIsDeletedAndPrivacyLevelIn(Long authorId, boolean isDeleted, List<PrivacyLevel> privacyLevels, Sort sort);

    Iterable<Status> findAllByAuthorIdAndIsDeletedAndBodyContaining(Long authorId, boolean isDeleted, String query);

    @Query("SELECT s FROM Status s " +
            "    WHERE ((s.author.id = :id AND s.privacyLevel != 'PRIVATE' )OR (s.author.id IN :friends AND s.privacyLevel = 'FRIENDS')" +
            "       OR s.privacyLevel='PUBLIC' ) AND s.isDeleted = false ORDER BY s.lastActivity DESC")
    Iterable<Status> findAllByPrivacy(@Param("id") Long userId, @Param("friends") List<Long> friends);
}
