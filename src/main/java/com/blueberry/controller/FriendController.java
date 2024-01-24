package com.blueberry.controller;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.FriendRequest;
import com.blueberry.model.app.FriendRequestStatus;
import com.blueberry.model.dto.AppUserDTO;
import com.blueberry.model.dto.FriendRequestDTO;
import com.blueberry.model.request.FriendRequestResponse;
import com.blueberry.service.AppUserService;
import com.blueberry.service.impl.FriendService;
import com.blueberry.util.ModelMapperUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/auth/api/friend")
@CrossOrigin("*")
@AllArgsConstructor
public class FriendController {

    private FriendService friendService;
    private AppUserService appUserService;
    private ModelMapperUtil modelMapper;

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<AppUserDTO>> getFriendList(@PathVariable Long userId) {
        List<AppUser> friends = friendService.getFriendList(userId);
        return new ResponseEntity<>(modelMapper.mapList(friends,AppUserDTO.class), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<AppUserDTO>> getCurrentUserFriendList() {
        AppUser currentUser = appUserService.getCurrentAppUser();
        List<AppUser> friendList = friendService.getCurrentUserFriendList(currentUser);
        return new ResponseEntity<>(modelMapper.mapList(friendList,
                AppUserDTO.class), HttpStatus.OK);
    }

    @GetMapping("/incoming-request")
    public ResponseEntity<List<FriendRequestDTO>> getIncomingFriendRequests() {
        AppUser currentUser = appUserService.getCurrentAppUser();
        List<FriendRequest> friendRequests = friendService.getIncomingFriendRequests(currentUser);
        return new ResponseEntity<>(modelMapper.mapList(friendRequests,
                FriendRequestDTO.class), HttpStatus.OK);
    }

    @GetMapping("/sent-request")
    public ResponseEntity<List<FriendRequestDTO>> getSentFriendRequests() {
        AppUser currentUser = appUserService.getCurrentAppUser();
        List<FriendRequest> friendRequests = friendService.getSentFriendRequests(currentUser);
        return new ResponseEntity<>(modelMapper.mapList(friendRequests,
                FriendRequestDTO.class), HttpStatus.OK);
    }

    @PostMapping("/friend-request/send")
    public ResponseEntity<?> sendFriendRequest(@RequestParam Long receiverId) {
        AppUser sender = appUserService.getCurrentAppUser();
        if (Objects.equals(sender.getId(), receiverId))
            return new ResponseEntity<>("Add friend with self not allowed", HttpStatus.BAD_REQUEST);
        Optional<AppUser> receiver = appUserService.findById(receiverId);
        if (receiver.isEmpty())
            return new ResponseEntity<>("Receiver not found", HttpStatus.BAD_REQUEST);
        if (friendService.checkFriend(sender, receiver.get()))
            return new ResponseEntity<>("Already friend", HttpStatus.CONFLICT);
        Optional<FriendRequest> request = friendService.findTopBySenderAndReceiverOrderByCreateAtDesc(sender,
                receiver.get());
        if (request.isEmpty() || !request.get().getStatus().equals(FriendRequestStatus.PENDING)) {
            FriendRequest friendRequest = friendService.sendFriendRequest(sender, receiver.get());
            return new ResponseEntity<>(modelMapper.map(friendRequest, FriendRequestDTO.class), HttpStatus.OK);
        } else return new ResponseEntity<>("Pending friend request already exist", HttpStatus.CONFLICT);
    }

    @PutMapping("/friend-request/respond")
    public ResponseEntity<?> friendRequestResponse(@RequestBody FriendRequestResponse response) {
        try {
            FriendRequest friendRequest = friendService.friendRequestResponse(response.getRequestId(),
                    response.getStatus());
            return new ResponseEntity<>(modelMapper.map(friendRequest, FriendRequestDTO.class), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/unfriend/{friendId}")
    public ResponseEntity<?> unfriend(@PathVariable Long friendId) {
        AppUser currentUser = appUserService.getCurrentAppUser();
        Optional<AppUser> friend = appUserService.findById(friendId);
        if (friend.isEmpty()) return new ResponseEntity<>("Friend not found", HttpStatus.NOT_FOUND);
        friendService.unfriend(currentUser, friend.get());
        return new ResponseEntity<>(friend.get(), HttpStatus.OK);
    }
}
