package com.synrgy.commit.repository.piksi;

import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.model.piksi.Lookup;
import com.synrgy.commit.model.piksi.Penugasan;
import com.synrgy.commit.model.piksi.Sekolah;
import com.synrgy.commit.model.piksi.SosialisasiSekolah;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SosialisasiSekolahRepo extends PagingAndSortingRepository<SosialisasiSekolah, Long> {
    @Query("select c from SosialisasiSekolah c WHERE c.id = :id")
    public SosialisasiSekolah getbyID(@Param("id") Long id);

    @Query("select c from SosialisasiSekolah c WHERE c.nama = :nama")
    public SosialisasiSekolah getbyNama(@Param("nama") String  nama);
//    @Query("select c from Penugasan c WHERE c.deskripsi = :desc")
//    public Penugasan findOneByDesc(@Param("desc") String desc);
//
    @Query("select c from SosialisasiSekolah c")// nama class
    public Page<SosialisasiSekolah> getAllData(Pageable pageable);

    @Query("select c from SosialisasiSekolah c where c.createdUser =:createdUser")// nama class
    public Page<SosialisasiSekolah> getAllDataByUser(User createdUser,Pageable pageable);


    @Query("FROM SosialisasiSekolah u WHERE LOWER(u.nama) like LOWER(:nama)")
    public Page<SosialisasiSekolah> findByNamaLike(String nama, Pageable pageable);

    @Query("FROM SosialisasiSekolah u WHERE LOWER(u.status) = LOWER('terkirim')")
    public Page<SosialisasiSekolah> showSendEmail(Pageable pageable);

    @Query("FROM SosialisasiSekolah u WHERE LOWER(u.status) = LOWER('gagal')")
    public Page<SosialisasiSekolah> showNotSendEmail(Pageable pageable);

//    @Query("FROM SosialisasiSekolah u WHERE  c.status is null")
    public Page<SosialisasiSekolah> findByStatusIsNull(Pageable pageable);

    @Query("FROM SosialisasiSekolah u WHERE LOWER(u.sekolah.nama) like LOWER(:namaSekolah)")
    public Page<SosialisasiSekolah> findByNamaSekolahLike(String namaSekolah, Pageable pageable);

    @Query("FROM SosialisasiSekolah u WHERE LOWER(u.email) like LOWER(:email)")
    public Page<SosialisasiSekolah> findByEmailLike(String email, Pageable pageable);

    @Query("FROM SosialisasiSekolah u WHERE LOWER(u.nama) = LOWER(:nama)")
    public List<SosialisasiSekolah> listNama(String nama);

    @Query("FROM SosialisasiSekolah u WHERE u.sekolah = :sekolah")
    public List<SosialisasiSekolah> listSekolah(Sekolah sekolah);

    @Query("FROM SosialisasiSekolah u WHERE u.kecamatan = :kecamatan")
    public List<SosialisasiSekolah> listWilayah(Lookup kecamatan);

    @Query("FROM SosialisasiSekolah u ")
    public List<SosialisasiSekolah> listAll();

//    @Query("select c from Penugasan c where c.deskripsi= :deskripsi")
//    public Page<Penugasan> findByNama(String deskripsi, Pageable pageable);
//
//    public Page<Penugasan> findByDeskripsiLike(String deskripsi, Pageable pageable);

    @Query("select c from SosialisasiSekolah c where  c.status is null")// nama class
    public Page<SosialisasiSekolah> getAllDataNotInTerkirim(Pageable pageable);

}
