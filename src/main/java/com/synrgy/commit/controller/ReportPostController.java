//package com.synrgy.commit.controller;
//
//import com.synrgy.commit.model.ReportComment;
//import com.synrgy.commit.model.ReportPost;
//import com.synrgy.commit.model.ReportUser;
//import com.synrgy.commit.service.ReportService;
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
//@RequestMapping("/report")
//public class ReportPostController {
//    @Autowired
//    ReportService reportService;
//
//    @PostMapping("/insert/{idPost}")
//    public ResponseEntity<Map> reportPost(@PathVariable(value = "idPost") Long idPost,
//                                        Principal principal,
//                                        ReportPost reportPost) {
//        Map map = reportService.report(idPost, principal, reportPost);
//        return new ResponseEntity<Map>(map, HttpStatus.OK);
//    }
//
//    @PostMapping("/comment/{idKomentar}")
//    public ResponseEntity<Map> reportcomment(@PathVariable(value = "idKomentar") Long idKomentar,
//                                        Principal principal,
//                                        ReportComment reportComment) {
//        Map map = reportService.reportComment(idKomentar, principal, reportComment);
//        return new ResponseEntity<Map>(map, HttpStatus.OK);
//    }
//
//    @PostMapping("/user/{id_user}")
//    public ResponseEntity<Map> reportUser(@PathVariable(value = "id_user") Long id_user,
//                                             Principal principal,
//                                             ReportUser reportUser) {
//        Map map = reportService.reportUser(id_user, principal, reportUser);
//        return new ResponseEntity<Map>(map, HttpStatus.OK);
//    }
//}
