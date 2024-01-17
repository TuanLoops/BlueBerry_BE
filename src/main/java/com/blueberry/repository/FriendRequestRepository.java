package com.blueberry.repository;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.FriendRequest;
import com.blueberry.model.app.FriendRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiverAndStatus(AppUser receiver, FriendRequestStatus status);
    Optional<FriendRequest> findTopBySenderAndReceiverOrderByCreateAtDesc(AppUser sender, AppUser receiver);
}