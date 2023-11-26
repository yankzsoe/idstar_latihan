package com.synrgy.commit.dao.request;

import lombok.Data;

import java.util.List;

@Data
public class UserUpdateModel {

    public Long id;
    public String email;
    public String otp;
    public String username;
    public String newPassword;
    public Boolean enabled;

}
