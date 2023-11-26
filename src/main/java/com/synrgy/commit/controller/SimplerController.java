//package com.synrgy.commit.controller;
//
//import com.synrgy.commit.dao.request.ProfileModel;
//import com.synrgy.commit.model.SimplerPayment;
//import com.synrgy.commit.service.ProfileService;
//import com.synrgy.commit.service.SimplerService;
//import com.synrgy.commit.util.Response;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.security.Principal;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/simpler/")
//public class SimplerController {
//    @Autowired
//    Response response;
//    @Autowired
//    SimplerService simplerService;
//
//    @PostMapping(value = "payment", consumes = {"multipart/form-data", "application/json"})
//    public ResponseEntity<Map> update (SimplerPayment objModel,
//                                       Principal principal,
//                                       MultipartFile file)  {
//        try {
//            Map map = simplerService.payment(objModel,principal,file);
//            return new ResponseEntity<Map>(map, HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<Map>(response.ControllerError("Error"), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @GetMapping("history")
//    public ResponseEntity<Map> history(
//            Principal principal
//    ){
//        Map map = simplerService.history(principal);
//        return new ResponseEntity<Map>(map, HttpStatus.OK);
//    }
//}
