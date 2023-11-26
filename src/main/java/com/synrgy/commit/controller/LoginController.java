package com.synrgy.commit.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.synrgy.commit.config.Config;
import com.synrgy.commit.dao.request.LoginModel;
import com.synrgy.commit.dao.request.Register;
import com.synrgy.commit.dao.request.RegisterModel;
import com.synrgy.commit.dao.request.UserUpdateModel;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.service.email.EmailSender;
import com.synrgy.commit.service.UserService;
import com.synrgy.commit.util.EmailTemplate;
import com.synrgy.commit.util.Response;
import com.synrgy.commit.util.SimpleStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

@RestController
@RequestMapping("/login-user")
//@Transactional
public class LoginController {
    @Autowired
    private UserRepository userRepository;

    Config config = new Config();

    @Autowired
    public UserService serviceReq;

    @Value("${expired.token.password.minute:}")//FILE_SHOW_RUL
    private int expiredToken;

    @Autowired
    public Response response;

    @Value("${BASEURL:}")//FILE_SHOW_RUL
    private String BASEURL;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Value("${AUTHURL:}")//FILE_SHOW_RUL
    private String AUTHURL;

    @Autowired
    public RegisterController registerController;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    public EmailTemplate emailTemplate;

    @Autowired
    public EmailSender emailSender;

    @Value("${APPNAME:}")//FILE_SHOW_RUL
    private String APPNAME;


    @PostMapping("")
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map> login(@Valid @RequestBody LoginModel objModel) {
        Map map = serviceReq.login(objModel);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @PostMapping("/signin_google")
    @ResponseBody
    public ResponseEntity<Map> repairGoogleSigninAction(@RequestParam MultiValueMap<String, String> parameters) throws IOException {

        Map<String, Object> map123 = new HashMap<>();
        Map<String, String> map = parameters.toSingleValueMap();
        String accessToken = map.get("accessToken");

        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        System.out.println("access_token user=" + accessToken);
        Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credential).setApplicationName(
                "Oauth2").build();
        Userinfoplus profile= null;
        try {
            profile = oauth2.userinfo().get().execute();
        }catch (GoogleJsonResponseException e)
        {
            return new ResponseEntity<Map>(response.Error(e.getDetails()), HttpStatus.BAD_GATEWAY);
        }
        profile.toPrettyString();
        User user = userRepository.findOneByUsername(profile.getEmail());
        if (null != user) {
            if(!user.isEnabled()){
                UserUpdateModel obk = new UserUpdateModel();
                obk.setEmail(user.getUsername());
                sendEmailegister(obk);
                map123.put(config.getCode(), "401");
                map123.put(config.getMessage(), "Your Account is disable. Please chek your email for activation.");
                map123.put("type", "register");
                System.out.println("masuk 2");
                return new ResponseEntity<Map>(map123, HttpStatus.OK);
            }
            for (Map.Entry<String, String> req : map.entrySet()) {
                logger.info(req.getKey());
                logger.info(req.getValue());
            }

            Register register = new Register();
            register.setEmail(profile.getEmail());
            register.setPassword("Password123");
            register.setName(profile.getName());


            String oldPassword = user.getPassword();
            System.out.println("password lama :"+user.getPassword());
//            Boolean isPasswordMatches = true;
            if (!passwordEncoder.matches(register.getPassword(), oldPassword)) {
//                userRepository.updatePassword(user.getId(), passwordEncoder.encode(register.getPassword()));
                System.out.println("update password berhasil");
                user.setPassword(passwordEncoder.encode(register.getPassword()));
                userRepository.save(user);
//                isPasswordMatches = false;
            }
            String url = AUTHURL + "?username=" + register.getEmail() +
                    "&password=" + register.getPassword() +
//                    "&password=" + "password" +
                    "&grant_type=password" +
                    "&client_id=my-client-web" +
                    "&client_secret=password";
            ResponseEntity<Map> response123 = restTemplateBuilder.build().exchange(url, HttpMethod.POST, null, new
                    ParameterizedTypeReference<Map>() {
                    });

            if (response123.getStatusCode() == HttpStatus.OK) {
                userRepository.save(user);

                map123.put("access_token", response123.getBody().get("access_token"));
                map123.put("token_type", response123.getBody().get("token_type"));
                map123.put("refresh_token", response123.getBody().get("refresh_token"));
                map123.put("expires_in", response123.getBody().get("expires_in"));
                map123.put("scope", response123.getBody().get("scope"));
                map123.put("jti", response123.getBody().get("jti"));
                map123.put(config.getCode(), config.code_sukses);
                map123.put("message", config.message_sukses);
                map123.put("type", "login");
                Map data = new HashMap();
                data.put("data",map123);
                data.put("type", "login");
                System.out.println("masuk 3");
                //update old password : wajib
                user.setPassword(oldPassword);
                userRepository.save(user);
                return new ResponseEntity<Map>(data, HttpStatus.OK);

            }
        } else {
//            register
            RegisterModel registerModel = new RegisterModel();
            registerModel.setEmail(profile.getEmail());
            registerModel.setName(profile.getName());
            registerModel.setPassword("Password123");
            registerModel.setPhone_number("082284703333");
            registerModel.setGender(profile.getGender() == null ? "-" : profile.getGender());
            registerModel.setDomicile(profile.getLocale()== null ? "-" : profile.getLocale() );
            registerModel.setDomicile("-");
            registerModel.setInterest("-");
            ResponseEntity<Map> mapRegister = registerController.saveRegisterManual(registerModel);
//            map123.put(config.getCode(), mapRegister.getBody().get("status"));
//            map123.put(config.getMessage(), mapRegister.getBody().get("message"));
//            map123.put("type", "register");
//            map123.put("data", mapRegister.getBody().get("data"));
            Map map2 = (Map) mapRegister.getBody().get("data");
            map2.put("type", "register");
//            System.out.println("masuk 2 register manual");
//            map123.put("access_token", map2 .get("access_token"));
//            map123.put("token_type", map2 .get("token_type"));
//            map123.put("refresh_token", map2 .get("refresh_token"));
//            map123.put("expires_in", map2.get("expires_in"));
//            map123.put("scope", map2.get("scope"));
//            map123.put("jti", map2.get("jti"));
//            map123.put(config.getCode(), config.code_sukses);
//            map123.put("message", config.message_sukses);
//            map123.put("type", "login");
            // noted : register by email ga perlu update password
            return new ResponseEntity<Map>(map2, HttpStatus.OK);
        }
        System.out.println("masuk 1 luar ");
        return new ResponseEntity<Map>(map123, HttpStatus.OK);
    }

    // Step 2: sendp OTP berupa URL: guna updeta enable agar bisa login:
    @PostMapping("send-otp")//send OTP
    public Map sendEmailegister(@RequestBody UserUpdateModel user) {
        String message = "Thanks, please check your email for activation.";

        if (user.getEmail() == null) return response.isRequired("No email provided");
        User found = userRepository.findOneByUsername(user.getEmail());
        if (found == null) return response.notFound("Email not found"); //throw new BadRequest("Email not found");

        String template = emailTemplate.getTalentAcc();
        if (StringUtils.isEmpty(found.getOtp())) {
            User search;
            String otp;
            do {
                otp = SimpleStringUtils.randomString(6, true);
                search = userRepository.findOneByOTP(otp);
            } while (search != null);
            Date dateNow = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateNow);
            calendar.add(Calendar.MINUTE, expiredToken);
            Date expirationDate = calendar.getTime();

            found.setOtp(otp);
            found.setOtpExpiredDate(expirationDate);
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername()));
            template = template.replaceAll("\\{\\{VERIF_LINK}}", BASEURL + "register/web/index/" + otp);
            userRepository.save(found);
        } else {
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername()));
            template = template.replaceAll("\\{\\{VERIF_LINK}}", BASEURL + "register/web/index/" + found.getOtp());
        }
        emailSender.sendAsync(found.getUsername(), APPNAME + "- Register", template);
        return response.Sukses(message);
    }

}

