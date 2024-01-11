package com.blueberry.service.impl;

import com.blueberry.model.app.Like;
import com.blueberry.repository.LikeRepository;
import com.blueberry.service.LikeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {

    private LikeRepository likeRepository;

    @Override
    public Iterable<Like> findAll() {
        return likeRepository.findAll();
    }

    @Override
    public Optional<Like> findById(Long id) {
        return likeRepository.findById(id);
    }

    @Override
    public Like save(Like like) {
        return likeRepository.save(like);
    }

    @Override
    public void delete(Long id) {
        likeRepository.deleteById(id);
    }

    @Override
    public Optional<Like> findByStatusIdAndAuthorId(Long statusId, Long authorId) {
        return likeRepository.findByStatusIdAndAuthorId(statusId, authorId);
    }

    @Override
    public void deleteByStatusIdAndAuthorId(Long statusId, Long authorId) {
        likeRepository.deleteByStatusIdAndAuthorId(statusId, authorId);
    }
}
