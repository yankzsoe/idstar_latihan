package com.synrgy.commit.controller;

import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.repository.FollowRepository;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.service.UserService;
import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
import com.synrgy.commit.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    Response response;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private Oauth2UserDetailsService userDetailsService;

    @Autowired
    UserService userService;

    @Autowired
    FollowRepository followRepository;

    @GetMapping("/list")
    public ResponseEntity<Map> getList(
            Principal principal,
            @RequestParam(required = false) String fullname){
        List<User> list = null;
        User idUser = getUserIdToken(principal, userDetailsService);
        List<Long> followingData = followRepository.getFollowingdata(idUser.getId());
        if (fullname == null) {
            list = userRepository.getAllUser(followingData, idUser.getId());
        } else {
            list = userRepository.getUserByNama("%" + fullname.toLowerCase() + "%", followingData, idUser.getId());
            if (list.isEmpty()) {
                return new ResponseEntity<Map>(response.Error("null"), new HttpHeaders(), HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<Map>(response.Sukses(list), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/detail-profile")
    public ResponseEntity<Map> detailProfile(
            Principal principal
    ){
        Map map = userService.getDetailProfile(principal);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @GetMapping("/detail/{idUserDetail}")
    public ResponseEntity<Map> detail(
            @PathVariable(value = "idUserDetail") Long idUserDetail,
            Principal principal
    ){
        Map map = userService.getDetail(principal, idUserDetail);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @GetMapping("/sugested")
    public ResponseEntity<Map> sugested(Principal principal){
        Map map = userService.sugestedPeople(principal);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    public List<Map> getUserCustom(
            List<User> data
    )
    {
      List<Map> alldata = new ArrayList<>();

      for (User user : data){
          Map model = new HashMap<>();
          model.put("username", user.getUsername());
          model.put("fullname", user.getFullname());
          model.put("gender", user.getGender());
          alldata.add(model);
      }
      return alldata;
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
