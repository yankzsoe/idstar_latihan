package com.synrgy.commit.repository.piksi;

import com.synrgy.commit.model.piksi.Dosen;
import com.synrgy.commit.model.piksi.MataKuliah;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface MatkulRepository extends PagingAndSortingRepository<MataKuliah, Long> {
    @Query("select c from MataKuliah c WHERE c.id = :id")
    public MataKuliah getbyID(@Param("id") Long id);

    @Query("select c from MataKuliah c")// nama class
    public Page<MataKuliah> getAllData(Pageable pageable);

    public MataKuliah findFirstByKode(String kode);

    @Query("select c from MataKuliah c WHERE LOWER(c.kode) = LOWER(:kode) and c.id not in(:id)")
    public MataKuliah findFirstByKodeNotId(@Param("kode") String nik, @Param("id") Long id);
//
//    @Query("select c from Dosen c WHERE LOWER(c.nidn) = LOWER(:nidn) and c.id not in(:id)")
//    public Dosen findFirstByNidnNotId(@Param("nidn") String nik, @Param("id") Long id);
//
    @Query("FROM MataKuliah u WHERE LOWER(u.kode) like LOWER(:kode)")
    public Page<MataKuliah> findByKodeLike(String kode, Pageable pageable);

    @Query("FROM MataKuliah u WHERE LOWER(u.nama) like LOWER(:nama)")
    public Page<MataKuliah> findByNamaLike(String nama, Pageable pageable);
}
