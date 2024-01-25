package com.blueberry.controller;

import com.blueberry.model.app.SavedArticle;
import com.blueberry.model.request.SavedArticleRequest;
import com.blueberry.service.impl.SavedArticleImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("auth/api/saved")
public class SavedArticleController {

    final SavedArticleImpl savedArticleImpl;

    public SavedArticleController(SavedArticleImpl savedArticle) {
        this.savedArticleImpl = savedArticle;
    }

    @GetMapping
    public ResponseEntity<List<SavedArticle>> getSavedArticles(@RequestParam("userId") Long userId) {
        List<SavedArticle> savedArticles = savedArticleImpl.getSavedArticles(userId);
        return ResponseEntity.ok(savedArticles);
    }

    @PostMapping
    public ResponseEntity<SavedArticle> saveArticle(@RequestBody SavedArticleRequest savedArticleRequest) {
        try {
            SavedArticle savedArticle = savedArticleImpl.saveArticle(savedArticleRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
        }catch (RuntimeException e){
            String errorMessage = e.getMessage();
            return new ResponseEntity(errorMessage, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> unSaveArticle(@PathVariable("articleId") Long articleId,
                                              @RequestParam("userId") Long userId) {
        savedArticleImpl.unSaveArticle(userId, articleId);
        return ResponseEntity.noContent().build();
    }
}
