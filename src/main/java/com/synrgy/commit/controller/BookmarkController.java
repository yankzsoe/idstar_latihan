package com.synrgy.commit.controller;

import com.synrgy.commit.model.Bookmark;
import com.synrgy.commit.model.Post;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.repository.BookmarkRepository;
import com.synrgy.commit.repository.FollowRepository;
import com.synrgy.commit.repository.LikeRepository;
import com.synrgy.commit.repository.PostRepository;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.service.BookmarkService;
import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
import com.synrgy.commit.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookmark")
public class BookmarkController {
    @Autowired
    BookmarkService bookmarkService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    Response response;
    @Autowired
    PostRepository postRepository;
    @Autowired
    BookmarkRepository bookmarkRepository;
    @Autowired
    private Oauth2UserDetailsService userDetailsService;
    @Autowired
    FollowRepository followRepository;
    @Autowired
    LikeRepository likeRepository;

    @PostMapping("/save/{idPost}")
    public ResponseEntity<Map> bookmark(@PathVariable(value = "idPost") Long idPost,
                                      Principal principal) {
        Map map = bookmarkService.bookmark(idPost, principal);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{idPost}")
    public ResponseEntity<Map> unbookmark(@PathVariable(value = "idPost") Long idPost,
                                      Principal principal) {
        Map map = bookmarkService.unbookmark(idPost, principal);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @GetMapping("list")
    public ResponseEntity<Map> listPost(Principal principal) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            List<Long> getIdPostBookmark = bookmarkRepository.getIdPostBookmark(idUser.getId());
            List<Long> getIsLiked = likeRepository.getIsLike(idUser);
            List<Post> getListPostBookmarked = postRepository.getListPostBookmarked(getIsLiked,getIdPostBookmark);
            return new ResponseEntity<Map>(response.Sukses(getListPostBookmarked), new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.ControllerError(e+"Error"), HttpStatus.BAD_REQUEST);
        }
    }

    public User getUserIdToken(Principal principal, Oauth2UserDetailsService userDetailsService) {
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
