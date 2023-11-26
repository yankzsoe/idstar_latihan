package com.synrgy.commit.dao.request;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
@Data
public class Register {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

//    @NotBlank(message = "Phone is required")
//    private String phone;

//    private String facebook;

//    private String google;


}
