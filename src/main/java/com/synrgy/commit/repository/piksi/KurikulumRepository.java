package com.synrgy.commit.repository.piksi;

import com.synrgy.commit.model.piksi.Dosen;
import com.synrgy.commit.model.piksi.Kurikulum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface KurikulumRepository extends PagingAndSortingRepository<Kurikulum, Long> {
    @Query("select c from Kurikulum c WHERE c.id = :id")
    public Kurikulum getbyID(@Param("id") Long id);
    @Query("select c from Kurikulum c")// nama class
    public Page<Kurikulum> getAllData(Pageable pageable);

//    public Dosen findFirstByNidn(String nidn);
//
//    public Dosen findFirstByNik(String nik);
//
//    @Query("select c from Dosen c WHERE LOWER(c.nik) = LOWER(:nik) and c.id not in(:id)")
//    public Dosen findFirstByNikNotId(@Param("nik") String nik, @Param("id") Long id);
//
//    @Query("select c from Dosen c WHERE LOWER(c.nidn) = LOWER(:nidn) and c.id not in(:id)")
//    public Dosen findFirstByNidnNotId(@Param("nidn") String nik, @Param("id") Long id);
//
    @Query("FROM Kurikulum u WHERE LOWER(u.nama) like LOWER(:nama)")
    public Page<Kurikulum> findByNamaLike(String nama, Pageable pageable);

//    @Query("FROM Dosen u WHERE LOWER(u.nidn) like LOWER(:nidn)")
//    public Page<Dosen> findByNidnLike(String nidn, Pageable pageable);
}
