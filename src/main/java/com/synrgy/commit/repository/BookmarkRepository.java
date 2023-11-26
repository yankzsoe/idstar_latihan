package com.synrgy.commit.repository;

import com.synrgy.commit.model.Bookmark;
import com.synrgy.commit.model.Follow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BookmarkRepository extends PagingAndSortingRepository<Bookmark, Long> {
    @Query("SELECT u FROM Bookmark u WHERE u.id_user = ?1 and u.id_post = ?2")
    public Bookmark findBookmark(Long id_user, Long id_post);

    @Query("SELECT u.id_post FROM Bookmark u WHERE u.id_user=?1")
    public List<Long> getIdPostBookmark(Long id_user);
}
