package com.blueberry.controller;

import com.blueberry.model.acc.User;
import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Image;
import com.blueberry.model.app.PrivacyLevel;
import com.blueberry.model.app.Status;
import com.blueberry.model.dto.StatusDTO;
import com.blueberry.model.request.StatusRequest;
import com.blueberry.service.AppUserService;
import com.blueberry.service.StatusService;
import com.blueberry.service.UserService;
import com.blueberry.util.ModelMapperUtil;
import jakarta.persistence.RollbackException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final Sort SORT_BY_TIME_DESC = Sort.by(Sort.Direction.DESC, "lastActivity");

    @GetMapping("/{id}")
    public ResponseEntity<StatusDTO> findStatusById(@PathVariable Long id) {
        Optional<Status> status = statusService.findById(id);
        if (status.isPresent()) {
            return new ResponseEntity<>(modelMapperUtil.map(status, StatusDTO.class), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/current-user")
    public ResponseEntity<List<StatusDTO>> getAllByCurrentUser() {
        AppUser appUser = appUserService.getCurrentAppUser();
        List<Status> statuses = (List<Status>) statusService.findAllByAuthorId(appUser.getId(), SORT_BY_TIME_DESC);
        return new ResponseEntity<>(modelMapperUtil.mapList(statuses, StatusDTO.class), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StatusDTO>> getAllStatusByBodyContaining(@RequestParam("query") String query) {
        User user = userService.getCurrentUser();
        AppUser appUser = appUserService.findByUserName(user.getEmail());
        List<Status> statuses = (List<Status>) statusService
                .findAllByAuthorIdAndIsDeletedAndBodyContaining(appUser.getId(), false, query);
        return new ResponseEntity<>(modelMapperUtil.mapList(statuses, StatusDTO.class), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<StatusDTO> create(@RequestBody StatusRequest statusRequest) {
        AppUser appUser = appUserService.getCurrentAppUser();
        Status status = new Status();
        status.setAuthor(appUser);
        status.setCreatedAt(LocalDateTime.now());
        status.setLastActivity(LocalDateTime.now());
        status.setBody(statusRequest.getBody());
        status.setBody(status.getBody().trim());
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
            return new ResponseEntity<>(modelMapperUtil.map(status, StatusDTO.class), HttpStatus.OK);
        } catch (RollbackException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusDTO> edit(@PathVariable Long id, @RequestBody Status status) {
        Optional<Status> optionalStatus = statusService.findById(id);
        if (optionalStatus.isPresent()) {
            User user = userService.getCurrentUser();
            String currentUsername = user.getEmail();
            if (currentUsername.equals(optionalStatus.get().getAuthor().getUser().getEmail())) {
                optionalStatus.get().setBody(status.getBody().trim());
                optionalStatus.get().setUpdatedAt(LocalDateTime.now());
                List<Image> newImageList = new ArrayList<>();
                if (status.getImageList() != null) {
                    newImageList.addAll(status.getImageList());
                }
                optionalStatus.get().setImageList(newImageList);
                Status savedStatus = statusService.save(optionalStatus.get());
                return new ResponseEntity<>(modelMapperUtil.map(savedStatus, StatusDTO.class), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Status> status = statusService.findById(id);
        if (status.isPresent()) {
            User user = userService.getCurrentUser();
            String currentUsername = user.getEmail();
            if (currentUsername.equals(status.get().getAuthor().getUser().getEmail())) {
                status.get().setDeleted(true);
                statusService.save(status.get());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StatusDTO> updatePrivacy(@PathVariable Long id, @RequestBody PrivacyLevel newPrivacy) {
        Optional<Status> optionalStatus = statusService.findById(id);
        if (optionalStatus.isPresent()) {
            AppUser currentUser = appUserService.getCurrentAppUser();
            if (Objects.equals(currentUser.getId(), optionalStatus.get().getAuthor().getId())) {
                optionalStatus.get().setPrivacyLevel(newPrivacy);
                statusService.save(optionalStatus.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<StatusDTO>> getStatusByUserId(@PathVariable Long id) {
        AppUser currentUser = appUserService.getCurrentAppUser();
        List<PrivacyLevel> privacyLevels = new ArrayList<>();
        if(Objects.equals(currentUser.getId(), id)) {
            privacyLevels.add(PrivacyLevel.PRIVATE);
        }
        privacyLevels.add(PrivacyLevel.PUBLIC);
        privacyLevels.add(PrivacyLevel.FRIENDS);
        List<Status> statuses = (List<Status>) statusService.findAllByAuthorIdAndPrivaty(id, privacyLevels, SORT_BY_TIME_DESC);
        if (statuses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(modelMapperUtil.mapList(statuses, StatusDTO.class), HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<List<StatusDTO>> findAllStatus(){
        AppUser appUser = appUserService.getCurrentAppUser();
        List<Long> friends = new ArrayList<>();
        friends.add(2L);
        List<Status> statuses = (List<Status>) statusService.findAllByPrivacy(appUser.getId(),friends);
        return new ResponseEntity<>(modelMapperUtil.mapList(statuses, StatusDTO.class), HttpStatus.OK);
    }

}
