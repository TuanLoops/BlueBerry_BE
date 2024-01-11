package com.blueberry.controller;

import com.blueberry.model.acc.User;
import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Image;
import com.blueberry.model.app.Status;
import com.blueberry.service.AppUserService;
import com.blueberry.service.StatusService;
import com.blueberry.service.UserService;
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
import java.util.Optional;

@RestController
@RequestMapping("/auth/api/status")
@CrossOrigin("*")
@AllArgsConstructor
public class StatusController {

    private StatusService statusService;
    private UserService userService;
    private AppUserService appUserService;

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Status>> findStatusById(@PathVariable Long id) {
        Optional<Status> status = statusService.findById(id);
        if (status.isPresent()) {
            return new ResponseEntity<>(status, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<Status>> getAllByCurrentUser() {

        User user = userService.getCurrentUser();

        AppUser appUser = appUserService.findByUserName(user.getEmail());

        Iterable<Status> statuses = statusService.findAllByAuthorId(appUser.getId(), Sort.by(Sort.Direction.DESC, "lastActivity"));
        System.out.println(statuses);

        return new ResponseEntity<>(statuses, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Iterable<Status>> getAllStatusByBodyContaining(@RequestParam("query") String query) {
        User user = userService.getCurrentUser();
        AppUser appUser = appUserService.findByUserName(user.getEmail());

        Iterable<Status> statuses = statusService.findAllByAuthorIdAndIsDeletedAndBodyContaining(appUser.getId(),false, query);

        return new ResponseEntity<>(statuses, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Status> create(@RequestBody Status status) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        status.setAuthor(appUserService.findByUserName(authentication.getName()));

        status.setCreatedAt(LocalDateTime.now());

        status.setLastActivity(LocalDateTime.now());

        return new ResponseEntity<>(statusService.save(status), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Status> edit(@PathVariable Long id, @RequestBody Status status) {
        Optional<Status> optionalStatus = statusService.findById(id);

        if (optionalStatus.isPresent()) {
            User user = userService.getCurrentUser();

            String currentUsername = user.getEmail();

            if (currentUsername.equals(optionalStatus.get().getAuthor().getUser().getEmail())) {

                optionalStatus.get().setBody(status.getBody());

                optionalStatus.get().setUpdatedAt(LocalDateTime.now());

                List<Image> newImageList = new ArrayList<>();

                if (status.getImageList() != null) {

                    newImageList.addAll(status.getImageList());

                }
                optionalStatus.get().setImageList(newImageList);

                Status savedStatus = statusService.save(optionalStatus.get());

                return new ResponseEntity<>(savedStatus, HttpStatus.OK);
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
}
