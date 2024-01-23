package com.blueberry.service.impl;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.SavedArticle;
import com.blueberry.model.app.Status;
import com.blueberry.model.request.SavedArticleRequest;
import com.blueberry.repository.SavedArticleRepository;
import com.blueberry.service.AppUserService;
import com.blueberry.service.SavedArticleService;
import com.blueberry.service.StatusService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SavedArticleImpl implements SavedArticleService {

    final SavedArticleRepository savedArticleRepository;
    final AppUserService appUserService;
    final StatusService statusService;


    public SavedArticleImpl(SavedArticleRepository savedArticleRepository, AppUserService appUserService, StatusService statusService) {
        this.savedArticleRepository = savedArticleRepository;
        this.appUserService = appUserService;
        this.statusService = statusService;
    }
    @Override
    public Iterable<SavedArticle> findAll() {
        return savedArticleRepository.findAll();
    }

    @Override
    public Optional<SavedArticle> findById(Long id) {
        return savedArticleRepository.findById(id);
    }

    @Override
    public SavedArticle save(SavedArticle savedArticle) {
        return savedArticleRepository.save(savedArticle);
    }

    @Override
    public void delete(Long id) {
        savedArticleRepository.deleteById(id);
    }

    @Override
    public SavedArticle saveArticle(SavedArticleRequest savedArticleRequest) {
        SavedArticle savedArticle = new SavedArticle();
        AppUser appUser = appUserService.getCurrentAppUser();
        Optional<Status> status1 = statusService.findById(savedArticleRequest.getStatus().getId());
        List<SavedArticle> existingSavedArticle = savedArticleRepository.findByAppUser_Id(appUser.getId());
        for (SavedArticle existingSavedArticles : existingSavedArticle){
            Status existingStatus = existingSavedArticles.getStatus();
            if (existingStatus != null && status1.isPresent() && status1.get().getId().equals(existingStatus.getId())) {
                throw new RuntimeException("The article has been saved, there is no need to save it");
            }
        }
        savedArticle.setStatus(status1.get());
        savedArticle.setAppUser(appUser);
        return savedArticleRepository.save(savedArticle);
    }



    @Override
    public void unSaveArticle(Long userId, Long articleId) {
        Optional<SavedArticle> savedArticleOptional = savedArticleRepository.findById(articleId);
        if (savedArticleOptional.isPresent()) {
            SavedArticle savedArticle = savedArticleOptional.get();
            if (savedArticle.getAppUser().getId().equals(userId)) {
                savedArticleRepository.delete(savedArticle);
            } else {
            }
        } else {
        }
    }

    @Override
    public List<SavedArticle> getSavedArticles(Long userId) {
        return savedArticleRepository.findByAppUser_Id(userId);
    }
}
