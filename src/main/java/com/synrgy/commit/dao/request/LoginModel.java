package com.synrgy.commit.dao.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginModel {

    private String email;

    private String password;
}