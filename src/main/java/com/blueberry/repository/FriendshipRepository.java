package com.blueberry.repository;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByUserOrFriend(AppUser user, AppUser friend);

    Optional<Friendship> findByUserAndFriend(AppUser user, AppUser friend);

    List<Friendship> findByUser(AppUser user);
}