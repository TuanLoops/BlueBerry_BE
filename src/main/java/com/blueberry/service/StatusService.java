package com.blueberry.service;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.PrivacyLevel;
import com.blueberry.model.app.Status;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StatusService extends GenericService<Status>{
    Optional<Status> findByIdAndDeleted(Long id, boolean deleted);
    Iterable<Status> findAllByAuthor(AppUser currentUser, AppUser user);
    Iterable<Status> findAllByPrivacy(AppUser user, List<AppUser> friendList);
    Iterable<Status> findStatusByNameContaining(AppUser user, List<AppUser> friendList, String body);
}
