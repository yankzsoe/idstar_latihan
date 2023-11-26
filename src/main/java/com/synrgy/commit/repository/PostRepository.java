package com.synrgy.commit.repository;

import com.synrgy.commit.model.Post;
import com.synrgy.commit.model.oauth.Client;
import com.synrgy.commit.model.oauth.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

    @Query("select c from Post c WHERE c.id = :id")
    public Post getbyID(@Param("id") Long id);

    @Query("select c from Post c")
    public Page<Post> getAllData(Pageable pageable);

    @Query("SELECT COUNT(id_post) FROM Post")
    public int getAllPost();

    @Query("select new com.synrgy.commit.model.Post(c.created_date, c.id_post, c.post_desc, c.post_status, c.post_tags, c.total_like, c.total_komentar, case when c.id_post in (:id_post) then true else false end as isLiked, c.user, c, case when c.id_post in (:id_postBookmarked) then true else false end as bookmarked) from Post c where c.user in (:id_user_creator) and c.post_status = false order by c.created_date asc ")
    public List<Post> getListPost(List<Long> id_post, List<User> id_user_creator, List<Long> id_postBookmarked);

//    @Query(nativeQuery = true, value = "select * from post_user WHERE id_user_creator in (:id_user_creator) and lower(post_tags) like lower(:tags) and post_status = false ORDER BY created_date asc ")
//    public List<Post> getListPostTags(List<User> id_user_creator, String tags);

    @Query("select new com.synrgy.commit.model.Post(c.created_date, c.id_post, c.post_desc, c.post_status, c.post_tags, c.total_like, c.total_komentar, case when c.id_post in (:id_post) then true else false end as isLiked, c.user, c, case when c.id_post in (:id_postBookmarked) then true else false end as bookmarked) from Post c where c.user in (:id_user_creator) and lower(c.post_tags) like lower(:tags) and c.post_status = false order by c.created_date asc ")
    public List<Post> getListPostTags(List<Long> id_post, List<User> id_user_creator, String tags, List<Long> id_postBookmarked);

    @Query("select new com.synrgy.commit.model.Post(c.created_date, c.id_post, c.post_desc, c.post_status, c.post_tags, c.total_like, c.total_komentar, case when c.id_post in (:id_post) then true else false end as isLiked, c.user, c, case when c.id_post in (:id_postBookmarked) then true else false end as bookmarked) from Post c where c.id = :id")
    public Post detailPost(List<Long> id_post, Long id, List<Long> id_postBookmarked);

    @Query("select new com.synrgy.commit.model.Post(c.created_date, c.id_post, c.post_desc, c.post_status, c.post_tags, c.total_like, c.total_komentar, case when c.id_post in (:id_post) then true else false end as isLiked, c.user, c, case when c.id_post in (:id_postBookmarked) then true else false end as bookmarked) from Post c where c.user = :id_user_creator order by c.created_date asc ")
    public List<Post> getListUserDetail(List<Long> id_post, User id_user_creator, List<Long> id_postBookmarked);

    @Query("select new com.synrgy.commit.model.Post(c.created_date, c.id_post, c.post_desc, c.post_status, c.post_tags, c.total_like, c.total_komentar, case when c.id_post in (:id_post) then true else false end as isLiked, c.user, c, case when c.id_post in (:id_postBookmarked) then true else false end as bookmarked) from Post c where c.user = :id_user_creator and c.post_status = false order by c.created_date asc ")
    public List<Post> getListUserDetailOther(List<Long> id_post, User id_user_creator, List<Long> id_postBookmarked);

    @Query("select new com.synrgy.commit.model.Post(c.created_date, c.id_post, c.post_desc, c.post_status, c.post_tags, c.total_like, c.total_komentar, case when c.id_post in (:id_post) then true else false end as isLiked, c.user, c, case when c.id_post in (:id) then true else false end as bookmarked) from Post c where c.id in (:id) order by c.created_date asc ")
    public List<Post> getListPostBookmarked(List<Long> id_post, List<Long> id);

    @Query("select new com.synrgy.commit.model.Post(c.created_date, c.id_post, c.post_desc, c.post_status, c.post_tags, c.total_like, c.total_komentar, case when c.id_post in (:id_post) then true else false end as isLiked, c.user, c, case when c.id_post in (:id_postBookmarked) then true else false end as bookmarked) from Post c where c.post_status = true and c.user.status = 'Verified' order by c.created_date asc ")
    public List<Post> getListPostSimplerVerified(List<Long> id_post, List<Long> id_postBookmarked);

    @Query("select new com.synrgy.commit.model.Post(c.created_date, c.id_post, c.post_desc, c.post_status, c.post_tags, c.total_like, c.total_komentar, case when c.id_post in (:id_post) then true else false end as isLiked, c.user, c, case when c.id_post in (:id_postBookmarked) then true else false end as bookmarked) from Post c where lower(c.post_tags) like lower(:tags) and c.post_status = true and c.user.status = 'Verified' order by c.created_date asc ")
    public List<Post> getListPostSimplerTagsVerified(List<Long> id_post, String tags, List<Long> id_postBookmarked);

    @Query("select new com.synrgy.commit.model.Post(c.created_date, c.id_post, c.post_desc, c.post_status, c.post_tags, c.total_like, c.total_komentar, case when c.id_post in (:id_post) then true else false end as isLiked, c.user, c, case when c.id_post in (:id_postBookmarked) then true else false end as bookmarked) from Post c where c.post_status = true and c.user.status = 'Official' order by c.created_date asc ")
    public List<Post> getListPostSimplerOff(List<Long> id_post, List<Long> id_postBookmarked);

    @Query("select new com.synrgy.commit.model.Post(c.created_date, c.id_post, c.post_desc, c.post_status, c.post_tags, c.total_like, c.total_komentar, case when c.id_post in (:id_post) then true else false end as isLiked, c.user, c, case when c.id_post in (:id_postBookmarked) then true else false end as bookmarked) from Post c where lower(c.post_tags) like lower(:tags) and c.post_status = true and c.user.status = 'Official' order by c.created_date asc ")
    public List<Post> getListPostSimplerTagsOff(List<Long> id_post, String tags, List<Long> id_postBookmarked);
}
