package com.synrgy.commit.service;

import com.synrgy.commit.model.ReportComment;
import com.synrgy.commit.model.ReportPost;
import com.synrgy.commit.model.ReportUser;

import java.security.Principal;
import java.util.Map;

public interface ReportService {
    public Map report(Long idPost, Principal principal, ReportPost reportPost);
    public Map reportComment(Long idKomentar, Principal principal, ReportComment reportComment);
    public Map reportUser(Long id_user, Principal principal, ReportUser reportUser);
}
