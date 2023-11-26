package com.synrgy.commit.repository;

import com.synrgy.commit.model.Follow;
import com.synrgy.commit.model.oauth.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends PagingAndSortingRepository<Follow, Long> {

    @Query("SELECT u FROM Follow u WHERE u.id_user = ?1 and u.id_user_following = ?2")
    public Follow findUserByIdUserAndIdFollowing(Long id_user, Long id_user_following);

    @Query("select c from Follow c WHERE c.id = :id")
    public Follow getbyID(@Param("id") Long id);

    @Query("SELECT COUNT(u) FROM Follow u WHERE u.id_user=?1")
    public Long getFollowing(Long id_user);

    @Query("SELECT COUNT(u) FROM Follow u WHERE u.id_user_following=?1")
    public Long getFollowers(Long id_user_following);

    @Query("SELECT u.id_user_following FROM Follow u WHERE u.id_user=?1")
    public List<Long> getFollowingdata(Long id_user);

    @Query("SELECT u.id_user FROM Follow u WHERE u.id_user_following=?1")
    public List<Long> getFollowersdata(Long id_user);


}
