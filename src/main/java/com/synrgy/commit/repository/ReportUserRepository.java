package com.synrgy.commit.repository;

import com.synrgy.commit.model.Post;
import com.synrgy.commit.model.ReportComment;
import com.synrgy.commit.model.ReportPost;
import com.synrgy.commit.model.ReportUser;
import com.synrgy.commit.model.oauth.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportUserRepository extends PagingAndSortingRepository<ReportUser, Long> {

    @Query("select c from ReportUser c")
    public List<ReportUser> getAllReportUser();

    @Query("SELECT COUNT(u) FROM ReportUser u")
    public Long getLike();

    @Query("select c from ReportUser c WHERE c.id_report_user = ?1")
    public ReportUser getbyID(@Param("id") Long id);

    @Query("select c from ReportUser c WHERE c.otherUser = ?1")
    public List<ReportUser> getListUser(User user);


}
