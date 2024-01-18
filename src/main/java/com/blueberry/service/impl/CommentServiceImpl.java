package com.blueberry.service.impl;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Comment;
import com.blueberry.model.app.CommentLike;
import com.blueberry.model.app.Like;
import com.blueberry.model.dto.MessageResponse;
import com.blueberry.repository.CommentLikeRepository;
import com.blueberry.repository.CommentRepository;
import com.blueberry.service.AppUserService;
import com.blueberry.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private AppUserService appUserService;
    private CommentLikeRepository commentLikeRepository;
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
        Iterable<Comment> comments = commentRepository.findAllByStatusIdAndIsDeleted(statusId,deleted,SORT_BY_TIME_DESC);
        AppUser appUser = appUserService.getCurrentAppUser();
        for (Comment comment : comments) {
            comment.setLiked(isLiked(comment.getLikes(),appUser.getId()));
        }
        return comments;
    }

    @Override
    public ResponseEntity<?> likeComment(Long commentId) {
        AppUser currentUser = appUserService.getCurrentAppUser();
        Optional<CommentLike> commentLike=  commentLikeRepository.findByAuthorIdAndCommentId(currentUser.getId(),commentId);
        try {
            if(commentLike.isPresent()){
                commentLikeRepository.delete(commentLike.get());
                return new ResponseEntity<>(-1,HttpStatus.OK);
            }else{
                CommentLike commentLikeNew = new CommentLike();
                commentLikeNew.setCommentId(commentId);
                commentLikeNew.setAuthorId(currentUser.getId());
                commentLikeRepository.save(commentLikeNew);
                return new ResponseEntity<>(1, HttpStatus.OK);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new MessageResponse("Error can not save !!"),HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isLiked(List<CommentLike> likes, Long userId) {
        for (CommentLike like: likes){
            if (Objects.equals( like.getAuthorId(),userId)){
                return true;
            }
        }
        return false;
    }
}
