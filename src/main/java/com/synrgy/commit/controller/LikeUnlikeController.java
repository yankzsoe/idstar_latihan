//package com.synrgy.commit.controller;
//
//import com.synrgy.commit.service.LikeService;
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
//@RequestMapping("/post")
//public class LikeUnlikeController {
//    @Autowired
//    LikeService likeService;
//
//    @PostMapping("like/{idPost}")
//    public ResponseEntity<Map> like(@PathVariable(value = "idPost") Long idPost,
//                                      Principal principal) {
//        Map map = likeService.like(idPost, principal);
//        return new ResponseEntity<Map>(map, HttpStatus.OK);
//    }
//
//    @PostMapping("unlike/{idPost}")
//    public ResponseEntity<Map> unlike(@PathVariable(value = "idPost") Long idPost,
//                                      Principal principal) {
//        Map map = likeService.unlike(idPost, principal);
//        return new ResponseEntity<Map>(map, HttpStatus.OK);
//    }
//}
