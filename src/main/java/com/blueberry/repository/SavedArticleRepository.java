package com.blueberry.repository;

import com.blueberry.model.app.SavedArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedArticleRepository extends JpaRepository<SavedArticle, Long> {
    List<SavedArticle> findByAppUser_Id(Long userId);
}
