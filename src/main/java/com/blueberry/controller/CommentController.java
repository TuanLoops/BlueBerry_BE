package com.blueberry.controller;

import com.blueberry.model.app.*;
import com.blueberry.model.dto.CommentDTO;
import com.blueberry.model.dto.MessageResponse;
import com.blueberry.service.*;
import com.blueberry.service.impl.FollowServiceImpl;
import com.blueberry.util.ModelMapperUtil;
import com.blueberry.util.StringTrimmer;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/auth/api/status")
@CrossOrigin("*")
@AllArgsConstructor
public class CommentController {

    private CommentService commentService;
    private StatusService statusService;
    private AppUserService appUserService;
    private ModelMapperUtil modelMapperUtil;
    private NotificationService notificationService;
    private FollowService followService;

    @GetMapping("/{statusId}/comments")
    public ResponseEntity<List<CommentDTO>> findAllByStatusId(@PathVariable Long statusId) {
        Status status = statusService.findById(statusId).orElse(null);

        if (status == null) {
            return ResponseEntity.notFound().build();
        }
        List<Comment> commentList = (List<Comment>) commentService.findAllByStatusIdAndIsDeleted(statusId,false);
        return new ResponseEntity<>(modelMapperUtil.mapList(commentList,CommentDTO.class), HttpStatus.OK);
    }

    @PostMapping("/{statusId}/comments")
    public ResponseEntity<CommentDTO> addCommentByStatusId(@PathVariable Long statusId, @RequestBody Comment newComment) {
        Status status = statusService.findById(statusId).orElse(null);
        AppUser currentAppUser = appUserService.getCurrentAppUser();
        if (status == null) {
            return ResponseEntity.notFound().build();
        }
        newComment.setAuthor(currentAppUser);
        newComment.setStatusId(status.getId());
        newComment.setBody(StringTrimmer.trim(newComment.getBody()));
        newComment.setLikes(new ArrayList<>());
        newComment.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentService.save(newComment);

        status.getCommentList().add(savedComment);
        status.setLastActivity(LocalDateTime.now());
        statusService.save(status);
        if (!currentAppUser.getId().equals(status.getAuthor().getId())) {
            followService.followStatus(currentAppUser, status);
            notificationService.saveNotification(currentAppUser, status.getAuthor(), NotificationType.COMMENT_ON_OWN_POST, status);
        }
        List<AppUser> followers = followService.findByStatus(status).getFollowers();
        for (AppUser follower: followers) {
            if (!Objects.equals(follower.getId(), currentAppUser.getId())) {
            notificationService.saveNotification(currentAppUser, follower, NotificationType.COMMENT_ON_FOLLOWED_POST, status);
            }
        }


        return new ResponseEntity<>(modelMapperUtil.map(savedComment,CommentDTO.class), HttpStatus.CREATED);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> editCommentById(@PathVariable Long commentId, @RequestBody Comment updatedComment) {
        Comment currentComment = commentService.findById(commentId).orElse(null);
        if (currentComment == null) {
            return new ResponseEntity<>(new MessageResponse("Not Found !!"),HttpStatus.NOT_FOUND);
        }
        AppUser currentAppUser = appUserService.getCurrentAppUser();
        if(Objects.equals(currentAppUser.getId(), currentComment.getAuthor().getId())){
            try {
                currentComment.setBody(StringTrimmer.trim(updatedComment.getBody()));
                currentComment.setUpdatedAt(LocalDateTime.now());
                currentComment.setUpdated(true);
                currentComment.setImage(updatedComment.getImage());
                Comment savedComment = commentService.save(currentComment);

                return new ResponseEntity<>(modelMapperUtil.map(savedComment,CommentDTO.class), HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity<>(new MessageResponse("Can't save comment!"),HttpStatus.BAD_REQUEST);
            }

        }
        return new ResponseEntity<>(new MessageResponse("Access denied!"),HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteCommentById(@PathVariable Long commentId) {
        Comment currentComment = commentService.findById(commentId).orElse(null);

        if (currentComment == null) {
            return new ResponseEntity<>(new MessageResponse("Deleted successful !!"),HttpStatus.NOT_FOUND);
        }
        AppUser currentAppUser = appUserService.getCurrentAppUser();
        if (Objects.equals(currentAppUser.getId(), currentComment.getAuthor().getId())) {
            currentComment.setDeleted(true);
            commentService.save(currentComment);
            return new ResponseEntity<>(commentId, HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse("Access denied !!"), HttpStatus.FORBIDDEN);
    }
    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<?> likeComment(@PathVariable Long commentId) {
        return commentService.likeComment(commentId);
    }
}
