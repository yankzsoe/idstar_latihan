package com.synrgy.commit.dao.request;

import com.synrgy.commit.controller.validationpass.anotation.PasswordValueMatch;
import com.synrgy.commit.controller.validationpass.anotation.ValidPassword;
import lombok.Data;
import org.aspectj.bridge.Message;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;


@Data
public class RegisterModel {
    public Long id;

    public String email;

    @ValidPassword
    @NotEmpty(message = "password is mandatory")
    public String password;

    public String name;

    public String gender;

    public String domicile;

    public String phone_number;

    public String interest;

    public Date tanggalLahir;

    public String tempatLahir;

    public String namaIbuKandung;
}