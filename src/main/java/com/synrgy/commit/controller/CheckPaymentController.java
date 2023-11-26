//package com.synrgy.commit.controller;
//
//import com.synrgy.commit.model.Post;
//import com.synrgy.commit.model.ReportComment;
//import com.synrgy.commit.model.SimplerPayment;
//import com.synrgy.commit.model.oauth.User;
//import com.synrgy.commit.repository.SimplerRepository;
//import com.synrgy.commit.repository.oauth.UserRepository;
//import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
//import com.synrgy.commit.util.Response;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.security.Principal;
//import java.util.*;
//
//@RestController
//@RequestMapping("/check/")
//public class CheckPaymentController {
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    Response response;
//    @Autowired
//    private Oauth2UserDetailsService userDetailsService;
//    @Autowired
//    SimplerRepository simplerRepository;
//
//
//    @GetMapping("payment")
//    public ResponseEntity<Map> checkPayment(Principal principal) {
//        try {
//            Map map = new HashMap();
//            String[] kosong = new String[] {};
//
//            User idUser = getUserIdToken(principal, userDetailsService);
//
//            List<SimplerPayment> payment = simplerRepository.getCheck(idUser);
//
//            for (SimplerPayment pay : payment ) {
//                if (pay.getStatus().equals("On Progress")){
//                    map.put("message", "On Progress");
//                    map.put("status", "200");
//                    map.put("data", kosong);
//                    return new ResponseEntity<Map>(map, HttpStatus.OK);
//                }
//
//                if (pay.getStatus().equals("Failed")){
//                    map.put("message", "Failed");
//                    map.put("status", "200");
//                    map.put("data", kosong);
//                    return new ResponseEntity<Map>(map, HttpStatus.OK);
//                }
//            }
//
//            if (payment.isEmpty()) {
//                map.put("message", "no transaction");
//                map.put("status", "200");
//                map.put("data", kosong);
//                return new ResponseEntity<Map>(map, HttpStatus.OK);
//            }
//
//                map.put("message", "success");
//                map.put("status", "200");
//                map.put("data", payment);
//                return new ResponseEntity<Map>(map, HttpStatus.OK);
//
//        } catch (Exception e) {
//            return new ResponseEntity<Map>(response.ControllerError("Error"+e), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    private User getUserIdToken(Principal principal, Oauth2UserDetailsService userDetailsService) {
//        UserDetails user = null;
//        String username = principal.getName();
//        if (!StringUtils.isEmpty(username)) {
//            user = userDetailsService.loadUserByUsername(username);
//        }
//
//        if (null == user) {
//            throw new UsernameNotFoundException("User not found");
//        }
//        User idUser = userRepository.findOneByUsername(user.getUsername());
//        if (null == idUser) {
//            throw new UsernameNotFoundException("User name not found");
//        }
//        return idUser;
//    }
//}
//
//
