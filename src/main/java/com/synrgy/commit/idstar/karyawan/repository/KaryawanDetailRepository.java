package com.synrgy.commit.idstar.karyawan.repository;

import com.synrgy.commit.idstar.karyawan.model.Karyawan;
import com.synrgy.commit.idstar.karyawan.model.KaryawanDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface KaryawanDetailRepository extends CrudRepository<KaryawanDetail,Long> {
    @Query("select c from KaryawanDetail c WHERE c.id = :karyawanDetailId")
    public Karyawan getbyID(@Param("karyawanDetailId") Long idKaryawanDetail);
}
