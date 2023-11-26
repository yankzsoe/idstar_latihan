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
//@RequestMapping("/list")
//public class ListFollowController {
//
//    @Autowired
//    FollowService followService;
//
//    @GetMapping("/following/{userId}")
//    public ResponseEntity<Map> following(@PathVariable(value = "userId") Long userId, Principal principal) {
//        Map map = followService.listFollowing(userId, principal);
//        return new ResponseEntity<Map>(map, HttpStatus.OK);
//    }
//
//    @GetMapping("/followers/{userId}")
//    public ResponseEntity<Map> followers(@PathVariable(value = "userId") Long userId, Principal principal) {
//        Map map = followService.listFollowers(userId, principal);
//        return new ResponseEntity<Map>(map, HttpStatus.OK);
//    }
//}
