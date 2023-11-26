package com.synrgy.commit.repository;

import com.synrgy.commit.model.*;
import com.synrgy.commit.model.oauth.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends PagingAndSortingRepository<ReportPost, Long> {

    @Query("select c from ReportPost c")
    public List<ReportPost> getAllReportPost();

    @Query("SELECT COUNT(u) FROM ReportPost u")
    public Long getLike();

    @Query("select c from ReportPost c WHERE c.id_report = ?1")
    public ReportPost getbyID(@Param("id") Long id);

    @Query("select c from ReportPost c WHERE c.id_post = ?1")
    public List<ReportPost> getPost(Post post);
}
