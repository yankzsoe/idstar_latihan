package com.synrgy.commit.repository.piksi;

import com.synrgy.commit.model.piksi.Kurikulum;
import com.synrgy.commit.model.piksi.KurikulumMataKuliah;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface KurikulumMatkulRepository extends PagingAndSortingRepository<KurikulumMataKuliah, Long> {
    @Query("select c from KurikulumMataKuliah c WHERE c.id = :id")
    public KurikulumMataKuliah getbyID(@Param("id") Long id);

    @Query("select c from KurikulumMataKuliah c where LOWER(c.matkul.kode) like LOWER(:kode)")
    public Page<KurikulumMataKuliah> getAllDataByMatkulKode(String  kode, Pageable pageable);

    @Query("select c from KurikulumMataKuliah c where LOWER(c.matkul.nama) like LOWER(:nama)")
    public Page<KurikulumMataKuliah> getAllDataByMatkulNama(String  nama, Pageable pageable);

    @Query("select c from KurikulumMataKuliah c where LOWER(c.semester) like LOWER(:semester)")
    public Page<KurikulumMataKuliah> getAllDataBySemester(String  semester, Pageable pageable);

    @Query("select c from KurikulumMataKuliah c")
    public Page<KurikulumMataKuliah> getAllData(  Pageable pageable);

}
