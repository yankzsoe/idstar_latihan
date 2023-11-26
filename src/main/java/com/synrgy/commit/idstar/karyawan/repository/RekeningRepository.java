package com.synrgy.commit.idstar.karyawan.repository;

import com.synrgy.commit.idstar.karyawan.model.Rekening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RekeningRepository extends CrudRepository<Rekening,Long> {

    @Query("select c from Rekening c WHERE c.id = :rekeningId")
    public Rekening getbyID(@Param("rekeningId") Long idRekening);

    @Query("select c from Rekening c")// nama class
    public Page<Rekening> getAllData(Pageable pageable);
}
