package com.blueberry.repository;


import com.blueberry.model.app.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Iterable<Comment> findAllByStatusIdAndIsDeleted(Long statusId, Boolean deleted);

    void deleteByStatusIdAndAuthorId(Long statusId, Long authorId);


}
