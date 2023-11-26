package com.synrgy.commit.repository;

import com.synrgy.commit.model.Follow;
import com.synrgy.commit.model.Like;
import com.synrgy.commit.model.Post;
import com.synrgy.commit.model.oauth.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends PagingAndSortingRepository<Like, Long> {

    @Query("SELECT u FROM Like u WHERE u.id_user = ?1 and u.id_post = ?2")
    public Like findUserByIdUserAndIdPost(User id_user, Long id_post);

    @Query("SELECT COUNT(u) FROM Like u WHERE u.id_user=?1")
    public Long getLike(User id_user);

    @Query("SELECT u.id_post FROM Like u WHERE u.id_user = ?1")
    public List<Long> getIsLike(User id_user);
}
