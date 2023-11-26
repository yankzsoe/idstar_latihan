package com.synrgy.commit.service;

import java.security.Principal;
import java.util.Map;

public interface BookmarkService {
    public Map bookmark(Long idPost, Principal principal);
    public Map unbookmark(Long idPost, Principal principal);
}
