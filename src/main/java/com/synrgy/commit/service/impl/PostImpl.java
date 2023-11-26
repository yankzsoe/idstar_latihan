//package com.synrgy.commit.service.impl;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.synrgy.commit.dao.request.PostModel;
//import com.synrgy.commit.model.FilePost;
//import com.synrgy.commit.model.Komentar;
//import com.synrgy.commit.model.Post;
//import com.synrgy.commit.model.oauth.User;
//import com.synrgy.commit.repository.*;
//import com.synrgy.commit.repository.oauth.UserRepository;
//import com.synrgy.commit.service.PostService;
//import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
//import com.synrgy.commit.util.Response;
//import org.apache.commons.io.FilenameUtils;
//import org.apache.commons.lang3.ArrayUtils;
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
//import java.util.*;
//
//
//@Service
//public class PostImpl implements PostService {
//
//    @Value("${document.bucket-name}")
//    private String bucketName;
//    @Value("${file.base.url.aws}")
//    private String fileBaseUrl;
//    @Autowired
//    private AmazonS3 amazonS3;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    private Oauth2UserDetailsService userDetailsService;
//    @Autowired
//    Response response;
//    @Autowired
//    PostRepository postRepository;
//    @Autowired
//    LikeRepository likeRepository;
//    @Autowired
//    KomentarRepository komentarRepository;
//    @Autowired
//    FollowRepository followRepository;
//    @Autowired
//    FilePostRepository filePostRepository;
//    @Autowired
//    BookmarkRepository bookmarkRepository;
//
//
//    @Override
//    public Map postsave(PostModel objModel, Principal principal, MultipartFile[] file) {
//
//        try {
//            User idUser = getUserIdToken(principal, userDetailsService);
//            if (idUser == null) {
//                return response.notFound("User id notfound");
//            }
//            if (objModel.getStatus().equals(true)) {
//                if(idUser.getStatus().equals("User") || idUser.getStatus().equals("Subscribed")) {
//                    return response.Error("You cant post to Simpler!");
//                }
//            }
//            if (response.chekNull(objModel.getDesc())) {
//                return response.Error("Desc is required");
//            }
//            if (response.chekNull(objModel.getTags())) {
//                return response.Error("Tags is required");
//            }
//            if (response.chekNull(objModel.getStatus())) {
//                return response.Error("Status is required");
//            }
//            if (objModel.getDesc().length() > 1000) {
//                return response.Error("Must be less than 1000!");
//            }
//
//            Post post = new Post();
//            post.setUser(idUser);
//            post.setPost_status(objModel.getStatus());
//            post.setPost_tags(objModel.getTags());
//            post.setPost_desc(objModel.getDesc());
//            Post obj = postRepository.save(post);
//
//            if (file.length > 5) {
//                return response.Error("Max 5 file!");
//            }
//
//            int checkVid = 0;
//
//
//            for (int i = 0; i < file.length; i++) {
//                String extension = FilenameUtils.getExtension(file[i].getOriginalFilename());
//
//                if (extension.equals("mp4")){
//                    checkVid = checkVid+1;
//                }
//            }
//
//            System.out.println(checkVid);
//
//            if (checkVid >= 1 && file.length > 1) {
//                return response.Error("can only one video file!");
//            }
//
//
//            for (int i = 0; i < file.length; i++) {
//                String extension = FilenameUtils.getExtension(file[i].getOriginalFilename());
//
//                if (extension.equals("mp4")) {
//                    if (file[i].getSize() > 104857600) {
//                        return response.Error("File too large, Max 100MB for video!");
//                    }
//                }
//                if (extension.equals("jpeg") || extension.equals("jpg") || extension.equals("png")) {
//                    if (file[i].getSize() > 5242880) {
//                        return response.Error("File too large, Max 5MB for image!");
//                    }
//                }
//
//                    String tempFileName = this.upload(file[i]);
//
//                    FilePost filePost = new FilePost();
//                    filePost.setPost(obj);
//                    filePost.setUrl(fileBaseUrl + tempFileName);
//
//                    FilePost save = filePostRepository.save(filePost);
//            }
//
//
//            return response.Sukses("Success post!");
//        } catch (Exception e) {
//            return response.Error("eror:"+e);
//        }
//    }
//
//    @Override
//    public Map postedit(PostModel objModel, Long idPost, Principal principal) {
//        Post checkId = postRepository.getbyID(idPost);
//        User idUser = getUserIdToken(principal, userDetailsService);
//        try {
//            if (idUser == null) {
//                return response.notFound("User id notfound");
//            }
//            if (response.chekNull(checkId)) {
//                return response.Error("Post Not Found");
//            }
//            if(checkId.getUser() != idUser){
//                return response.Error("You cant Edit this post!");
//            }
//            if (response.chekNull(objModel.getDesc())) {
//                return response.Error("Desc is required");
//            }
//            if (response.chekNull(objModel.getTags())) {
//                return response.Error("Tags is required");
//            }
//            if (response.chekNull(objModel.getStatus())) {
//                return response.Error("Status is required");
//            }
//            if (objModel.getDesc().length() > 1000) {
//                return response.Error("Must be less than 1000");
//            }
//
//            checkId.setPost_status(objModel.getStatus());
//            checkId.setPost_tags(objModel.getTags());
//            checkId.setPost_desc(objModel.getDesc());
//            Post obj = postRepository.save(checkId);
//            return response.Sukses(obj);
//
//        } catch (Exception e) {
//            return response.Error("eror:"+e);
//        }
//
//    }
//
//    @Override
//    public Map postdelete(Long idPost, Principal principal) {
//        Post checkId = postRepository.getbyID(idPost);
//        User idUser = getUserIdToken(principal, userDetailsService);
//        try {
//            if (idUser == null) {
//                return response.notFound("User id notfound");
//            }
//
//            if (response.chekNull(checkId)) {
//                return response.Error("Post Not Found");
//            }
//
//            if(checkId.getUser() != idUser){
//                return response.Error("You cant Delete this post!");
//            }
//
//            checkId.setDeleted_date(new Date());
//
//            Post obj = postRepository.save(checkId);
//            return response.Sukses("Success Delete Post!");
//
//        } catch (Exception e) {
//            return response.Error("eror:"+e);
//        }
//    }
//
//
//    @Override
//    public Map listPost(Principal principal) {
//        User idUser = getUserIdToken(principal, userDetailsService);
//        List<Long> getIdPostBookmark = bookmarkRepository.getIdPostBookmark(idUser.getId());
//        List<Long> followingData = followRepository.getFollowingdata(idUser.getId());
//        List<User> getListId = userRepository.getListId(followingData);
//        List<Long> getIsLiked = likeRepository.getIsLike(idUser);
//        List<Post> getListPost = postRepository.getListPost(getIsLiked,getListId,getIdPostBookmark);
//        try {
//            if (getListPost.isEmpty()) {
//                return response.Error("null");
//            }
//
//            return response.Sukses(getListPost);
//        } catch (Exception e) {
//            return response.Error(e);
//        }
//    }
//
//    @Override
//    public Map detailpost(Long idPost, Principal principal) {
//        User idUser = getUserIdToken(principal, userDetailsService);
//        List<Long> getIdPostBookmark = bookmarkRepository.getIdPostBookmark(idUser.getId());
//        List<Long> getIsLiked = likeRepository.getIsLike(idUser);
//        Post post = postRepository.detailPost(getIsLiked, idPost, getIdPostBookmark);
//        List<Komentar> isiKomentar = komentarRepository.getbyIDpost(post);
//        if (response.chekNull(post)) {
//            return response.Error("Post Not Found");
//        }
//
//        Map<String, Object> map = new HashMap<>();
//
//        map.put("detail_post", post);
////        for(x=0; x < n; x++){
////            map.put("tags_post" + (x+1), tagsSplit[x]);
////        }
//        map.put("komentar_post", isiKomentar);
//
//        return response.Sukses(map);
//    }
//
//    @Override
//    public Map postsaveText(PostModel objModel, Principal principal) {
//        try {
//            User idUser = getUserIdToken(principal, userDetailsService);
//            if (idUser == null) {
//                return response.notFound("User id notfound");
//            }
//            if (objModel.getStatus().equals(true)) {
//                if(idUser.getStatus().equals("User") || idUser.getStatus().equals("Subscribed")) {
//                    return response.Error("You cant post to Simpler!");
//                }
//            }
//            if (response.chekNull(objModel.getDesc())) {
//                return response.Error("Desc is required");
//            }
//            if (response.chekNull(objModel.getTags())) {
//                return response.Error("Tags is required");
//            }
//            if (response.chekNull(objModel.getStatus())) {
//                return response.Error("Status is required");
//            }
//            if (objModel.getDesc().length() > 1000) {
//                return response.Error("Must be less than 1000");
//            }
//
//            Post post = new Post();
//            post.setUser(idUser);
//            post.setPost_status(objModel.getStatus());
//            post.setPost_tags(objModel.getTags());
//            post.setPost_desc(objModel.getDesc());
//            Post obj = postRepository.save(post);
//            return response.Sukses("Success post!");
//        } catch (Exception e) {
//            return response.Error(e);
//        }
//    }
//
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
//        } else if (extension.equals("mp4")) {
//            metadata.setContentType("video/mp4");
//        } else {
//            throw new FileUploadException("Can only upload jpeg, jpg, png, and mp4 file");
//        }
//
//        String nameFiles = file.getOriginalFilename().replaceAll(" ", "-").toLowerCase();
//
//        String tempFileName = "post/" + UUID.randomUUID() + "-" + nameFiles;
//
//        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, tempFileName, file.getInputStream(),
//                metadata);
//        amazonS3.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));
//
//        return tempFileName;
//    }
//
//
//}
