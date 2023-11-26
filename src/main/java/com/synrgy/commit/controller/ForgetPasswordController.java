package com.synrgy.commit.controller;

import com.synrgy.commit.config.Config;
import com.synrgy.commit.dao.request.ResetPasswordModel;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.service.email.EmailSender;
import com.synrgy.commit.service.UserService;
import com.synrgy.commit.util.EmailTemplate;
import com.synrgy.commit.util.Response;
import com.synrgy.commit.util.SimpleStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/forget-password/")
public class ForgetPasswordController {

    @Autowired
    private UserRepository userRepository;

    Config config = new Config();

    @Autowired
    public UserService serviceReq;

    @Value("${expired.token.password.minute:}")//FILE_SHOW_RUL
    private int expiredToken;

    @Autowired
    public Response response;

    @Autowired
    public EmailTemplate emailTemplate;

    @Autowired
    public EmailSender emailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Step 1 : Send OTP
    @PostMapping("/send")//send OTP
    public Map sendEmailPassword(@RequestBody ResetPasswordModel user) {
        String message = "Thanks, please check your email";

        if (user.getEmail() == "") {
            return response.Error("Fill the email");
        }

        if (StringUtils.isEmpty(user.getEmail())) return response.Error("No email provided");
        User found = userRepository.findOneByUsername(user.getEmail());
        if (found == null) return response.notFound("Email not found"); //throw new BadRequest("Email not found");

        String template = emailTemplate.getResetPassword();

            User search;
            String otp;

                otp = SimpleStringUtils.randomString(4, true);
                search = userRepository.findOneByOTP(otp);

            Date dateNow = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateNow);

            calendar.add(Calendar.MINUTE, expiredToken);
            Date expirationDate = calendar.getTime();

            found.setOtp(otp);
            found.setOtpExpiredDate(expirationDate);
            found.setIs_change(false);
            found.setLimitotp(0);
            found.setResendOtpDate(null);
            template = template.replaceAll("\\{\\{PASS_TOKEN}}", otp);
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getFullname() == null ? "" +
                    "@UserName"
                    :
                    "" + found.getFullname()));

            userRepository.save(found);

        emailSender.sendAsync(found.getUsername(), "Absensi - Forget Password", template);


        return response.Sukses("Succesfull send email!");
    }

    //Step 2 : CHek TOKEN OTP EMAIL
    @PostMapping("/validate")
    public Map cheKTOkenValid(@RequestBody ResetPasswordModel model) {
        if(model.getEmail() == null ) return response.notFound("email" + config.isRequired);
        if (model.getOtp() == null) return response.notFound("Token " + config.isRequired);

        if (model.getEmail() == "") {
            return response.Error("Fill the email");
        }

        if (model.getOtp() == "") {
            return response.Error("Fill the otp");
        }

        User user = userRepository.checkExistingEmail(model.getEmail());

        Date currentTime = Calendar.getInstance().getTime();

        if(user.getOtpExpiredDate().compareTo(currentTime) < 0) {
            return response.Error("otp is expired");
        }
        if (user == null) {
            return response.Error("Not Found");
        }
        if(!model.getOtp().equals(user.getOtp())) {
            user.setLimitotp(user.getLimitotp()+1);
            userRepository.save(user);
            return response.Error("Wrong code otp!");
        }
        user.setIs_change(true);
        User dosave = userRepository.save(user);
        return response.Sukses("Sukses");
    }

    // Step 3 : lakukan reset password baru
    @PostMapping("/change-password")
    public Map<String, String> resetPassword(@RequestBody ResetPasswordModel model) {
        if (model.getEmail() == null) return response.notFound("Email " + config.isRequired);
        if (model.getNewPassword() == null) return response.notFound("New Password " + config.isRequired);
        if (model.getConfirmNewPassword() == null) return response.notFound("New Confirm Password " + config.isRequired);
        if (model.getEmail() == "") {
            return response.Error("Fill the email");
        }
        if (model.getNewPassword() == "") {
            return response.Error("Fill the New Password");
        }
        if (model.getNewPassword() == "") {
            return response.Error("Fill the confirm new password");
        }

        User user = userRepository.checkExistingEmail(model.getEmail());
        String success;

        if(user.getIs_change() == false) {
            return response.Error("please verify otp first!");
        }
        if(model.getNewPassword().length() < 6 && model.getConfirmNewPassword().length() < 6) {
            return response.Error("Minimum password 6 character");
        }
        if(!model.getNewPassword().equals(model.getConfirmNewPassword())) {
           return response.Error("New password doesn't match with Confirm password!");
       }

        user.setPassword(passwordEncoder.encode(model.getNewPassword().replaceAll("\\s+", "")));
        user.setOtpExpiredDate(null);
        user.setOtp(null);
        user.setIs_change(false);
        user.setLimitotp(0);
        user.setResendOtpDate(null);

        try {
            userRepository.save(user);
            success = "success";
        } catch (Exception e) {
            return response.Error("Gagal simpan user");
        }
        return response.Sukses(success);
    }

    @PostMapping("/resend")
    public Map resendotp(@RequestBody ResetPasswordModel model) {
        if(model.getEmail() == null ) return response.notFound("email" + config.isRequired);
        if (!response.isValidEmail(model.getEmail())) {
            return response.Error("Email is invalid");
        }

        if (model.getEmail() == "") {
            return response.Error("Fill the email");
        }

        User found = userRepository.checkExistingEmail(model.getEmail());

        Date currentTime = Calendar.getInstance().getTime();

        if(found == null) {
            return response.Error("User not found!");
        }


        if(found.getResendOtpDate() != null) {
            if(found.getResendOtpDate().compareTo(currentTime) > 0 && found.getLimitotp() >= 3) {
                return response.Error("You wrong 3 times, Please wait 5 minutes to resend!");
            } else if(found.getResendOtpDate().compareTo(currentTime) > 0) {
                return response.Error("Please wait 1 minutes to resend!");
            }
        }



        if (StringUtils.isEmpty(model.getEmail())) return response.Error("No email provided");
        if (found == null) return response.notFound("Email not found"); //throw new BadRequest("Email not found");

        String template = emailTemplate.getResetPassword();
            User search;
            String otp;

                otp = SimpleStringUtils.randomString(4, true);
                search = userRepository.checkExistingEmail(model.getEmail());

            Date dateNow = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateNow);
            calendar.add(Calendar.MINUTE, expiredToken);
            Date expirationDate = calendar.getTime();

        int jedaresend;

        if (found.getLimitotp() >= 3 ) {
            jedaresend = 5;
        } else {
            jedaresend = 1;
        }

            Date lim = new Date();
            Calendar limit = Calendar.getInstance();
            limit.setTime(lim);


        limit.add(Calendar.MINUTE, jedaresend);
            Date jeda = limit.getTime();



            found.setOtp(otp);
            found.setOtpExpiredDate(expirationDate);
            found.setResendOtpDate(jeda);
            template = template.replaceAll("\\{\\{PASS_TOKEN}}", otp);
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getFullname() == null ? "" +
                    "@UserName"
                    :
                    "" + found.getFullname()));

            User dosave = userRepository.save(found);

        emailSender.sendAsync(found.getUsername(), "Absensi - Forget Password", template);


        return response.Sukses("Succesfull send email!");
    }

    // Step 1 : Send OTP
    @PostMapping("/send-langsung")//send OTP
    public Map sendEmailPasswordLangsung(@RequestBody ResetPasswordModel user) {
        String message = "Thanks";

        if (user.getEmail() == "") {
            return response.Error("Fill the email");
        }

        if (StringUtils.isEmpty(user.getEmail())) return response.Error("No email provided");
        User found = userRepository.findOneByUsername(user.getEmail());
        if (found == null) return response.notFound("Email not found"); //throw new BadRequest("Email not found");

        String template = emailTemplate.getResetPassword();

        User search;
        String otp;

        otp = SimpleStringUtils.randomString(4, true);
        search = userRepository.findOneByOTP(otp);

        Date dateNow = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateNow);

        calendar.add(Calendar.MINUTE, expiredToken);
        Date expirationDate = calendar.getTime();

        found.setOtp(otp);
        found.setOtpExpiredDate(expirationDate);
        found.setIs_change(false);
        found.setLimitotp(0);
        found.setResendOtpDate(null);


        userRepository.save(found);

        return response.Sukses("OTP anda:"+otp);
    }

    @PostMapping("/change-password-langsung")
    public Map<String, String> resetPasswordlangsung(@RequestBody ResetPasswordModel model) {
        if (model.getEmail() == null) return response.notFound("Email " + config.isRequired);
        if (model.getNewPassword() == null) return response.notFound("New Password " + config.isRequired);
        if (model.getConfirmNewPassword() == null) return response.notFound("New Confirm Password " + config.isRequired);
        if (model.getOtp() == null) return response.notFound("OTP " + config.isRequired);
        if (model.getEmail() == "") {
            return response.Error("Fill the email");
        }
        if (model.getNewPassword() == "") {
            return response.Error("Fill the New Password");
        }
        if (model.getNewPassword() == "") {
            return response.Error("Fill the confirm new password");
        }
        if (model.getOtp() == "") {
            return response.Error("OTP the confirm new password");
        }
        //chek token
//        cheKTOkenValid(model);

        User user = userRepository.checkExistingEmail(model.getEmail());
        String success;

//        if(user.getIs_change() == false) {
//            return response.Error("please verify otp first!");
//        }
        if(model.getNewPassword().length() < 6 && model.getConfirmNewPassword().length() < 6) {
            return response.Error("Minimum password 6 character");
        }
        if(!model.getNewPassword().equals(model.getConfirmNewPassword())) {
            return response.Error("New password doesn't match with Confirm password!");
        }

        user.setPassword(passwordEncoder.encode(model.getNewPassword().replaceAll("\\s+", "")));
        user.setOtpExpiredDate(null);
        user.setOtp(null);
        user.setIs_change(false);
        user.setLimitotp(0);
        user.setResendOtpDate(null);

        try {
            userRepository.save(user);
            success = "success";
        } catch (Exception e) {
            return response.Error("Gagal simpan user");
        }
        return response.Sukses(success);
    }

}
