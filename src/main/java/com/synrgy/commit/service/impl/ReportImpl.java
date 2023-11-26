package com.synrgy.commit.service.impl;

import com.synrgy.commit.model.*;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.repository.*;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.service.email.EmailSender;
import com.synrgy.commit.service.ReportService;
import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
import com.synrgy.commit.util.EmailTemplate;
import com.synrgy.commit.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.Map;

@Service
public class ReportImpl implements ReportService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private Oauth2UserDetailsService userDetailsService;
    @Autowired
    Response response;
    @Autowired
    PostRepository postRepository;
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    KomentarRepository komentarRepository;
    @Autowired
    ReportCommmentRepository reportCommmentRepository;
    @Autowired
    ReportUserRepository reportUserRepository;
    @Autowired
    public EmailSender emailSender;
    @Autowired
    public EmailTemplate emailTemplate;

    @Override
    public Map report(Long idPost, Principal principal, ReportPost rp) {
        try{
            String template = emailTemplate.Report();
            User idUser = getUserIdToken(principal, userDetailsService);
            Post post = postRepository.getbyID(idPost);
            if (post == null) {
                return response.Error("Post not found");
            }
            if (rp.getReason().length() > 200) {
                return response.Error("Max 200 character!");
            }
            if (rp.getReason().isEmpty()) {
                return response.Error("Fill the reason");
            }
            ReportPost reportPost = new ReportPost();
            reportPost.setId_post(post);
            reportPost.setId_user(idUser);
            reportPost.setReason(rp.getReason());

            ReportPost rps = reportRepository.save(reportPost);


            template = template.replaceAll("\\{\\{NAMA}}", rps.getId_user().getFullname());

            emailSender.sendAsync(rps.getId_user().getUsername(), "Absensi - Post Report", template);

            return response.Sukses(rps);
        } catch (Exception e) {
            return response.Error("error"+e);
        }
    }

    @Override
    public Map reportComment(Long idKomentar, Principal principal, ReportComment reportComment) {
        try {
            String template = emailTemplate.Report();
            User idUser = getUserIdToken(principal, userDetailsService);
            Komentar komen = komentarRepository.getbyID(idKomentar);
            if (komen == null) {
                return response.Error("Comment not found");
            }
            if (reportComment.getReason().length() > 200) {
                return response.Error("Max 200 character!");
            }
            if (reportComment.getReason().isEmpty()) {
                return response.Error("Fill the reason");
            }
            ReportComment rp = new ReportComment();
            rp.setId_komentar(komen);
            rp.setId_user(idUser);
            rp.setReason(reportComment.getReason());

            ReportComment rpc = reportCommmentRepository.save(rp);

            template = template.replaceAll("\\{\\{NAMA}}", rpc.getId_user().getFullname());


            emailSender.sendAsync(rpc.getId_user().getUsername(), "Absensi - Comment Report", template);

            return response.Sukses(rpc);
        } catch (Exception e) {
            return response.Error("Error"+e);
        }
    }

    @Override
    public Map reportUser(Long id_user, Principal principal, ReportUser reportUser) {
        try {
            String template = emailTemplate.Report();
            User idUser = getUserIdToken(principal, userDetailsService);
            User user = userRepository.getbyID(id_user);
            if (user == null) {
                return response.Error("User not found");
            }
            if (reportUser.getReason().length() > 200) {
                return response.Error("Max 200 character!");
            }
            if (reportUser.getReason().isEmpty()) {
                return response.Error("Fill the reason");
            }
            ReportUser rps = new ReportUser();
            rps.setOtherUser(user);
            rps.setId_user(idUser);
            rps.setReason(reportUser.getReason());

            ReportUser save = reportUserRepository.save(rps);

            template = template.replaceAll("\\{\\{NAMA}}", rps.getId_user().getFullname());

            emailSender.sendAsync(rps.getId_user().getUsername(), "Absensi - User Report", template);

            return response.Sukses(save);
        } catch (Exception e) {
            return response.Error("Error"+e);
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
