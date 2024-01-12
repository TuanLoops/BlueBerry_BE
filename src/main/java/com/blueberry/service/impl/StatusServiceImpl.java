package com.blueberry.service.impl;


import com.blueberry.model.app.PrivacyLevel;
import com.blueberry.model.app.Status;
import com.blueberry.repository.StatusRepository;
import com.blueberry.service.StatusService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StatusServiceImpl implements StatusService {

    private StatusRepository statusRepository;

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
        return statusRepository.findAllByAuthorIdAndIsDeleted(authorId, false, sort);
    }

    @Override
    public Iterable<Status> findAllByAuthorIdAndPrivaty(Long authorId, List<PrivacyLevel> privacyLevels, Sort sort) {
        return statusRepository.findAllByAuthorIdAndIsDeletedAndPrivacyLevelIn(authorId, false, privacyLevels, sort);
    }

    @Override
    public Iterable<Status> findAllByAuthorIdAndIsDeletedAndBodyContaining(Long authorId, boolean isDeleted, String query) {
        return statusRepository.findAllByAuthorIdAndIsDeletedAndBodyContaining(authorId, isDeleted, query);
    }

    @Override
    public Iterable<Status> findAllByPrivacy(Long userId, List<Long> friends) {
        return statusRepository.findAllByPrivacy(userId, friends);
    }
}
