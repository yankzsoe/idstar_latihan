package com.synrgy.commit.service.impl;

//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectRequest;
import com.synrgy.commit.dao.request.AccountsModel;
import com.synrgy.commit.dao.request.ProfileModel;
import com.synrgy.commit.model.FilePost;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.repository.oauth.UserRepository;
//import com.synrgy.commit.service.PostService;
import com.synrgy.commit.service.ProfileService;
import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
import com.synrgy.commit.util.Response;
//import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Service
public class ProfileImpl implements ProfileService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private Oauth2UserDetailsService userDetailsService;
    @Autowired
    Response response;
//    @Value("${document.bucket-name}")
//    private String bucketName;
//    @Value("${file.base.url.aws}")
//    private String fileBaseUrl;
//    @Autowired
//    private AmazonS3 amazonS3;


    @Override
    public Map updateProfile(ProfileModel objModel, Principal principal, MultipartFile file) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            User userr = userRepository.getbyID(idUser.getId());
//            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            if (idUser == null) {
                return response.notFound("User id notfound");
            }
            if (response.chekNull(objModel.getBio())) {
                return response.Error("Bio is required");
            }
            if (response.chekNull(objModel.getFullname())) {
                return response.Error("Fullname is required");
            }
            if (objModel.getFullname().isEmpty()){
                return response.Error("Fill the name!");
            }
            if(objModel.getFullname().length() > 100) {
                return response.Error("Name must be less than 100!");
            }
            if(!response.nameNotSimbol(objModel.getFullname())) {
                return response.Error("Name do not use number or symbol");
            }
//            if (extension.equals("jpeg") || extension.equals("jpg") || extension.equals("png")) {
//                if (file.getSize() > 5242880) {
//                    return response.Error("File too large, Max 5MB for image!");
//                }
//            }

//            String tempFileName = this.upload(file);

//            userr.setProfile_pic(fileBaseUrl+tempFileName);
            userr.setBio(objModel.getBio());
            userr.setFullname(objModel.getFullname());

            User save = userRepository.save(userr);

            return response.Sukses(save);
        } catch (Exception e) {
            return response.Error("eror:"+e);
        }
    }

    @Override
    public Map updateProfileOnly(ProfileModel objModel, Principal principal) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            User userr = userRepository.getbyID(idUser.getId());
            if (idUser == null) {
                return response.notFound("User id notfound");
            }
            if (response.chekNull(objModel.getBio())) {
                return response.Error("Bio is required");
            }
            if (response.chekNull(objModel.getFullname())) {
                return response.Error("Fullname is required");
            }
            if (objModel.getFullname().isEmpty()){
                return response.Error("Fill the name!");
            }
            if(objModel.getFullname().length() > 101) {
                return response.Error("Name must be less than 100!");
            }
            if (objModel.getBio().length() > 201 ) {
                return response.Error("Max 200 Character!");
            }
            if(objModel.getFullname().length() > 100) {
                return response.Error("Name must be less than 100!");
            }
            if(!response.nameNotSimbol(objModel.getFullname())) {
                return response.Error("Name do not use number or symbol");
            }

            userr.setBio(objModel.getBio());
            userr.setFullname(objModel.getFullname());

            User save = userRepository.save(userr);

            return response.Sukses(save);
        } catch (Exception e) {
            return response.Error("eror:"+e);
        }
    }

    @Override
    public Map updateAccounts(AccountsModel objModel, Principal principal) {
        try{
            User idUser = getUserIdToken(principal, userDetailsService);
            User user = userRepository.getbyID(idUser.getId());
            if (idUser == null) {
                return response.notFound("User id notfound");
            }
            if (objModel.getEmail() == "") {
                return response.Error("Fill the email");
            }
            if (objModel.getPhone_number() == "") {
                return response.Error("Fill the phone number");
            }
            if (objModel.getDomicile() == "") {
                return response.Error("Choose the domicile");
            }
            if (objModel.getGender() == "") {
                return response.Error("Choose the gender");
            }
            if (response.chekNull(objModel.getEmail())) {
                return response.Error("Email is required");
            }
            if (response.chekNull(objModel.getPhone_number())) {
                return response.Error("Phone number is required");
            }
            if (response.chekNull(objModel.getDomicile())) {
                return response.Error("Domicile is required");
            }
            if (response.chekNull(objModel.getGender())) {
                return response.Error("Gender is required");
            }
            if (!response.isValidEmail(objModel.getEmail())) {
                return response.Error("Email is invalid");
            }
            if(!response.isNumeric(objModel.getPhone_number())){
                return response.Error("Phone number is invalid");
            }
            if (!response.notNumber(objModel.getPhone_number())) {
                return response.Error("Invalid number phone");
            }
//            if (!objModel.getEmail().equals(user.getUsername())) {
//                User checkEmail = userRepository.checkExistingEmail(objModel.getEmail());
//                if (null != checkEmail) {
//                    return response.Error("Email is Registered, try another email");
//                }
//            }

            user.setUsername(objModel.getEmail());
            user.setPhone_number(objModel.getPhone_number());
            user.setRegion(objModel.getDomicile());
            user.setGender(objModel.getGender());
            User save = userRepository.save(user);

            return response.Sukses(save);
        } catch (Exception e) {
            return response.Error(e);
        }
    }

    private User getUserIdToken(Principal principal, Oauth2UserDetailsService userDetailsService) {
        UserDetails user = null;
        String username = principal.getName();
        if (!StringUtils.isEmpty(username)) {
            user = userDetailsService.loadUserByUsername(username);
        }

        if (null == user) {
            throw new UsernameNotFoundException("User not found");
        }
        User idUser = userRepository.findOneByUsername(user.getUsername());
        if (null == idUser) {
            throw new UsernameNotFoundException("User name not found");
        }
        return idUser;
    }

//    private String upload(MultipartFile file) throws IOException {
//        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
//
//
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentLength(file.getSize());
//        if (extension.equals("jpeg") || extension.equals("jpg")) {
//            metadata.setContentType("image/jpg");
//        } else if (extension.equals("png")) {
//            metadata.setContentType("image/png");
//        } else {
//            throw new FileUploadException("Can only upload jpeg, jpg and png file");
//        }
//
//        String nameFiles = file.getOriginalFilename().replaceAll(" ", "-").toLowerCase();
//
//        String tempFileName = "profpic/" + UUID.randomUUID() + "." + extension;
//
//        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, tempFileName, file.getInputStream(),
//                metadata);
//        amazonS3.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));
//
//        return tempFileName;
//    }
}
