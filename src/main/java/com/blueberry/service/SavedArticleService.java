package com.blueberry.service;

import com.blueberry.model.app.SavedArticle;
import com.blueberry.model.request.SavedArticleRequest;

import java.util.List;

public interface SavedArticleService extends GenericService<SavedArticle> {
    SavedArticle saveArticle(SavedArticleRequest savedArticleRequest);
    void unSaveArticle(Long userId, Long articleId);
    List<SavedArticle> getSavedArticles(Long userId);
}
