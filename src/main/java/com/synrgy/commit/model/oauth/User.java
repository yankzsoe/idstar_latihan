package com.synrgy.commit.model.oauth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.synrgy.commit.model.piksi.Dosen;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@Entity
@Table(name = "oauth_user")
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100,unique=true)
    private String username;

    @Setter
    @Getter
    private String idPushNotif;

    @Column(name = "phone_number", nullable = true, length = 100)
    private String phone_number;

    @JsonIgnore
    @Column(name = "profile_pic", nullable = true)
    private String profile_pic;

    @JsonIgnore
    @Column(name = "region",length = 100, nullable = true)
    private String region;

    @JsonIgnore
    @Column(name = "status",length = 100, nullable = true)
    private String status;
@JsonIgnore
    @Column(name = "passion",length = 100, nullable = true)
    private String passion;

@JsonIgnore
    @Column(name = "bio", columnDefinition="TEXT")
    private String bio;

    @JsonIgnore
    private Long total_followers;
    @JsonIgnore
    private Long total_following;

    @Transient
    @JsonIgnore
    private boolean is_follow;

    @JsonIgnore
    private boolean is_subscribed = false;

    @Column(name = "gender", nullable = true)
    private String gender;

    @JsonIgnore
    private boolean is_verified = false;

    @JsonIgnore
    private boolean isBlocked = false;

    @Column(length = 100, nullable = true)
    private String fullname;

    @Setter
    @Getter
    @Column()
    private String tempatLahir;

    @Setter
    @Getter
    @Column()
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date tanggalLahir;

    @Setter
    @Getter
    @Column()
    private String namaIbuKandung;

//    @Setter
//    @Getter
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "dosen_id", referencedColumnName = "id")
//    private Dosen dosen;


    @JsonIgnore
    private String password;

    @JsonIgnore
    private String verifyToken;

    @JsonIgnore
    private Date expiredVerifyToken;

    @JsonIgnore
    @Column(length = 100, nullable = true)
    private String otp;

    @JsonIgnore
    private Date expiredSubs;

    @JsonIgnore
    private Date otpExpiredDate;

    @JsonIgnore
    private Date resendOtpDate;

    @JsonIgnore
    private int limitotp = 0;

    @JsonIgnore
    private boolean is_change = false;

    @JsonIgnore
    private boolean enabled = true;

    @JsonIgnore
    @Column(name = "not_expired")
    private boolean accountNonExpired = true;

    @JsonIgnore
    @Column(name = "not_locked")
    private boolean accountNonLocked = true;

    @JsonIgnore
    @Column(name = "credential_not_expired")
    private boolean credentialsNonExpired = true;


    @JsonIgnore
    @ManyToMany(targetEntity = Role.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "oauth_user_role",
            joinColumns = {
                    @JoinColumn(name = "user_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id")
            }
    )
    private List<Role> roles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotal_followers() {
        return total_followers;
    }

    public void setTotal_followers(Long total_followers) {
        this.total_followers = total_followers;
    }

    public Long getTotal_following() {
        return total_following;
    }

    public void setTotal_following(Long total_following) {
        this.total_following = total_following;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

//      return  this.roles
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setIs_follow(boolean is_follow) {
        this.is_follow = is_follow;
    }

    public boolean isIs_follow() {
        return is_follow;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setPassion(String passion) {
        this.passion = passion;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setIs_change(boolean is_change) {
        this.is_change = is_change;
    }


    @Override
    public String getUsername() {
        return username;
    }

    public int getLimitotp() {
        return limitotp;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public String getRegion() {
        return region;
    }

    public String getPassion() {
        return passion;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getStatus() {
        return status;
    }

    public String getGender() {
        return gender;
    }

    public boolean getIs_change() {
        return is_change;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }

    public Date getExpiredVerifyToken() {
        return expiredVerifyToken;
    }

    public Date getExpiredSubs() {
        return expiredSubs;
    }

    public void setExpiredSubs(Date expiredSubs) {
        this.expiredSubs = expiredSubs;
    }

    public void setExpiredVerifyToken(Date expiredVerifyToken) {
        this.expiredVerifyToken = expiredVerifyToken;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Date getOtpExpiredDate() {
        return otpExpiredDate;
    }

    public Date getResendOtpDate() {
        return resendOtpDate;
    }

    public void setOtpExpiredDate(Date otpExpiredDate) {
        this.otpExpiredDate = otpExpiredDate;
    }

    public void setResendOtpDate(Date resendOtpDate) {
        this.resendOtpDate = resendOtpDate;
    }

    public void setLimitotp(int limitotp) {
        this.limitotp = limitotp;
    }

    public User() {
    }

    public User(Long id,String fullname, String profile_pic, String passion ,boolean is_follow, String username, String phone_number, String region, String status, String bio, Long total_following, Long total_followers, String gender) {
        this.id = id;
        this.fullname = fullname;
        this.profile_pic = profile_pic;
        this.passion = passion;
        this.is_follow = is_follow;
        this.username = username;
        this.phone_number = phone_number;
        this.region = region;
        this.status = status;
        this.bio = bio;
        this.total_following = total_following;
        this.total_followers = total_followers;
        this.gender = gender;
    }

}
