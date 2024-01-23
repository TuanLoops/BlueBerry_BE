package com.blueberry.service.impl;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Follow;
import com.blueberry.model.app.Status;
import com.blueberry.repository.FollowRepository;
import com.blueberry.service.FollowService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FollowServiceImpl implements FollowService {
    private FollowRepository followRepository;

    public Follow save(Follow follow) {
        return followRepository.save(follow);
    }

    public Follow findByStatus(Status status) {
        return followRepository.findByStatus(status);
    }

    public void followStatus(AppUser follower, Status status) {
        Follow follow = findByStatus(status);
        List<AppUser> followers = follow.getFollowers();
        if (!followers.contains(follower)) {
            followers.add(follower);
            followRepository.save(follow);
        }
    }

    public void unFollowStatus(AppUser follower, Status status) {
        Follow follow = findByStatus(status);
        List<AppUser> followers = follow.getFollowers();
        if (followers.contains(follower)) {
            followers.remove(follower);
            followRepository.save(follow);
        }
    }
}
