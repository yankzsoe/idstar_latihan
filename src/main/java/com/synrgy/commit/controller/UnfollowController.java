//package com.synrgy.commit.controller;
//
//import com.synrgy.commit.service.FollowService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.security.Principal;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/unfollow")
//public class UnfollowController {
//
//    @Autowired
//    FollowService followService;
//
//    @PostMapping("/{followIdUser}")
//    public ResponseEntity<Map> delete(@PathVariable(value = "followIdUser") Long followIdUser,
//                                      Principal principal) {
//        Map map = followService.unfollow(followIdUser, principal);
//        return new ResponseEntity<Map>(map, HttpStatus.OK);
//    }
//}
//
