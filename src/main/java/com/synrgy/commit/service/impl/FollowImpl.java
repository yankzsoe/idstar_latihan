package com.synrgy.commit.service.impl;

import com.synrgy.commit.model.Follow;
import com.synrgy.commit.model.Post;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.repository.FollowRepository;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.service.FollowService;
import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
import com.synrgy.commit.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FollowImpl implements FollowService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Response response;
    
    @Autowired
    FollowRepository followRepository;

    @Autowired
    private Oauth2UserDetailsService userDetailsService;

    @Override
    public Map follow(Long followIdUser, Principal principal) {

        User checkId = userRepository.getbyID(followIdUser);
        User idUser = getUserIdToken(principal, userDetailsService);
        try {
            if (followIdUser == idUser.getId()) {
                return response.Error("disabled!");
            }
            if (response.chekNull(checkId)) {
                return response.Error("User not found!");
            }
            if (idUser == null) {
                return response.notFound("User id notfound!");
            }

            Follow findIdUser = followRepository.findUserByIdUserAndIdFollowing(idUser.getId(), checkId.getId());


           if (findIdUser == null){
               Follow objSave = new Follow();
               objSave.setId_user(idUser.getId());
               objSave.setId_user_following(checkId.getId());
               objSave.setIs_follow(true);
               objSave.setDeleted_date(null);

               Follow dosave = followRepository.save(objSave);

               Long getFollowers = followRepository.getFollowers(idUser.getId());
               Long getFollowing = followRepository.getFollowing(idUser.getId());

               idUser.setTotal_following(getFollowing);
               idUser.setTotal_followers(getFollowers);

               userRepository.save(idUser);

               User other = userRepository.getbyID(followIdUser);

               Long getFollowersOther = followRepository.getFollowers(other.getId());
               Long getFollowingOther = followRepository.getFollowing(other.getId());
               other.setTotal_followers(getFollowersOther);
               other.setTotal_following(getFollowingOther);

               userRepository.save(other);

               return response.Sukses("Success Follow!");
           }

           return response.Error("Already Follow");

        } catch (Exception e ) {
            return response.Error(e);
        }

    }

    @Override
    public Map unfollow(Long followIdUser, Principal principal) {
        User checkId = userRepository.getbyID(followIdUser);
        User idUser = getUserIdToken(principal, userDetailsService);
        Follow findIdUser = followRepository.findUserByIdUserAndIdFollowing(idUser.getId(), checkId.getId());
        try {
            if (followIdUser == idUser.getId()) {
                return response.Error("disabled!");
            }
            if (response.chekNull(checkId)) {
                return response.Error("User not found!");
            }
            if (idUser == null) {
                return response.notFound("User id notfound!");
            }

            if (findIdUser == null) {
                return response.Error("Already Unfollow!");
            }

            Follow checkIdFollow = followRepository.getbyID(findIdUser.getId_follow());
            if (response.chekNull(checkIdFollow)) {
                return response.Error("Id Follow not found!");
            }

            checkIdFollow.setDeleted_date(new Date());
            followRepository.save(checkIdFollow);
            Long getFollowers = followRepository.getFollowers(idUser.getId());
            Long getFollowing = followRepository.getFollowing(idUser.getId());

            idUser.setTotal_following(getFollowing);
            idUser.setTotal_followers(getFollowers);

            userRepository.save(idUser);

            User other = userRepository.getbyID(followIdUser);

            Long getFollowersOther = followRepository.getFollowers(other.getId());
            Long getFollowingOther = followRepository.getFollowing(other.getId());
            other.setTotal_followers(getFollowersOther);
            other.setTotal_following(getFollowingOther);

            userRepository.save(other);
            return response.Sukses("Success Unfollow!");
        } catch (Exception e){
            return response.Error(e);
        }

    }

    @Override
    public Map listFollowing(Long userId, Principal principal) {
        try {
            User checkid = userRepository.getbyID(userId);
            if (response.chekNull(userId)){
                return response.Error("id user required!");
            }
            if (response.chekNull(checkid)){
                return response.Error("user not found");
            }
            User idUser = getUserIdToken(principal, userDetailsService);
            List<Long> followingData = followRepository.getFollowingdata(idUser.getId());
            List<Long> getFollowingdata = followRepository.getFollowingdata(userId);
            List<User> getListFollowing = userRepository.getListFollowing(followingData,getFollowingdata);

            return response.Sukses(getListFollowing);
        } catch (Exception e) {
            return response.Error(e);
        }
    }

    @Override
    public Map listFollowers(Long userId, Principal principal) {
        try {
            User checkid = userRepository.getbyID(userId);
            if (response.chekNull(userId)){
                return response.Error("id user required!");
            }
            if (response.chekNull(checkid)){
                return response.Error("user not found");
            }
            User idUser = getUserIdToken(principal, userDetailsService);
            List<Long> followingData = followRepository.getFollowingdata(idUser.getId());
            List<Long> getFollowingdata = followRepository.getFollowersdata(userId);
            List<User> getListFollowing = userRepository.getListFollowing(followingData,getFollowingdata);

            return response.Sukses(getListFollowing);
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
