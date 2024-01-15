package com.blueberry.controller;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Comment;
import com.blueberry.model.app.Status;
import com.blueberry.model.dto.CommentDTO;
import com.blueberry.model.dto.MessageResponse;
import com.blueberry.service.AppUserService;
import com.blueberry.service.CommentService;
import com.blueberry.service.StatusService;
import com.blueberry.service.UserService;
import com.blueberry.util.ModelMapperUtil;
import com.blueberry.util.StringTrimmer;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
        newComment.setStatus(status);
        newComment.setBody(StringTrimmer.trim(newComment.getBody()));
        newComment.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentService.save(newComment);

        status.getCommentList().add(savedComment);

        statusService.save(status);

        return new ResponseEntity<>(modelMapperUtil.map(savedComment,CommentDTO.class), HttpStatus.CREATED);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> editCommentById(@PathVariable Long commentId, @RequestBody Comment updatedComment) {
        Comment currentComment = commentService.findById(commentId).orElse(null);
        if (currentComment == null) {
            return ResponseEntity.notFound().build();
        }
        AppUser currentAppUser = appUserService.getCurrentAppUser();
        if(Objects.equals(currentAppUser.getId(), currentComment.getAuthor().getId())){
            currentComment.setBody(StringTrimmer.trim(updatedComment.getBody()));
            currentComment.setUpdatedAt(LocalDateTime.now());
            currentComment.setUpdated(true);

            Comment savedComment = commentService.save(currentComment);

            return new ResponseEntity<>(modelMapperUtil.map(savedComment,CommentDTO.class), HttpStatus.OK);
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
            Status status = currentComment.getStatus();
            status.getCommentList().remove(currentComment);
            statusService.save(status);

            commentService.delete(commentId);

            return new ResponseEntity<>(commentId, HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse("Access denied !!"), HttpStatus.FORBIDDEN);
    }
}
