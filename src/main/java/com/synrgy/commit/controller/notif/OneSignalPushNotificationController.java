package com.synrgy.commit.controller.notif;


import com.synrgy.commit.config.Config;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
import com.synrgy.commit.service.oauth.TemplateCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.Principal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by Weslei Dias.
 **/
@RestController
@RequestMapping("/v1/apps/push-notif")
public class OneSignalPushNotificationController {

    //    @Autowired
//    private OneSignalPushNotificationRepository repository;
    @Autowired
    public UserRepository repository;
//    @Autowired
//    public ProductRepository repoProduct;

//    @Autowired
//    public ProductUserRecentlyRepository repoProductUserRecently;

//    @Autowired
//    public ProductWhitelistRepo productWhitelistRepo;


    @Autowired
    public UserRepository repoUser;

//    @Autowired
//    public ProductLoveRepository repoProductLoveRepository;

//    @Autowired
//    public ProductService serviceProduct;

    @Autowired
    private Oauth2UserDetailsService userDetailsService;

    @PersistenceContext
    private EntityManager entityManager;

    TemplateCRUD templateCRUD = new TemplateCRUD();

    Config config = new Config();


//    @PostMapping("/sendMessageToAllUsers/{message}")
//    public void sendMessageToAllUsers(@PathVariable("message") String message) {
//        PushNotificationOptions.sendMessageToAllUsers(message);
//    }

    //    @PostMapping("/sendMessageToUser/{userId}/{message}")
//    public void sendMessageToUser(@PathVariable("userId") String userId,
//                                  @PathVariable("message") String message)
//    {
//        PushNotificationOptions.sendMessageToUser(message, userId);
//    }
    @PostMapping("/sendMessageToUser")
    public ResponseEntity<Map> sendMessageToAllUsers(
            @RequestBody ReqPush objModel,
            Principal principal
    ) throws RuntimeException, ParseException {
        User idUser = getUserIdToken(principal, userDetailsService);
        if (idUser == null) {
            return new ResponseEntity<Map>(templateCRUD.notFound("User id" + config.isRequired), HttpStatus.NOT_FOUND);
        }

        Map  map =  PushNotificationOptions.sendMessageToUserMap(objModel.getMessage(), objModel.getIdPushNotif(), true);

        return new ResponseEntity<Map>(templateCRUD.template1(map), HttpStatus.OK);// response
    }

    @PostMapping("/register-device")
    public ResponseEntity<Map> RegisterDevice( @RequestBody ReqPush objModel,
                                               Principal principal
    ) throws RuntimeException, ParseException {
        User idUser = getUserIdToken(principal, userDetailsService);
        if (idUser == null) {
            return new ResponseEntity<Map>(templateCRUD.notFound("User id" + config.isRequired), HttpStatus.NOT_FOUND);
        }
        Map map= new HashMap();
        if(idUser.getIdPushNotif() ==null){
            map =  PushNotificationOptions.registerDevice(objModel);
            //lakukan simpan ke user
            idUser.setIdPushNotif(map.get("data").toString());//belum selesai. besok lanjut
            User save = repoUser.save(idUser);
        }
        return new ResponseEntity<Map>(templateCRUD.template1(map), HttpStatus.OK);// response
    }

    public User getUserIdToken(Principal principal, Oauth2UserDetailsService userDetailsService) {
        UserDetails user = null;
        String username = principal.getName();
        if (!StringUtils.isEmpty(username)) {
            user = userDetailsService.loadUserByUsername(username);
        }

        if (null == user) {
            throw new UsernameNotFoundException("User not found");
        }
        User idUser = repoUser.findOneByUsername(user.getUsername());
        if (null == idUser) {
            throw new UsernameNotFoundException("User name not found");
        }
        return idUser;

    }
}