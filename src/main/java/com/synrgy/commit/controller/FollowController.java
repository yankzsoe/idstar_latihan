//package com.synrgy.commit.controller;
//
//import com.synrgy.commit.service.FollowService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/follow")
//public class FollowController {
//
//    @Autowired
//    FollowService followService;
//
//    @PostMapping("/{followIdUser}")
//    public ResponseEntity<Map> follow(@PathVariable(value = "followIdUser") Long followIdUser,
//                                      Principal principal) {
//        Map map = followService.follow(followIdUser, principal);
//        return new ResponseEntity<Map>(map, HttpStatus.OK);
//    }
//}
