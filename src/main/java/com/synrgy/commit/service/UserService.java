package com.synrgy.commit.service;


import com.synrgy.commit.dao.request.LoginModel;
import com.synrgy.commit.dao.request.RegisterModel;
import com.synrgy.commit.model.oauth.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.Map;

public interface UserService {
//    public Map insert(User obj);
//    public Map update(User obj);

    Map registerManual(RegisterModel objModel);

    public Map login(LoginModel objLogin);

    public Map getDetailProfile(Principal principal);

    public Map sugestedPeople(Principal principal);

    public Map getDetail(Principal principal, Long idUserDetail);
    Map registerManualTampaEMail(RegisterModel objModel);



}
