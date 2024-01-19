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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StatusServiceImpl implements StatusService {
    private final Sort SORT_BY_TIME_DESC = Sort.by(Sort.Direction.DESC, "lastActivity");
    private StatusRepository statusRepository;
    private AppUserService appUserService;
    private FriendService friendService;

    @Override
    public Iterable<Status> findAll() {
        return statusRepository.findAll();
    }

    @Override
    public Optional<Status> findById(Long id) {
        Optional<Status> status= statusRepository.findById(id);
        AppUser appUser = appUserService.getCurrentAppUser();
        status.ifPresent(value -> value.setLiked(likedByCurrentUser(value.getLikeList(), appUser.getId())));
        return status;
    }
    @Override
    public Optional<Status> findByIdAndDeleted(Long id,boolean deleted) {
        Optional<Status> status= statusRepository.findByIdAndIsDeleted(id,deleted);
        AppUser appUser = appUserService.getCurrentAppUser();
        status.ifPresent(value -> value.setLiked(likedByCurrentUser(value.getLikeList(), appUser.getId())));
        return status;
    }

    @Override
    public Status save(Status status) {
        Status statusSave = statusRepository.save(status);
        AppUser appUser = appUserService.getCurrentAppUser();
        statusSave.setLiked(likedByCurrentUser(statusSave.getLikeList(),appUser.getId()));
        return statusSave;
    }

    @Override
    public void delete(Long id) {
        statusRepository.deleteById(id);
    }

    @Override
    public Iterable<Status> findAllByAuthor(AppUser author, AppUser currentUser) {
        List<PrivacyLevel> privacyLevels = getPrivacyLevel(author, currentUser);
        Iterable<Status> statuses = statusRepository.findAllByAuthor(author, privacyLevels);
        for (Status status : statuses) {
            status.setLiked(likedByCurrentUser(status.getLikeList(),currentUser.getId()));
        }
        return statuses;
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
    private List<PrivacyLevel> getPrivacyLevel(AppUser author, AppUser currentUser) {
        List<PrivacyLevel> privacyLevels = new ArrayList<>();
        if(Objects.equals(currentUser.getId(), author.getId())) {
            privacyLevels.add(PrivacyLevel.PRIVATE);
            privacyLevels.add(PrivacyLevel.FRIENDS);
        }else {
            if (friendService.checkFriend(currentUser, author)){
                privacyLevels.add(PrivacyLevel.FRIENDS);
            }
        }
        privacyLevels.add(PrivacyLevel.PUBLIC);
        return privacyLevels;
    }

}
