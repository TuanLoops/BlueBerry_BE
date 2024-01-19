package com.blueberry.service.impl;


import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Like;
import com.blueberry.model.app.PrivacyLevel;
import com.blueberry.model.app.Status;
import com.blueberry.repository.StatusRepository;
import com.blueberry.service.AppUserService;
import com.blueberry.service.StatusService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StatusServiceImpl implements StatusService {

    private StatusRepository statusRepository;
    private AppUserService appUserService;

    @Override
    public Iterable<Status> findAll() {
        return statusRepository.findAll();
    }

    @Override
    public Optional<Status> findById(Long id) {
        return statusRepository.findById(id);
    }

    @Override
    public Status save(Status status) {
        return statusRepository.save(status);
    }

    @Override
    public void delete(Long id) {
        statusRepository.deleteById(id);
    }

    @Override
    public Iterable<Status> findAllByAuthorId(Long id) {
        return statusRepository.findAllByAuthorId(id);
    }

    @Override
    public Iterable<Status> findAllByAuthorIdAndIsDeleted(Long authorId, boolean isDeleted) {
        return statusRepository.findAllByAuthorIdAndIsDeleted(authorId, isDeleted);
    }

    @Override
    public Iterable<Status> findAllByAuthorId(Long authorId, Sort sort) {
        AppUser appUser = appUserService.getCurrentAppUser();
        Iterable<Status> statuses = statusRepository.findAllByAuthorIdAndIsDeleted(authorId, false, sort);
        for (Status status : statuses) {
            status.setLiked(likedByCurrentUser(status.getLikeList(),appUser.getId()));
        }
        return statuses;
    }

    @Override
    public Iterable<Status> findAllByAuthorIdAndPrivacy(Long authorId, List<PrivacyLevel> privacyLevels, Sort sort) {
        return statusRepository.findAllByAuthorIdAndIsDeletedAndPrivacyLevelIn(authorId, false, privacyLevels, sort);
    }

    @Override
    public Iterable<Status> findAllByAuthorIdAndIsDeletedAndBodyContaining(Long authorId, boolean isDeleted, String query) {
        return statusRepository.findAllByAuthorIdAndIsDeletedAndBodyContaining(authorId, isDeleted, query);
    }

    @Override
    public Iterable<Status> findAllByPrivacy(AppUser user, List<AppUser> friendList) {
        AppUser appUser = appUserService.getCurrentAppUser();
        Iterable<Status> statuses = statusRepository.findAllByPrivacy(user, friendList);
        for (Status status : statuses) {
            status.setLiked(likedByCurrentUser(status.getLikeList(),appUser.getId()));
        }
        return statuses;
    }

    @Override
    public Iterable<Status> findStatusByNameContaining(AppUser user, List<AppUser> friendList, String body) {
        Iterable<Status> statuses = statusRepository.findStatusByNameContaining(user, friendList,body);
        for (Status status : statuses) {
            status.setLiked(likedByCurrentUser(status.getLikeList(),user.getId()));
        }
        return statuses;
    }

    private boolean likedByCurrentUser(List<Like> likes,Long currentUserId){
        for (Like like: likes) {
            if (Objects.equals(like.getAuthorId(), currentUserId)){
                return true;
            }
        }
        return false;
    }
}
