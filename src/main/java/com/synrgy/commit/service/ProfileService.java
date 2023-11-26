package com.synrgy.commit.service;

import com.synrgy.commit.dao.request.AccountsModel;
import com.synrgy.commit.dao.request.ProfileModel;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Map;

public interface ProfileService {
    public Map updateProfile(ProfileModel objModel, Principal principal, MultipartFile file);
    public Map updateProfileOnly(ProfileModel objModel, Principal principal);
    public Map updateAccounts(AccountsModel objModel, Principal principal);
}
