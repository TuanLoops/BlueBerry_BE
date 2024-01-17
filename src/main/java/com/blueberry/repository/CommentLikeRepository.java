package com.blueberry.repository;

import com.blueberry.model.app.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long> {
    Optional<CommentLike> findByAuthorIdAndCommentId(long authorId, long commentId);
}
