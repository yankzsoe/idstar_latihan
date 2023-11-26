//package com.synrgy.commit.controller;
//
//import com.synrgy.commit.model.Komentar;
//import com.synrgy.commit.service.KomentarService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/komentar")
//public class KomentarController {
//    @Autowired
//    KomentarService komentarService;
//
//    @PostMapping("/insert/{idPost}")
//    public ResponseEntity<Map> like(@PathVariable(value = "idPost") Long idPost,
//                                    Komentar objModel,
//                                    Principal principal) {
//        Map map = komentarService.insert(objModel,idPost, principal);
//        return new ResponseEntity<Map>(map, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/delete/{idKomentar}")
//    public ResponseEntity<Map> like(@PathVariable(value = "idKomentar") Long idKomentar,
//                                    Principal principal) {
//        Map map = komentarService.delete(idKomentar, principal);
//        return new ResponseEntity<Map>(map, HttpStatus.OK);
//    }
//}
