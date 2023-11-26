package com.synrgy.commit.service;

import java.security.Principal;
import java.util.Map;

public interface LikeService {
    public Map like(Long idPost, Principal principal);
    public Map unlike(Long idPost, Principal principal);
}
