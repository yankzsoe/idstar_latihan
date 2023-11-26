package com.synrgy.commit.service.impl;

import com.synrgy.commit.model.Follow;
import com.synrgy.commit.model.Like;
import com.synrgy.commit.model.Post;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.repository.LikeRepository;
import com.synrgy.commit.repository.PostRepository;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.service.LikeService;
import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
import com.synrgy.commit.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.Date;
import java.util.Map;

@Service
public class LikeImpl implements LikeService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private Oauth2UserDetailsService userDetailsService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    Response response;

    @Autowired
    LikeRepository likeRepository;

    @Override
    public Map like(Long idPost, Principal principal) {
        Post checkId = postRepository.getbyID(idPost);
        User idUser = getUserIdToken(principal, userDetailsService);
        try {
            if (response.chekNull(checkId)) {
                return response.Error("id post not found!");
            }
            if (idUser == null) {
                return response.Error("Please Login!");
            }

            Like findIdLike = likeRepository.findUserByIdUserAndIdPost(idUser,checkId.getId_post());
            if (findIdLike == null) {
                checkId.setTotal_like(checkId.getTotal_like() + 1);
                postRepository.save(checkId);

                Like objSave = new Like();
                objSave.setId_post(checkId.getId_post());
                objSave.setId_user(idUser);
                likeRepository.save(objSave);
                return response.Sukses(objSave);
            }
            return response.Error("Already like!");

        } catch (Exception e) {
            return response.Error(e);
        }

    }

    @Override
    public Map unlike(Long idPost, Principal principal) {
        Post checkId = postRepository.getbyID(idPost);
        User idUser = getUserIdToken(principal, userDetailsService);

        try {
            if (response.chekNull(checkId)) {
                return response.Error("id post not found!");
            }
            if (idUser == null) {
                return response.Error("Please Login!");
            }

            Like findIdLike = likeRepository.findUserByIdUserAndIdPost(idUser,checkId.getId_post());
            if (findIdLike == null){
                return response.Error("Already Unfollow!");
            }

            checkId.setTotal_like(checkId.getTotal_like() - 1);
            postRepository.save(checkId);

            findIdLike.setDeleted_date(new Date());
            likeRepository.save(findIdLike);
            return response.Sukses("Success Unlike!");
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
}