package com.synrgy.commit.service.piksi.impl;

import com.synrgy.commit.config.Config;
import com.synrgy.commit.controller.fileupload.FileController;
import com.synrgy.commit.model.Bookmark;
import com.synrgy.commit.model.Post;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.model.piksi.Penugasan;
import com.synrgy.commit.model.piksi.ReportAbsensi;
import com.synrgy.commit.repository.BookmarkRepository;
import com.synrgy.commit.repository.PostRepository;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.repository.piksi.AbsensiRepository;
import com.synrgy.commit.repository.piksi.PenugasanRepo;
import com.synrgy.commit.service.BookmarkService;
import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
import com.synrgy.commit.service.piksi.AbsensiService;
import com.synrgy.commit.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.Date;
import java.util.Map;

@Transactional
@Service
public class AbsensiImpl implements AbsensiService {
    private static final Logger logger = LoggerFactory.getLogger(AbsensiImpl.class);
    @Autowired
    Response response;
    @Autowired
    AbsensiRepository absensiRepository;

    @Autowired
    PenugasanRepo penugasanRepo;

    Config config = new Config();
    @Autowired
    UserRepository userRepository;
    @Autowired
    private Oauth2UserDetailsService userDetailsService;

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

    @Override
    public Map insert(Principal principal,ReportAbsensi obj) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null){
                return response.Error("User not found");
            }
            //chek penugasan
            if (null == obj.getPenugasan()) {
                return response.Error("Penugasan not is empty.");
            }
            Penugasan penugasan = penugasanRepo.getbyID(obj.getPenugasan().getId());
            if (null == penugasan) {
                logger.error("Penugasan not found.",obj.getPenugasan().getId());
                return response.Error("Penugasan not found.");
            }

            if (obj.getType() == null){
                return response.Error("Type not found. Expectation type is check_in or check_out.");
            }
            if(obj.getType().equals("check_in") || obj.getType().equals("check_out")){

            }else{
                return response.Error("Expectation type is check_in or check_out. You send this value is "+obj.getType());
            }

            //chek duplikat chekin
//            ReportAbsensi chekDuplikatChekin = absensiRepository.chekDuplikatChekin(obj.getType(),config.dateToString(new Date()),idUser);
//            if(chekDuplikatChekin != null){
//                return response.Error("Already Chek In at time "+chekDuplikatChekin.getDate() );
//            }
            obj.setId_user(idUser);
            absensiRepository.save(obj);
            return response.Sukses("Success "+obj.getType());

        } catch (Exception e) {
            return response.Error(e);
        }
    }
}
