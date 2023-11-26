package com.synrgy.commit.service.impl;

import com.synrgy.commit.model.Bookmark;
import com.synrgy.commit.model.Post;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.repository.BookmarkRepository;
import com.synrgy.commit.repository.PostRepository;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.service.BookmarkService;
import com.synrgy.commit.service.FollowService;
import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
import com.synrgy.commit.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.awt.print.Book;
import java.security.Principal;
import java.util.Date;
import java.util.Map;

@Service
public class BookmarkImpl implements BookmarkService {

    @Autowired
    Response response;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private Oauth2UserDetailsService userDetailsService;
    @Autowired
    BookmarkRepository bookmarkRepository;

    @Override
    public Map bookmark(Long idPost, Principal principal) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            Post check = postRepository.getbyID(idPost);
            if (check == null){
                return response.Error("Post not found");
            }
            Bookmark find = bookmarkRepository.findBookmark(idUser.getId(), idPost);
            if (find != null) {
                return response.Error("Already Bookmark!");
            }

            Bookmark bookmark = new Bookmark();
            bookmark.setId_user(idUser.getId());
            bookmark.setId_post(check.getId_post());
            bookmarkRepository.save(bookmark);
            return response.Sukses("Success Bookmark!");

        } catch (Exception e) {
            return response.Error(e);
        }
    }

    @Override
    public Map unbookmark(Long idPost, Principal principal) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            Post check = postRepository.getbyID(idPost);
            if (check == null){
                return response.Error("Post not found");
            }
            Bookmark find = bookmarkRepository.findBookmark(idUser.getId(), idPost);
            if (find == null) {
                return response.Error("Bookmark first!");
            }
            find.setDeleted_date(new Date());

            Bookmark save = bookmarkRepository.save(find);
            return response.Sukses("Success Unbookmark!");

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
