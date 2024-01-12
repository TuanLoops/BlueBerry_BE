package com.blueberry.controller;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Comment;
import com.blueberry.model.app.Status;
import com.blueberry.model.dto.CommentDTO;
import com.blueberry.service.AppUserService;
import com.blueberry.service.CommentService;
import com.blueberry.service.StatusService;
import com.blueberry.service.UserService;
import com.blueberry.util.ModelMapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/auth/api/status")
@CrossOrigin("*")
@AllArgsConstructor
public class CommentController {

    private CommentService commentService;
    private StatusService statusService;
    private UserService userService;
    private AppUserService appUserService;
    private ModelMapperUtil modelMapperUtil;

    @GetMapping("/{statusId}/comments")
    public ResponseEntity<List<CommentDTO>> findAllByStatusId(@PathVariable Long statusId) {
        Status status = statusService.findById(statusId).orElse(null);

        if (status == null) {
            return ResponseEntity.notFound().build();
        }

        List<Comment> commentList = status.getCommentList();

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
        newComment.setStatus(status);
        newComment.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentService.save(newComment);

        status.getCommentList().add(savedComment);

        statusService.save(status);

        return new ResponseEntity<>(modelMapperUtil.map(savedComment,CommentDTO.class), HttpStatus.CREATED);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDTO> editCommentById(@PathVariable Long commentId, @RequestBody Comment updatedComment) {
        Comment currentComment = commentService.findById(commentId).orElse(null);

        if (currentComment == null) {
            return ResponseEntity.notFound().build();
        }

        currentComment.setBody(updatedComment.getBody());
        currentComment.setUpdatedAt(LocalDateTime.now());
        currentComment.setUpdated(true);

        Comment savedComment = commentService.save(currentComment);

        return new ResponseEntity<>(modelMapperUtil.map(savedComment,CommentDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteCommentById(@PathVariable Long commentId) {
        Comment currentComment = commentService.findById(commentId).orElse(null);

        if (currentComment == null) {
            return ResponseEntity.notFound().build();
        }

        Status status = currentComment.getStatus();
        status.getCommentList().remove(currentComment);
        statusService.save(status);

        commentService.delete(commentId);

        return new ResponseEntity<>("Deleted successful !!", HttpStatus.OK);
    }
}