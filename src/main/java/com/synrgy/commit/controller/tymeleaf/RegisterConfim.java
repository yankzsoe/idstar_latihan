package com.synrgy.commit.controller.tymeleaf;


import com.synrgy.commit.config.Config;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.repository.oauth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Date;

@Controller
@RequestMapping("/register/web/")
public class RegisterConfim {

    @Autowired
    public UserRepository userRepo;

    Config config = new Config();

    @GetMapping(value = { "/index/{tokenotp}"})
    public String index(Model model,@PathVariable String  tokenotp) {
        User user = userRepo.findOneByOTP(tokenotp);
        if (null == user) {
            System.out.println("user null: tidak ditemukan");
            model.addAttribute("erordesc", "User not found for code "+tokenotp);
            model.addAttribute("title", "");
            return "register";
        }
       String today = config.convertDateToString(new Date());

        String dateToken = config.convertDateToString(user.getOtpExpiredDate());
        if(Long.parseLong(today) > Long.parseLong(dateToken)){
            model.addAttribute("erordesc", "Your token is expired. Please Get token again.");
            model.addAttribute("title", "");
            return "register";
        }
        user.setEnabled(true);
        userRepo.save(user);
        model.addAttribute("title", "Congratulations, "+user.getUsername()+", you have successfully registered.");
        model.addAttribute("erordesc", "");
        return "register";
    }

    @GetMapping(value = "/user1")
    public String index() {
        return "user";
    }

    @RequestMapping(value = "/user")
    public Principal user(Principal principal) {

      return  principal;
    }
}
