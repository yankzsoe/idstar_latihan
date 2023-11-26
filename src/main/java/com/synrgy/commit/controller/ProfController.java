//package com.synrgy.commit.controller;
//
//import com.synrgy.commit.dao.request.AccountsModel;
//import com.synrgy.commit.dao.request.ProfileModel;
//import com.synrgy.commit.service.ProfileService;
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
//@RequestMapping("/profile/")
//public class ProfController {
//
//    @Autowired
//    Response response;
//    @Autowired
//    ProfileService profileService;
//
//    @PutMapping(value = "update", consumes = {"multipart/form-data", "application/json"})
//    public ResponseEntity<Map> update (ProfileModel objModel,
//                                       Principal principal,
//                                       @RequestParam(value = "file", required = false) MultipartFile file)  {
//        try {
//            if (file != null) {
//                Map map = profileService.updateProfile(objModel,principal,file);
//                return new ResponseEntity<Map>(map, HttpStatus.OK);
//            } else {
//                Map map = profileService.updateProfileOnly(objModel,principal);
//                return new ResponseEntity<Map>(map, HttpStatus.OK);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<Map>(response.ControllerError("Error"), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @PutMapping(value = "account")
//    public ResponseEntity<Map> accounts (@RequestBody AccountsModel objModel,
//                                         Principal principal)  {
//        try {
//            Map map = profileService.updateAccounts(objModel,principal);
//            return new ResponseEntity<Map>(map, HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<Map>(response.ControllerError("Error"), HttpStatus.BAD_REQUEST);
//        }
//    }
//}
