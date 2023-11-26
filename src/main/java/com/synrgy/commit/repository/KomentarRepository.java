package com.synrgy.commit.repository;

import com.synrgy.commit.model.Follow;
import com.synrgy.commit.model.Komentar;
import com.synrgy.commit.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KomentarRepository  extends PagingAndSortingRepository<Komentar, Long> {
    @Query("select c from Komentar c WHERE c.id = :id")
    public Komentar getbyID(@Param("id") Long id);

    @Query("select c from Komentar c WHERE c.id_post = :id_post")
    public List<Komentar> getbyIDpost(Post id_post);
}
