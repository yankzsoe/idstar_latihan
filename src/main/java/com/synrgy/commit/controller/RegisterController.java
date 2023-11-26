package com.synrgy.commit.controller;

import com.synrgy.commit.config.Config;
import com.synrgy.commit.dao.request.RegisterModel;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.service.UserService;
import com.synrgy.commit.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private UserRepository userRepository;

    Config config = new Config();

    @Autowired
    public UserService serviceReq;

    @Value("${BASEURL:}")//FILE_SHOW_RUL
    private String BASEURL;

    @Autowired
    public Response response;
    @PostMapping("")
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map> saveRegisterManual(@Valid @RequestBody RegisterModel objModel) throws RuntimeException {
        Map map = new HashMap();

        User user = userRepository.checkExistingEmail(objModel.getEmail());
        if (null != user) {
            return new ResponseEntity<Map>(response.Error("Email is Registered, try another email or click Forget Password"), HttpStatus.OK);
        }
        map = serviceReq.registerManual(objModel);


        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @PostMapping("/langsung")
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map> saveRegisterManualTampaGOOGLE(@Valid @RequestBody RegisterModel objModel) throws RuntimeException {
        Map map = new HashMap();

        User user = userRepository.checkExistingEmail(objModel.getEmail());
        if (null != user) {
            return new ResponseEntity<Map>(response.Error("Email is Registered, try another email or click Forget Password"), HttpStatus.OK);
        }
        map = serviceReq.registerManualTampaEMail(objModel);


        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }


}
