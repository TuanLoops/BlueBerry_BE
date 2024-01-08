package com.blueberry.repository;

import com.blueberry.model.acc.User;
import com.blueberry.model.app.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUser(User user);
}
