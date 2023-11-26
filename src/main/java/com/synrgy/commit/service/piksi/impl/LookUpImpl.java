package com.synrgy.commit.service.piksi.impl;

import com.synrgy.commit.config.Config;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.model.piksi.Kurikulum;
import com.synrgy.commit.model.piksi.KurikulumMataKuliah;
import com.synrgy.commit.model.piksi.Lookup;
import com.synrgy.commit.model.piksi.MataKuliah;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.repository.piksi.KurikulumMatkulRepository;
import com.synrgy.commit.repository.piksi.KurikulumRepository;
import com.synrgy.commit.repository.piksi.LookupRepository;
import com.synrgy.commit.repository.piksi.MatkulRepository;
import com.synrgy.commit.service.UserService;
import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
import com.synrgy.commit.service.piksi.KurikulumService;
import com.synrgy.commit.service.piksi.LookUpService;
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
public class LookUpImpl implements LookUpService {
    private static final Logger logger = LoggerFactory.getLogger(LookUpImpl.class);
    @Autowired
    Response response;

    @Autowired
    LookupRepository lookupRepository;

    @Autowired
    MatkulRepository matkulRepository;

    @Autowired
    public UserService userService;


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
    public Map insert(Principal principal, Lookup req) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null) {
                return response.Error("User not found");
            }

            if (StringUtils.isEmpty(req.getNama())) {
                return response.Error("Deksripsi Wajib diisi");
            }
            if (StringUtils.isEmpty(req.getType())) {
                return response.Error("Type Wajib diisi");
            }
            return response.Sukses(lookupRepository.save(req));
        } catch (Exception e) {
            return response.Error(e);
        }
    }


    @Override
    public Map update(Principal principal, Lookup req) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null) {
                return response.Error("User not found");
            }

            if (StringUtils.isEmpty(req.getNama())) {
                return response.Error("Deksripsi Wajib diisi");
            }
            if (StringUtils.isEmpty(req.getType())) {
                return response.Error("Type Wajib diisi");
            }

            Lookup doUpdate = lookupRepository.getbyID(req.getId());
            if (doUpdate == null) {
                return response.Error("Lookup Id " + req.getId() + " tidak ditemukan.");
            }
            doUpdate.setType(req.getType());
            doUpdate.setNama(req.getNama());
            return response.Sukses(lookupRepository.save(req));
        } catch (Exception e) {
            return response.Error(e);
        }
    }

    @Override
    public Map delete(Principal principal, Lookup req) {
        try {

            Lookup doUpdate = lookupRepository.getbyID(req.getId());
            if (doUpdate == null) {
                return response.Error("Lookup Id " + req.getId() + " tidak ditemukan.");
            }

            doUpdate.setDeleted_date(new Date());
            lookupRepository.save(doUpdate);
            return response.Sukses("Sukses Deleted");
        } catch (Exception e) {
            return response.Error(e);
        }
    }
}
