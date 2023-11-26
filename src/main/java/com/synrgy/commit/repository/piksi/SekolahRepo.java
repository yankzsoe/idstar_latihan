package com.synrgy.commit.repository.piksi;

import com.synrgy.commit.model.piksi.Lookup;
import com.synrgy.commit.model.piksi.Sekolah;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface SekolahRepo extends PagingAndSortingRepository<Sekolah, Long> {
    @Query("select c from Sekolah c WHERE c.id = :id")
    public Sekolah getbyID(@Param("id") Long id);
    @Query("select c from Sekolah c WHERE LOWER(c.nama) = LOWER(:nama)")
    public Sekolah findLikeByNama(@Param("nama") String nama);


    @Query("select c from Sekolah c WHERE LOWER(c.nama) = LOWER(:nama) and c.id not in(:id)")
    public Sekolah findLikeByNamaAndNotID(@Param("nama") String nama,@Param("id") Long id);


//
    @Query("select c from Sekolah c")// nama class
    public Page<Sekolah> getAllData(Pageable pageable);

    @Query("select c from Sekolah c where LOWER(c.nama) like LOWER(:nama)")
    public Page<Sekolah> findLikeByNama(String nama, Pageable pageable);
//
//    public Page<Penugasan> findByDeskripsiLike(String deskripsi, Pageable pageable);

//    @Query(value = "select c from sekolah c where id=:id", nativeQuery = true)
//    public Sekolah get(@Param("id") Long id);

}
