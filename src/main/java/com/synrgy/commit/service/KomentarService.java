package com.synrgy.commit.service;

import com.synrgy.commit.model.Komentar;

import java.security.Principal;
import java.util.Map;

public interface KomentarService {
    public Map insert(Komentar objModel, Long idPost, Principal principal);
    public Map delete(Long idKomentar, Principal principal);
}
