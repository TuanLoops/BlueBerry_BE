package com.blueberry.repository;

import com.blueberry.model.app.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByStatusIdAndAuthorId(Long statusId, Long authorId);
    void deleteByStatusIdAndAuthorId(Long statusId, Long authorId);
}
