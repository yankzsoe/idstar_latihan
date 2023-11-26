//package com.synrgy.commit.controller;
//
//import com.synrgy.commit.dao.request.PostModel;
//import com.synrgy.commit.model.Post;
//import com.synrgy.commit.model.oauth.User;
//import com.synrgy.commit.repository.BookmarkRepository;
//import com.synrgy.commit.repository.FollowRepository;
//import com.synrgy.commit.repository.LikeRepository;
//import com.synrgy.commit.repository.PostRepository;
//import com.synrgy.commit.repository.oauth.UserRepository;
//import com.synrgy.commit.service.PostService;
//import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
//import com.synrgy.commit.util.Response;
//import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.security.Principal;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/post")
//public class PostController {
//
//    @Autowired
//    Response response;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    private Oauth2UserDetailsService userDetailsService;
//
//    @Autowired
//    PostService postService;
//
//    @Autowired
//    PostRepository postRepository;
//
//    @Autowired
//    FollowRepository followRepository;
//
//    @Autowired
//    LikeRepository likeRepository;
//
//    @Autowired
//    BookmarkRepository bookmarkRepository;
//
//
//    @Value("${app.uploadto.cdn}")//FILE_SHOW_RUL
//    private String UPLOADED_FOLDER;
//
//
//    @PostMapping(value = "/save", consumes = {"multipart/form-data", "application/json"})
//    public ResponseEntity<Map> insert (PostModel postModel, Principal principal,
//                                       @RequestParam(value = "file", required = false) MultipartFile[] file)  {
//        try {
//
//            if (file != null) {
//                Map map = postService.postsave(postModel, principal, file);
//                return new ResponseEntity<Map>(map, HttpStatus.OK);
//            } else {
//                Map map = postService.postsaveText(postModel, principal);
//                return new ResponseEntity<Map>(map, HttpStatus.OK);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<Map>(response.ControllerError("Error"), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @PutMapping("/edit/{idPost}")
//    public ResponseEntity<Map> edit(@PathVariable(value = "idPost") Long idPost,
//                                    PostModel postModel, Principal principal){
//        Map map = postService.postedit(postModel, idPost, principal);
//        return new ResponseEntity<Map>(map, HttpStatus.OK);
//    }
//
//
//    @DeleteMapping("/delete/{idPost}")
//    public ResponseEntity<Map> delete(@PathVariable(value = "idPost") Long idPost,
//                                      Principal principal) {
//        Map map = postService.postdelete(idPost,principal);
//        return new ResponseEntity<Map>(map, HttpStatus.OK);
//    }
//
//    @GetMapping ("/list")
//    public ResponseEntity<Map> listPost(Principal principal,
//                                        @RequestParam(required = false) String tags
//    ) {
//        List<Post> listPost = null;
//        User idUser = getUserIdToken(principal, userDetailsService);
//        List<Long> getIdPostBookmark = bookmarkRepository.getIdPostBookmark(idUser.getId());
//        List<Long> followingData = followRepository.getFollowingdata(idUser.getId());
//        List<User> getListId = userRepository.getListId(followingData);
//        getListId.add(idUser);
//        List<Long> getIsLiked = likeRepository.getIsLike(idUser);
//        if(tags != null) {
//            listPost = postRepository.getListPostTags(getIsLiked,getListId, "%" + tags.toLowerCase() + "%",getIdPostBookmark);
//            if (listPost.isEmpty()){
//                return new ResponseEntity<Map>(response.Sukses(listPost), new HttpHeaders(), HttpStatus.NOT_FOUND);
//            }
//        } else {
//            listPost = postRepository.getListPost(getIsLiked,getListId,getIdPostBookmark);
//        }
//
//        return new ResponseEntity<Map>(response.Sukses(listPost), new HttpHeaders(), HttpStatus.OK);
//    }
//
//    @GetMapping ("/list-simpler/verified")
//    public ResponseEntity<Map> listPostSimpler(Principal principal,
//                                        @RequestParam(required = false) String tags
//    ) {
//        List<Post> listPost = null;
//        User idUser = getUserIdToken(principal, userDetailsService);
//        List<Long> getIdPostBookmark = bookmarkRepository.getIdPostBookmark(idUser.getId());
//        List<Long> getIsLiked = likeRepository.getIsLike(idUser);
//        if(tags != null) {
//            listPost = postRepository.getListPostSimplerTagsVerified(getIsLiked, "%" + tags.toLowerCase() + "%",getIdPostBookmark);
//            if (listPost.isEmpty()){
//                return new ResponseEntity<Map>(response.Sukses(listPost), new HttpHeaders(), HttpStatus.OK);
//            }
//        } else {
//            listPost = postRepository.getListPostSimplerVerified(getIsLiked,getIdPostBookmark);
//        }
//
//        return new ResponseEntity<Map>(response.Sukses(listPost), new HttpHeaders(), HttpStatus.OK);
//    }
//
//    @GetMapping ("/list-simpler/official")
//    public ResponseEntity<Map> listPostSimplerOff(Principal principal,
//                                               @RequestParam(required = false) String tags
//    ) {
//        List<Post> listPost = null;
//        User idUser = getUserIdToken(principal, userDetailsService);
//        List<Long> getIdPostBookmark = bookmarkRepository.getIdPostBookmark(idUser.getId());
//        List<Long> getIsLiked = likeRepository.getIsLike(idUser);
//        if(tags != null) {
//            listPost = postRepository.getListPostSimplerTagsOff(getIsLiked, "%" + tags.toLowerCase() + "%",getIdPostBookmark);
//            if (listPost.isEmpty()){
//                return new ResponseEntity<Map>(response.Sukses(listPost), new HttpHeaders(), HttpStatus.OK);
//            }
//        } else {
//            listPost = postRepository.getListPostSimplerOff(getIsLiked,getIdPostBookmark);
//        }
//
//        return new ResponseEntity<Map>(response.Sukses(listPost), new HttpHeaders(), HttpStatus.OK);
//    }
//
//    @GetMapping("/detail/{idPost}")
//    public ResponseEntity<Map> detail(@PathVariable(value = "idPost") Long idPost,
//                                      Principal principal) {
//        Map map = postService.detailpost(idPost,principal);
//        return new ResponseEntity<Map>(map, HttpStatus.OK);
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
//}
