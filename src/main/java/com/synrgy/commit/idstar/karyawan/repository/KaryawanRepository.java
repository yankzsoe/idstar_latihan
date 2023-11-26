package com.synrgy.commit.idstar.karyawan.repository;

import com.synrgy.commit.idstar.karyawan.model.Karyawan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface KaryawanRepository extends CrudRepository<Karyawan,Long> {

    @Query("select c from Karyawan c WHERE c.id = :karyawanId")
    public Karyawan getbyID(@Param("karyawanId") Long idKaryawan);

    @Query("select c from Karyawan c")// nama class
    public Page<Karyawan> getAllData(Pageable pageable);

}
