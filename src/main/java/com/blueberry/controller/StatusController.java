package com.blueberry.controller;

import com.blueberry.model.acc.User;
import com.blueberry.model.app.*;
import com.blueberry.model.dto.MessageResponse;
import com.blueberry.model.dto.StatusDTO;
import com.blueberry.model.request.StatusRequest;
import com.blueberry.service.*;
import com.blueberry.service.impl.FriendService;
import com.blueberry.util.ModelMapperUtil;
import com.blueberry.util.StringTrimmer;
import jakarta.persistence.RollbackException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/auth/api/status")
@CrossOrigin("*")
@AllArgsConstructor
public class StatusController {

    private StatusService statusService;
    private UserService userService;
    private AppUserService appUserService;
    private ModelMapperUtil modelMapperUtil;
    private LikeService likeService;
    private FriendService friendService;
    private FollowService followService;
    private NotificationService notificationService;
    private final Sort SORT_BY_TIME_DESC = Sort.by(Sort.Direction.DESC, "lastActivity");

    @GetMapping("/{id}")
    public ResponseEntity<StatusDTO> findStatusById(@PathVariable Long id) {
        Optional<Status> status = statusService.findByIdAndDeleted(id, false);
        if (status.isPresent()) {
            return new ResponseEntity<>(modelMapperUtil.map(status, StatusDTO.class), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<StatusDTO>> getAllStatusByBodyContaining(@RequestParam("query") String query) {
        AppUser appUser = appUserService.getCurrentAppUser();
        List<AppUser> friendList = friendService.getFriendList(appUser.getId());
        List<Status> statuses = (List<Status>) statusService.findStatusByBodyContaining(appUser, friendList, query);
        return new ResponseEntity<>(modelMapperUtil.mapList(statuses, StatusDTO.class), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody StatusRequest statusRequest) {
        AppUser appUser = appUserService.getCurrentAppUser();
        Status status = new Status();
        status.setAuthor(appUser);
        status.setCreatedAt(LocalDateTime.now());
        status.setLastActivity(LocalDateTime.now());
        status.setBody(StringTrimmer.trim(statusRequest.getBody()));
        status.setImageList(statusRequest.getImageList());
        status.setCommentList(new ArrayList<>());
        status.setLikeList(new ArrayList<>());

        try {
            if (statusRequest.getPrivacyLevel() != null) {
                status.setPrivacyLevel(statusRequest.getPrivacyLevel());
            } else {
                status.setPrivacyLevel(PrivacyLevel.PUBLIC);
            }
            status = statusService.save(status);
            Follow follow = new Follow();
            follow.setStatus(status);
            follow.setFollowers(new ArrayList<>());
            followService.save(follow);
            return new ResponseEntity<>(modelMapperUtil.map(status, StatusDTO.class), HttpStatus.OK);
        } catch (RollbackException e) {
            return new ResponseEntity<>(new MessageResponse("Post failure status !!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable Long id, @RequestBody StatusRequest status) {
        Optional<Status> optionalStatus = statusService.findById(id);
        if (optionalStatus.isPresent()) {
            User user = userService.getCurrentUser();
            String currentUsername = user.getEmail();
            if (currentUsername.equals(optionalStatus.get().getAuthor().getUser().getEmail())) {
                optionalStatus.get().setBody(StringTrimmer.trim(status.getBody()));
                optionalStatus.get().setUpdatedAt(LocalDateTime.now());
                List<Image> newImageList = new ArrayList<>();
                if (status.getImageList() != null) {
                    newImageList.addAll(status.getImageList());
                }
                optionalStatus.get().setImageList(newImageList);
                Status savedStatus = statusService.save(optionalStatus.get());
                return new ResponseEntity<>(modelMapperUtil.map(savedStatus, StatusDTO.class), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponse("Access denied !!"), HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Not found !!"), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Status> status = statusService.findById(id);
        if (status.isPresent()) {
            User user = userService.getCurrentUser();
            String currentUsername = user.getEmail();
            if (currentUsername.equals(status.get().getAuthor().getUser().getEmail())) {
                status.get().setDeleted(true);
                statusService.save(status.get());
                return new ResponseEntity<>(id, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponse("Access denied !!"), HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Delete failed !!"), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/change-privacy")
    public ResponseEntity<?> updatePrivacy(@PathVariable Long id, @RequestBody StatusRequest statusRequest) {
        Optional<Status> optionalStatus = statusService.findById(id);
        if (optionalStatus.isPresent()) {
            AppUser currentUser = appUserService.getCurrentAppUser();
            if (Objects.equals(currentUser.getId(), optionalStatus.get().getAuthor().getId())) {
                optionalStatus.get().setPrivacyLevel(statusRequest.getPrivacyLevel());
                statusService.save(optionalStatus.get());
                return new ResponseEntity<>(new MessageResponse("Change Successfully !!"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new MessageResponse("Access denied !!"), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new MessageResponse("Not Found !!"), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<StatusDTO>> getStatusByUserId(@PathVariable Long id) {
        AppUser currentUser = appUserService.getCurrentAppUser();
        AppUser appUser = appUserService.findById(id).get();

        List<Status> statuses = (List<Status>) statusService.findAllByAuthor(appUser, currentUser);
        if (statuses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(modelMapperUtil.mapList(statuses, StatusDTO.class), HttpStatus.OK);
    }

    @GetMapping("/users/{id}/search")
    public ResponseEntity<List<StatusDTO>> searchStatusByUserId(@PathVariable Long id,@RequestParam String query) {
        AppUser currentUser = appUserService.getCurrentAppUser();
        AppUser appUser = appUserService.findById(id).get();

        List<Status> statuses = (List<Status>) statusService.findAllByAuthorAndBodyContaining(currentUser, appUser,query);
        if (statuses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(modelMapperUtil.mapList(statuses, StatusDTO.class), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<StatusDTO>> findAllStatus() {
        AppUser appUser = appUserService.getCurrentAppUser();
        List<AppUser> friendList = friendService.getFriendList(appUser.getId());
        List<Status> statuses = (List<Status>) statusService.findAllByPrivacy(appUser, friendList);
        return new ResponseEntity<>(modelMapperUtil.mapList(statuses, StatusDTO.class), HttpStatus.OK);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeStatus(@PathVariable Long id) {
        AppUser appUser = appUserService.getCurrentAppUser();
        Optional<Like> liked = likeService.findByStatusIdAndAuthorId(id, appUser.getId());
        try {
            if (liked.isPresent()) {
                likeService.deleteLike(liked.get());
                return new ResponseEntity<>(-1, HttpStatus.OK);
            } else {
                Like newLike = new Like(appUser.getId(), id);
                likeService.save(newLike);
                AppUser statusAuthor = statusService.findById(id).get().getAuthor();
                if (!statusAuthor.getId().equals(appUser.getId())) {
                    notificationService.saveNotification(appUser, statusAuthor,
                            NotificationType.LIKE_ON_POST, statusService.findById(id).get());
                }
                return new ResponseEntity<>(1, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
