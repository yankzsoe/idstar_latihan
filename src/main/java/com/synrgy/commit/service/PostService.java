//package com.synrgy.commit.service;
//
//import com.synrgy.commit.dao.request.PostModel;
//import com.synrgy.commit.model.Post;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.security.Principal;
//import java.util.Map;
//
//public interface PostService {
//    public Map postsave(PostModel objModel, Principal principal, MultipartFile[] file);
//    public Map postedit(PostModel objModel, Long idPost, Principal principal);
//    public Map postdelete(Long idPost, Principal principal);
//    public Map listPost(Principal principal);
//    public Map detailpost(Long idPost, Principal principal);
//    public Map postsaveText(PostModel objModel, Principal principal);
//}
