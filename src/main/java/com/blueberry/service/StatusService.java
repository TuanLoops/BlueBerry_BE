package com.blueberry.service;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.PrivacyLevel;
import com.blueberry.model.app.Status;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatusService extends GenericService<Status>{

    Iterable<Status> findAllByAuthorId(Long id);

    Iterable<Status> findAllByAuthorIdAndIsDeleted(Long authorId, boolean isDeleted);
    Iterable<Status> findAllByAuthorId(Long authorId, Sort sort);
    Iterable<Status> findAllByAuthorIdAndPrivacy(Long authorId, List<PrivacyLevel> privacyLevels, Sort sort);
    Iterable<Status> findAllByAuthorIdAndIsDeletedAndBodyContaining(Long authorId, boolean isDeleted, String query);
    Iterable<Status> findAllByPrivacy(AppUser user, List<AppUser> friendList);
    Iterable<Status> findStatusByNameContaining(AppUser user, List<AppUser> friendList, String body);
}
