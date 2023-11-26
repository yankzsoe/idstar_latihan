package com.synrgy.commit.service.impl;

import com.synrgy.commit.config.Config;
import com.synrgy.commit.dao.request.LoginModel;
import com.synrgy.commit.dao.request.RegisterModel;
import com.synrgy.commit.model.Follow;
import com.synrgy.commit.model.Post;
import com.synrgy.commit.model.oauth.Role;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.repository.BookmarkRepository;
import com.synrgy.commit.repository.FollowRepository;
import com.synrgy.commit.repository.LikeRepository;
import com.synrgy.commit.repository.PostRepository;
import com.synrgy.commit.repository.oauth.RoleRepository;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.service.UserService;
import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
import com.synrgy.commit.util.Response;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.slf4j.Logger;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    public static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    Config config = new Config();
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    RoleRepository repoRole;

    @Autowired
    UserRepository repoUser;

    @Autowired
    FollowRepository followRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    PostRepository postRepository;


    @Autowired
    private Oauth2UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    public Response response;

    @Autowired
    public Response responses;

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Override
    public Map registerManual(RegisterModel objModel) {
        Map map = new HashMap();
        try {

            if (!response.isValidEmail(objModel.getEmail())) {
                return response.Error("Email is invalid");
            }

            if (objModel.getEmail() == "") {
                return response.Error("Fill the email");
            }

//            if (objModel.getDomicile() == "") {
//                return response.Error("Choose the domicile");
//            }

//            if (objModel.getGender() == "") {
//                return response.Error("Choose the gender");
//            }

            if (objModel.getPhone_number() == "") {
                return response.Error("Fill the phone number");
            }

            if (objModel.getPassword() == "") {
                return response.Error("Fill the password");
            }

            if (objModel.getName() == "") {
                return response.Error("Fill the name");
            }

//            if (objModel.getInterest() == "") {
//                return response.Error("Choose the interest");
//            }

            if (response.chekNull(objModel.getName())) {
                return response.Error("Name is required");
            }

            if (response.chekNull(objModel.getEmail())) {
                return response.Error("Email is required");
            }

//            if (response.chekNull(objModel.getPhone_number())) {
//                return response.Error("Phone Number is required");
//            }

            if (response.chekNull(objModel.getPassword())) {
                return response.Error("Password is required");
            }

//            if (objModel.getPhone_number().length() < 11 || objModel.getPhone_number().length() > 15) {
//                return response.Error("Phone number Minimum 11 character and maksimum 15 character!");
//            }

            if(objModel.getPassword().length() < 6 ) {
                return response.Error("Password Minimum password 6 character");
            }

            if(objModel.getName().length() > 100) {
                return response.Error("Name must be less than 100!");
            }

//            if(response.isNumeric(objModel.password)) {
//                return response.Error("Password must contain number and letter!");
//            }
//            if(!response.notSimbol(objModel.getPassword())) {
//                return response.Error("password do not use symbols");
//            }

//            if (response.chekNull(objModel.getInterest())) {
//                return response.Error("Interest is required");
//            }

//            if (response.chekNull(objModel.getDomicile())) {
//                return response.Error("Domicile is required");
//            }

//            if (response.chekNull(objModel.getGender())) {
//                return response.Error("Gender is required");
//            }

//            if(!response.isNumeric(objModel.getPhone_number())){
//                return response.Error("Phone number is invalid");
//            }



//            if (!response.notNumber(objModel.getPhone_number())) {
//                return response.Error("Invalid number phone");
//            }

            if(!response.nameNotSimbol(objModel.getName())) {
                return response.Error("Name do not use number or symbol");
            }

            User checkEmail = repoUser.checkExistingEmail(objModel.getEmail());
            if (null != checkEmail) {
                return response.Error("Email is Registered, try another email or click Forget Password");
            }

            String str = objModel.getInterest().replaceAll(",", " | ");

            String[] roleNames = {"ROLE_USER", "ROLE_READ", "ROLE_WRITE"}; // admin
            User user = new User();
            user.setUsername(objModel.getEmail());
            user.setFullname(objModel.getName());
            user.setRegion(objModel.getDomicile());
            user.setStatus("User");
            user.setPhone_number(objModel.getPhone_number());
            user.setGender(objModel.getGender());
            user.setPassion(str);
            user.setTempatLahir(objModel.getTempatLahir());
            user.setTanggalLahir(objModel.getTanggalLahir());
            user.setNamaIbuKandung(objModel.getNamaIbuKandung());

            String password = encoder.encode(objModel.getPassword().replaceAll("\\s+", ""));
            List<Role> r = repoRole.findByNameIn(roleNames);

            user.setRoles(r);
            user.setPassword(password);
            User obj = repoUser.save(user);

            String url = baseUrl + "/login-user?email=" + objModel.getEmail() +
                    "&password=" + objModel.getPassword();
            ResponseEntity<Map> responses = restTemplateBuilder.build().postForEntity(url, objModel, Map.class);
                return response.Sukses(responses.getBody());

        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
            return response.Error("eror:"+e);
        }
    }

    @Override
    public Map registerManualTampaEMail(RegisterModel objModel) {
        Map map = new HashMap();
        try {

            if (!response.isValidEmail(objModel.getEmail())) {
                return response.Error("Email is invalid");
            }

            if (objModel.getEmail() == "") {
                return response.Error("Fill the email");
            }

//            if (objModel.getDomicile() == "") {
//                return response.Error("Choose the domicile");
//            }

//            if (objModel.getGender() == "") {
//                return response.Error("Choose the gender");
//            }

            if (objModel.getPhone_number() == "") {
                return response.Error("Fill the phone number");
            }

            if (objModel.getPassword() == "") {
                return response.Error("Fill the password");
            }

            if (objModel.getName() == "") {
                return response.Error("Fill the name");
            }

//            if (objModel.getInterest() == "") {
//                return response.Error("Choose the interest");
//            }

            if (response.chekNull(objModel.getName())) {
                return response.Error("Name is required");
            }

            if (response.chekNull(objModel.getEmail())) {
                return response.Error("Email is required");
            }

//            if (response.chekNull(objModel.getPhone_number())) {
//                return response.Error("Phone Number is required");
//            }

            if (response.chekNull(objModel.getPassword())) {
                return response.Error("Password is required");
            }

//            if (objModel.getPhone_number().length() < 11 || objModel.getPhone_number().length() > 15) {
//                return response.Error("Phone number Minimum 11 character and maksimum 15 character!");
//            }

            if(objModel.getPassword().length() < 6 ) {
                return response.Error("Password Minimum password 6 character");
            }

            if(objModel.getName().length() > 100) {
                return response.Error("Name must be less than 100!");
            }

//            if(response.isNumeric(objModel.password)) {
//                return response.Error("Password must contain number and letter!");
//            }
//            if(!response.notSimbol(objModel.getPassword())) {
//                return response.Error("password do not use symbols");
//            }

//            if (response.chekNull(objModel.getInterest())) {
//                return response.Error("Interest is required");
//            }

//            if (response.chekNull(objModel.getDomicile())) {
//                return response.Error("Domicile is required");
//            }

//            if (response.chekNull(objModel.getGender())) {
//                return response.Error("Gender is required");
//            }

//            if(!response.isNumeric(objModel.getPhone_number())){
//                return response.Error("Phone number is invalid");
//            }



//            if (!response.notNumber(objModel.getPhone_number())) {
//                return response.Error("Invalid number phone");
//            }

            if(!response.nameNotSimbol(objModel.getName())) {
                return response.Error("Name do not use number or symbol");
            }

            User checkEmail = repoUser.checkExistingEmail(objModel.getEmail());
            if (null != checkEmail) {
                return response.Error("Email is Registered, try another email or click Forget Password");
            }

//            String str = objModel.getInterest().replaceAll(",", " | ");

            String[] roleNames = {"ROLE_USER", "ROLE_READ", "ROLE_WRITE"}; // admin
            User user = new User();
            user.setUsername(objModel.getEmail());
            user.setFullname(objModel.getName());
            user.setRegion(objModel.getDomicile());
            user.setStatus("User");
            user.setPhone_number(objModel.getPhone_number());
            user.setGender(objModel.getGender());
//            user.setPassion(str);
            user.setTempatLahir(objModel.getTempatLahir());
            user.setTanggalLahir(objModel.getTanggalLahir());
            user.setNamaIbuKandung(objModel.getNamaIbuKandung());

            String password = encoder.encode(objModel.getPassword().replaceAll("\\s+", ""));
            List<Role> r = repoRole.findByNameIn(roleNames);

            user.setRoles(r);
            user.setPassword(password);
            user.setEnabled(true);
            User obj = repoUser.save(user);
//jika ingin dapatkan token ototmatis
//            String url = baseUrl + "/login-user?email=" + objModel.getEmail() +
//                    "&password=" + objModel.getPassword();
//            ResponseEntity<Map> responses = restTemplateBuilder.build().postForEntity(url, objModel, Map.class);
            return response.Sukses("Silahkan Lakukan Login.");

        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
            return response.Error("eror:"+e);
        }
    }

    @Value("${BASEURL}")
    private String baseUrl;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Override
    public Map login(LoginModel loginModel) {
        /**
         * bussines logic for login here
         * **/
        try {

            if (loginModel.getEmail() == "") {
                return response.Error("Fill the email");
            }

            if (loginModel.getPassword() == "") {
                return response.Error("Fill the password");
            }

            if(response.chekNull(loginModel.getEmail())) {
                return response.Error("Email is required");
            }

            if(response.chekNull(loginModel.getPassword())) {
                return response.Error("Password is required");
            }

            Map<String, Object> map = new HashMap<>();

            User checkUser = repoUser.findOneByUsername(loginModel.getEmail());

            if (checkUser.isBlocked()) {
                return response.Error("Your account is blocked, please contact Admin!");
            }

            if ((checkUser != null) && (encoder.matches(loginModel.getPassword(), checkUser.getPassword()))) {
                if (!checkUser.isEnabled()) {
                    map.put("is_enabled", checkUser.isEnabled());
                    return response.Error(map);
                }
            }
            if (checkUser == null) {
                return response.notFound("User not found");
            }
            if (!(encoder.matches(loginModel.getPassword(), checkUser.getPassword()))) {
                return response.Error("Wrong Password");
            }
            String url = baseUrl + "/oauth/token?username=" + loginModel.getEmail() +
                    "&password=" + loginModel.getPassword() +
                    "&grant_type=password" +
                    "&client_id=my-client-web" +
                    "&client_secret=password";
            ResponseEntity<Map> response = restTemplateBuilder.build().exchange(url, HttpMethod.POST, null, new
                    ParameterizedTypeReference<Map>() {
                    });
            System.out.println("reponse="+response.getBody());
            if (response.getStatusCode() == HttpStatus.OK) {
                User user = repoUser.findOneByUsername(loginModel.getEmail());
                List<String> roles = new ArrayList<>();

                for (Role role : user.getRoles()) {
                    roles.add(role.getName());
                }
                //save token
//                checkUser.setAccessToken(response.getBody().get("access_token").toString());
//                checkUser.setRefreshToken(response.getBody().get("refresh_token").toString());
//                userRepository.save(checkUser);

                Long getFollowers = followRepository.getFollowers(user.getId());
                Long getFollowing = followRepository.getFollowing(user.getId());

                user.setTotal_following(getFollowing);
                user.setTotal_followers(getFollowers);

                repoUser.save(user);


                map.put("access_token", response.getBody().get("access_token"));
                map.put("token_type", response.getBody().get("token_type"));
                map.put("refresh_token", response.getBody().get("refresh_token"));
                map.put("expires_in", response.getBody().get("expires_in"));
                map.put("scope", response.getBody().get("scope"));
                map.put("jti", response.getBody().get("jti"));
                map.put("user", user);


                return responses.Sukses(map);
            } else {
                return responses.Error(map);
            }
        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return response.Error("invalid login");
            }
            return response.Error(e);
        } catch (Exception e) {
            e.printStackTrace();

            return response.Error(e);
        }
    }

    @Override
    public Map getDetailProfile(Principal principal) {
        User idUser = getUserIdToken(principal, userDetailsService);
        Long getFollowers = followRepository.getFollowers(idUser.getId());
        Long getFollowing = followRepository.getFollowing(idUser.getId());
        try {
            idUser.setTotal_following(getFollowing);
            idUser.setTotal_followers(getFollowers);

            User obj = repoUser.save(idUser);
            return response.Sukses(obj);
        } catch (Exception e){
            return response.Error(e);
        }
    }

    @Override
    public Map sugestedPeople(Principal principal) {
        User idUser = getUserIdToken(principal, userDetailsService);
        List<Long> followingData = followRepository.getFollowingdata(idUser.getId());
        followingData.add(idUser.getId());
        String[] passion = idUser.getPassion().split("\\ |" /*<- Regex */);
        List<User> getSugestedPeople = repoUser.getSugestedPeople("%" + passion[0].toLowerCase() + "%",followingData);
        return response.Sukses(getSugestedPeople);
    }

    @Override
    public Map getDetail(Principal principal, Long idUserDetail) {
        User idUser = getUserIdToken(principal, userDetailsService);
        List<Post> listPost = null;
        List<Long> getIdPostBookmark = bookmarkRepository.getIdPostBookmark(idUser.getId());
        List<Long> getIsLiked = likeRepository.getIsLike(idUser);
        List<Long> getFollowingdata = followRepository.getFollowingdata(idUser.getId());
        User user = repoUser.getbyId(getFollowingdata,idUserDetail);

        try {
            if (user == null) {
                return response.notFound("user not found");
            }

            if (idUser.getId().equals(idUserDetail)) {
                listPost = postRepository.getListUserDetail(getIsLiked, user, getIdPostBookmark);

                Map<String, Object> map = new HashMap<>();

                map.put("detail_profile", user);
                map.put("post_user", listPost);

                return response.Sukses(map);
            }

            listPost = postRepository.getListUserDetailOther(getIsLiked, user, getIdPostBookmark);

            Map<String, Object> map = new HashMap<>();

            map.put("detail_profile", user);
            map.put("post_user", listPost);

            return response.Sukses(map);
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
        User idUser = repoUser.findOneByUsername(user.getUsername());
        if (null == idUser) {
            throw new UsernameNotFoundException("User name not found");
        }
        return idUser;
    }


}