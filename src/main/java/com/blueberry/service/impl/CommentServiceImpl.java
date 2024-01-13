package com.blueberry.service.impl;

import com.blueberry.model.app.Comment;
import com.blueberry.repository.CommentRepository;
import com.blueberry.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

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
    public Iterable<Comment> findAllByStatusIdAndAuthorId(Long statusId, Long authorId) {
        return commentRepository.findAllByStatusIdAndAuthorId(statusId, authorId);
    }

    @Override
    public void deleteByStatusIdAndAuthorId(Long statusId, Long authorId) {
        commentRepository.deleteByStatusIdAndAuthorId(statusId, authorId);
    }
}
