package com.blueberry.service.impl;

import com.blueberry.model.app.Comment;
import com.blueberry.repository.CommentRepository;
import com.blueberry.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private final Sort SORT_BY_TIME_DESC = Sort.by(Sort.Direction.DESC, "createdAt");

    @Override
    public Iterable<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public void deleteByStatusIdAndAuthorId(Long statusId, Long authorId) {
        commentRepository.deleteByStatusIdAndAuthorId(statusId, authorId);
    }

    @Override
    public Iterable<Comment> findAllByStatusIdAndIsDeleted(Long statusId, Boolean deleted) {
        return commentRepository.findAllByStatusIdAndIsDeleted(statusId,deleted,SORT_BY_TIME_DESC);
    }
}
