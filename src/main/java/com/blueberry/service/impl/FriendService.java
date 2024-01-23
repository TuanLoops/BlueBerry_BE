package com.blueberry.service.impl;

import com.blueberry.model.app.*;
import com.blueberry.repository.AppUserRepository;
import com.blueberry.repository.FriendRequestRepository;
import com.blueberry.repository.FriendshipRepository;
import com.blueberry.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FriendService {

    private FriendshipRepository friendshipRepository;

    private FriendRequestRepository friendRequestRepository;

    private AppUserRepository appUserRepository;

    private NotificationService notificationService;

    public List<AppUser> getFriendList(Long userId) {
        AppUser user = appUserRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not " +
                "found"));

        List<Friendship> friendships = friendshipRepository.findByUserOrFriend(user, user);

        return friendships.stream()
                .map(friendship -> friendship.getUser().equals(user) ? friendship.getFriend() : friendship.getUser())
                .collect(Collectors.toList());
    }

    public List<AppUser> getCurrentUserFriendList(AppUser user) {
        List<Friendship> friendships = friendshipRepository.findByUserOrFriend(user, user);

        user.setLastOnline(LocalDateTime.now());
        appUserRepository.save(user);

        return friendships.stream()
                .map(friendship -> friendship.getUser().equals(user) ? friendship.getFriend() : friendship.getUser())
                .collect(Collectors.toList());
    }

    public List<FriendRequest> getIncomingFriendRequests(AppUser user) {
        return friendRequestRepository.findByReceiverAndStatus(user, FriendRequestStatus.PENDING);
    }

    public List<FriendRequest> getSentFriendRequests(AppUser user) {
        return friendRequestRepository.findBySenderAndStatus(user, FriendRequestStatus.PENDING);
    }

    public FriendRequest sendFriendRequest(AppUser sender, AppUser receiver) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setCreateAt(LocalDateTime.now());
        friendRequest.setStatus(FriendRequestStatus.PENDING);
        notificationService.saveNotification(sender, receiver, NotificationType.FRIEND_REQUEST_INCOMING);
        return friendRequestRepository.save(friendRequest);
    }

    public Optional<FriendRequest> findTopBySenderAndReceiverOrderByCreateAtDesc(AppUser sender, AppUser receiver) {
        return friendRequestRepository.findTopBySenderAndReceiverOrderByCreateAtDesc(sender, receiver);
    }

    public FriendRequest friendRequestResponse(Long requestId, FriendRequestStatus status) {
        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Friend request not found"));

        if (!friendRequest.getStatus().equals(FriendRequestStatus.PENDING)) {
            throw new IllegalStateException("Friend request is no longer pending.");
        }

        switch (status) {
            case ACCEPTED -> {
                establishFriendship(friendRequest.getSender(), friendRequest.getReceiver());
                notificationService.saveNotification(friendRequest.getReceiver(), friendRequest.getSender(),
                        NotificationType.FRIEND_REQUEST_ACCEPT);
            }
            case DECLINED -> {
                notificationService.saveNotification(friendRequest.getReceiver(), friendRequest.getSender(),
                        NotificationType.FRIEND_REQUEST_DECLINE);
            }
        }

        friendRequest.setStatus(status);
        return friendRequestRepository.save(friendRequest);
    }

    private void establishFriendship(AppUser sender, AppUser receiver) {
        Friendship friendship = new Friendship();
        friendship.setUser(sender);
        friendship.setFriend(receiver);
        friendshipRepository.save(friendship);
    }

    public void unfriend(AppUser user, AppUser friend) {
        Optional<Friendship> friendship1 = friendshipRepository.findByUserAndFriend(user, friend);
        Optional<Friendship> friendship2 = friendshipRepository.findByUserAndFriend(friend, user);
        friendship1.ifPresent(friendship -> friendshipRepository.delete(friendship));
        friendship2.ifPresent(friendship -> friendshipRepository.delete(friendship));
    }

    public boolean checkFriend(AppUser user1, AppUser user2) {
        if (friendshipRepository.findByUserAndFriend(user1, user2).isPresent()) return true;
        return friendshipRepository.findByUserAndFriend(user2, user1).isPresent();
    }
}

