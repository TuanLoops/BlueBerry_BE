package com.blueberry.repository;

import com.blueberry.model.app.Follow;
import com.blueberry.model.app.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findByStatus(Status status);
}
