package com.synrgy.commit.service.impl;

import com.synrgy.commit.model.Komentar;
import com.synrgy.commit.model.Post;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.repository.KomentarRepository;
import com.synrgy.commit.repository.PostRepository;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.service.KomentarService;
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
public class KomentarImpl implements KomentarService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    Response response;

    @Autowired
    KomentarRepository komentarRepository;

    @Autowired
    private Oauth2UserDetailsService userDetailsService;

    @Override
    public Map insert(Komentar objModel, Long idPost, Principal principal) {
        Post checkId = postRepository.getbyID(idPost);
        User idUser = getUserIdToken(principal, userDetailsService);
        try {
            if (response.chekNull(checkId)) {
                return response.Error("id post not found!");
            }
            if (idUser == null) {
                return response.Error("Please Login!");
            }

            if (response.chekNull(objModel.getIsiKomentar())){
                return response.Error("isi komentar is required");
            }

            if (objModel.getIsiKomentar().length() > 200) {
                return response.Error("Must be less than 200!");
            }

            checkId.setTotal_komentar(checkId.getTotal_komentar() + 1);
            postRepository.save(checkId);

            Komentar komentar = new Komentar();

            komentar.setIsiKomentar(objModel.getIsiKomentar());
            komentar.setId_post(checkId);
            komentar.setId_user(idUser);

            Komentar obj = komentarRepository.save(komentar);
            return response.Sukses(obj);
        } catch (Exception e) {
            return response.Error(e);
        }
    }

    @Override
    public Map delete(Long idKomentar, Principal principal) {
        User idUser = getUserIdToken(principal, userDetailsService);
        Komentar findIdKomentar = komentarRepository.getbyID(idKomentar);

        try {
                if (response.chekNull(findIdKomentar)) {
                    return response.Error("id komentar not found!");
                }
                if (idUser == null) {
                    return response.Error("Please Login!");
                }
                if(findIdKomentar.getId_user() != idUser){
                    return response.Error("You cant delete this comment!");
                }

                Post checkId = postRepository.getbyID(findIdKomentar.getId_post().getId_post());

                checkId.setTotal_komentar(checkId.getTotal_komentar() - 1);
                postRepository.save(checkId);

                findIdKomentar.setDeleted_date(new Date());
                komentarRepository.save(findIdKomentar);

            return response.Sukses("Success Delete!");
        } catch (Exception e){
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
