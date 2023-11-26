package com.synrgy.commit.repository.oauth;


import com.synrgy.commit.model.oauth.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>, CrudRepository<User, Long> {
    @Query("FROM User u WHERE LOWER(u.username) = LOWER(?1)")
    User findOneByUsername(String username);

    @Query("FROM User u WHERE u.otp = ?1")
    User findOneByOTP(String otp);

    @Query("FROM User u WHERE LOWER(u.username) = LOWER(:username)")
    User checkExistingEmail(String username);

    @Query("From User c WHERE LOWER (c.username) = LOWER (:username) and LOWER (c.otp) = LOWER(:otp)")
    User findbyEmailandOtp(String username, String otp);

    @Query("From User c WHERE LOWER (c.username) = LOWER (:username) and LOWER (c.status) = LOWER(:status)")
    User findEmailUser(String username, String status);

//    @Query("FROM User c WHERE LOWER(c.fullname) like LOWER(?1)")
//    public Page<User> findByFullnameLike(String fullname, Pageable pageable);

    @Query("FROM User c WHERE LOWER(c.fullname) like :fullname")
    public Page<User> findByFullnameLike(String fullname, Pageable pageable);

//    public Page<User> findByFullnameLike(String fullname, Pageable pageable);

    @Query("select c from User c")
    public Page<User> getAllData(Pageable pageable);

    @Query("select c from User c WHERE c.id = :id")
    public User getbyID(@Param("id") Long id);

    @Query(nativeQuery = true, value = "select * from oauth_user WHERE LOWER(passion) like :passion and id not in (:id) ORDER BY random() LIMIT 3")
    public List<User> getSugestedPeople(String passion,List<Long> id);

    @Query(nativeQuery = true, value = "select * from oauth_user WHERE id in (:id)")
    public List<User> getListId(List<Long> id);


    @Query("select new com.synrgy.commit.model.oauth.User(c.id, c.fullname, c.profile_pic, c.passion ,case when id in (:id) then true else false end as is_follow, c.username, c.phone_number, c.region, c.status, c.bio, c.total_following, c.total_followers, c.gender) from User c where c.id not in (:iduser)")
    public List<User> getAllUser(List<Long> id, Long iduser);

    @Query("select new com.synrgy.commit.model.oauth.User(c.id, c.fullname, c.profile_pic, c.passion ,case when id in (:id) then true else false end as is_follow, c.username, c.phone_number, c.region, c.status, c.bio, c.total_following, c.total_followers, c.gender) from User c where lower(c.fullname) like lower(:nama) and c.id not in (:iduser)")
    public List<User> getUserByNama(String nama,List<Long> id, Long iduser);

    @Query("select new com.synrgy.commit.model.oauth.User(c.id, c.fullname, c.profile_pic, c.passion ,case when id in (:id_following) then true else false end as is_follow, c.username, c.phone_number, c.region, c.status, c.bio, c.total_following, c.total_followers, c.gender) from User c where c.id in (:id)")
    public List<User> getListFollowing(List<Long> id_following,List<Long> id);

    @Query("select new com.synrgy.commit.model.oauth.User(c.id, c.fullname, c.profile_pic, c.passion ,case when id in (:id_following) then true else false end as is_follow, c.username, c.phone_number, c.region, c.status, c.bio, c.total_following, c.total_followers, c.gender) from User c where c.id = (:id)")
    public User getbyId(List<Long> id_following,Long id);

//    @Query("UPDATE User u SET u.password = ?2 WHERE u.id = ?1")
//    @Modifying
//    @Transactional
//    void updatePassword(Long id, String password);



}

