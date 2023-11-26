package com.synrgy.commit.service;

import com.synrgy.commit.model.Follow;

import java.security.Principal;
import java.util.Map;

public interface FollowService {
    public Map follow(Long followIdUser, Principal principal);
    public Map unfollow(Long followIdUser, Principal principal);
    public Map listFollowing(Long userId, Principal principal);
    public Map listFollowers(Long userId, Principal principal);
}
