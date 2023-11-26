package com.synrgy.commit.repository.piksi;

import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.model.piksi.Penugasan;
import com.synrgy.commit.model.piksi.ReportAbsensi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface AbsensiRepository extends PagingAndSortingRepository<ReportAbsensi, Long> {
    @Query("select c from ReportAbsensi c WHERE c.id = :id")
    public ReportAbsensi getbyID(@Param("id") Long id);

    @Query("select c from ReportAbsensi c WHERE c.type = :type and to_char(c.date,'YYYY-MM-DD') = :date and c.id_user =:user ")
    public ReportAbsensi chekDuplikatChekin(@Param("type") String type,
                                            @Param("date") String date,
                                            @Param("user") User user);

    @Query("select c from ReportAbsensi c")// nama class
    public Page<ReportAbsensi> getAllData(Pageable pageable);

    @Query("select c from ReportAbsensi c where c.id_user=:user")
    public Page<ReportAbsensi> getAllDataByUser(User user,Pageable pageable);

    @Query("select c from ReportAbsensi c")// nama class
    public List<ReportAbsensi> getAllDataList();

    @Query("select c from ReportAbsensi c where c.date between :dateFrom and :dateTo and c.id_user = :userId")// nama class
    public List<ReportAbsensi> getAllDataListRangeDate(@Param("dateFrom") Date dateFrom,
                                                       @Param("dateTo") Date dateTo,
                                                       @Param("userId") User userId);
}
