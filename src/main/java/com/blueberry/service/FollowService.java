package com.blueberry.service;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Follow;
import com.blueberry.model.app.Status;

import java.util.List;

public interface FollowService {
    Follow save(Follow follow);
    Follow findByStatus(Status status);
    void followStatus(AppUser follower, Status status);
    void unFollowStatus(AppUser follower, Status status);
}
