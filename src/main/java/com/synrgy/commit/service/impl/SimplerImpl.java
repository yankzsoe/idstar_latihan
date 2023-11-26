//package com.synrgy.commit.service.impl;
//
////import com.amazonaws.services.s3.AmazonS3;
////import com.amazonaws.services.s3.model.CannedAccessControlList;
////import com.amazonaws.services.s3.model.ObjectMetadata;
////import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.synrgy.commit.model.SimplerPayment;
//import com.synrgy.commit.model.oauth.User;
//import com.synrgy.commit.repository.SimplerRepository;
//import com.synrgy.commit.repository.oauth.UserRepository;
//import com.synrgy.commit.service.SimplerService;
//import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
//import com.synrgy.commit.util.Response;
//import io.swagger.v3.oas.annotations.servers.Server;
//import org.apache.commons.io.FilenameUtils;
//import org.apache.tomcat.util.http.fileupload.FileUploadException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.security.Principal;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//@Service
//public class SimplerImpl implements SimplerService {
//    @Autowired
//    private Oauth2UserDetailsService userDetailsService;
//    @Autowired
//    Response response;
//    @Autowired
//    UserRepository userRepository;
//    @Value("${document.bucket-name}")
//    private String bucketName;
//    @Value("${file.base.url.aws}")
//    private String fileBaseUrl;
//    @Autowired
//    private AmazonS3 amazonS3;
//    @Autowired
//    SimplerRepository simplerRepository;
//
//    @Override
//    public Map payment(SimplerPayment objModel, Principal principal, MultipartFile file) {
//
//        try {
//            User idUser = getUserIdToken(principal, userDetailsService);
//            User userr = userRepository.getbyID(idUser.getId());
//            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
//            if (userr == null) {
//                return response.notFound("User notfound");
//            }
//            if (objModel.getPlan() == null) {
//                return response.notFound("Choice the plan!");
//            }
//            if (response.chekNull(objModel.getPlan())) {
//                return response.Error("Plan is required!");
//            }
//            if (extension.equals("jpeg") || extension.equals("jpg") || extension.equals("png")) {
//                if (file.getSize() > 5242880) {
//                    return response.Error("File too large, Max 5MB for image!");
//                }
//            }
//
//            SimplerPayment getPayment = simplerRepository.getUser(idUser, "On Progress");
//
//            if (getPayment != null){
//                return response.Error("Your transaction ID " + getPayment.getTransaction_id() + " that is still on progress");
//            }
//
//            String total_paid = null;
//            String method_payment = "Manual Bank Transfer";
//            String tempFileName = this.upload(file);
//
//            if (objModel.getPlan() == 1) {
//                total_paid = "10.000";
//            }
//            if (objModel.getPlan() == 3) {
//                total_paid = "24.000";
//            }
//            if (objModel.getPlan() == 6) {
//                total_paid = "30.000";
//            }
//
//            SimplerPayment simplerPayment = new SimplerPayment();
//            simplerPayment.setPlan(objModel.getPlan());
//            simplerPayment.setPayment_method(method_payment);
//            simplerPayment.setStatus("On Progress");
//            simplerPayment.setImage_payment(fileBaseUrl+tempFileName);
//            simplerPayment.setId_user(idUser);
//            simplerPayment.setTotal_paid("IDR "+total_paid);
//            simplerPayment.setTransaction_id(objModel.getTransaction_id());
//
//            SimplerPayment save = simplerRepository.save(simplerPayment);
//
//            return response.Sukses(save);
//        } catch (Exception e) {
//            return response.Error("Error"+e);
//        }
//    }
//
//    @Override
//    public Map history(Principal principal) {
//        try {
//            User idUser = getUserIdToken(principal, userDetailsService);
//            User userr = userRepository.getbyID(idUser.getId());
//            if (userr == null) {
//                return response.notFound("User notfound");
//            }
//            List<SimplerPayment> getHistory = simplerRepository.getHistory(idUser);
//
//            return response.Sukses(getHistory);
//        } catch (Exception e) {
//            return response.Error("error"+e);
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
//
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
//        String tempFileName = "payment/" + UUID.randomUUID() + "." + extension;
//
//        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, tempFileName, file.getInputStream(),
//                metadata);
//        amazonS3.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));
//
//        return tempFileName;
//    }
//}
