package com.synrgy.commit.repository.piksi;

import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.model.piksi.Penugasan;
import com.synrgy.commit.model.piksi.ReportAbsensi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface PenugasanRepo extends PagingAndSortingRepository<Penugasan, Long> {
    @Query("select c from Penugasan c WHERE c.id = :id")
    public Penugasan getbyID(@Param("id") Long id);
    @Query("select c from Penugasan c WHERE c.deskripsi = :desc")
    public Penugasan findOneByDesc(@Param("desc") String desc);

    @Query("select c from Penugasan c")// nama class
    public Page<Penugasan> getAllData(Pageable pageable);
    @Query("select c from Penugasan c where c.deskripsi= :deskripsi")
    public Page<Penugasan> findByNama(String deskripsi, Pageable pageable);

    public Page<Penugasan> findByDeskripsiLike(String deskripsi, Pageable pageable);

}
