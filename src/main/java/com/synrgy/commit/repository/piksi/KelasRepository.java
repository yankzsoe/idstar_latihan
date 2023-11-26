package com.synrgy.commit.repository.piksi;

import com.synrgy.commit.model.piksi.Dosen;
import com.synrgy.commit.model.piksi.Kelas;
import com.synrgy.commit.model.piksi.Kurikulum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface KelasRepository extends PagingAndSortingRepository<Kelas, Long> {
    @Query("select c from Kelas c WHERE c.id = :id")
    public Kelas getbyID(@Param("id") Long id);

    @Query("select c from Kelas c")// nama class
    public Page<Kelas> getAllData(Pageable pageable);

    @Query("FROM Kelas u WHERE LOWER(u.nama) like LOWER(:nama)")
    public Page<Kelas> findByNamaLike(String nama, Pageable pageable);
}
