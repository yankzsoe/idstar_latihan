package com.synrgy.commit.repository;

import com.synrgy.commit.model.Post;
import com.synrgy.commit.model.ReportComment;
import com.synrgy.commit.model.SimplerPayment;
import com.synrgy.commit.model.oauth.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SimplerRepository extends PagingAndSortingRepository<SimplerPayment, Long> {
    @Query("select c from SimplerPayment c WHERE c.status = ?1")
    public List<SimplerPayment> getOnprogressPayment(String status);

    @Query("SELECT COUNT(u) FROM SimplerPayment u WHERE u.status = ?1")
    public Long getTotalSimpler(String status);

    @Query("select c from SimplerPayment c WHERE c.id = :id")
    public SimplerPayment getbyID(@Param("id") Long id);

    @Query("select c from SimplerPayment c WHERE c.id_user = ?1")
    public List<SimplerPayment> getHistory(User user);

    @Query("select c from SimplerPayment c WHERE c.id_user = ?1 and c.status =?2")
    public SimplerPayment getUser(User user, String status);

    @Query(nativeQuery = true, value ="select * from simpler_payment WHERE id_user = (:user) order by created_date desc LIMIT 1")
    public List<SimplerPayment> getCheck(User user);
}
