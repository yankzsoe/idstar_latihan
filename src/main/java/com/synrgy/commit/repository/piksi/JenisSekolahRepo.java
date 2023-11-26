package com.synrgy.commit.repository.piksi;

import com.synrgy.commit.model.piksi.JenisSekolah;
import com.synrgy.commit.model.piksi.Penugasan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface JenisSekolahRepo extends PagingAndSortingRepository<JenisSekolah, Long> {
    @Query("select c from JenisSekolah c WHERE c.id = :id")
    public JenisSekolah getbyID(@Param("id") Long id);
    @Query("select c from JenisSekolah c WHERE LOWER(c.nama) = LOWER(:nama)")
    public JenisSekolah findOneByNama(@Param("nama") String nama);
//
    @Query("select c from JenisSekolah c")// nama class
    public Page<JenisSekolah> getAllData(Pageable pageable);
//    @Query("select c from Penugasan c where c.deskripsi= :deskripsi")
//    public Page<Penugasan> findByNama(String deskripsi, Pageable pageable);
//
//    public Page<Penugasan> findByDeskripsiLike(String deskripsi, Pageable pageable);

}
