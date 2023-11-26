package com.synrgy.commit.repository;

import com.synrgy.commit.model.*;
import com.synrgy.commit.model.oauth.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportCommmentRepository extends PagingAndSortingRepository<ReportComment, Long> {
    @Query("select c from ReportComment c")
    public List<ReportComment> getAllReportComment();

    @Query("SELECT COUNT(u) FROM ReportComment u")
    public Long gettotalReportComment();

    @Query("select c from ReportComment c WHERE c.id_report_comment = ?1")
    public ReportComment getbyID(@Param("id") Long id);

    @Query("select c from ReportComment c WHERE c.id_komentar = ?1")
    public List<ReportComment> getComment(Komentar komentar);
}
