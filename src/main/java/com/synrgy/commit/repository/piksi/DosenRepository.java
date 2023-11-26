package com.synrgy.commit.repository.piksi;

import com.synrgy.commit.model.piksi.Dosen;
import com.synrgy.commit.model.piksi.Lookup;
import com.synrgy.commit.model.piksi.Sekolah;
import com.synrgy.commit.model.piksi.SosialisasiSekolah;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface DosenRepository extends PagingAndSortingRepository<Dosen, Long> {
    @Query("select c from Dosen c WHERE c.id = :id")
    public Dosen getbyID(@Param("id") Long id);

//    @Query("select c from Lookup c WHERE c.nama=:nama and c.type=:type")
//    public Lookup findByNamaAndType(@Param("nama") String nama,@Param("type") String type);

//    @Query("select c from Lookup c WHERE   c.type=:type")
//    public Lookup findFirstByType(String type);

//    @Query("select c from ReportAbsensi c WHERE c.type = :type and to_char(c.date,'YYYY-MM-DD') = :date and c.id_user =:user ")
//    public ReportAbsensi chekDuplikatChekin(@Param("type") String type,
//                                            @Param("date") String date,
//                                            @Param("user") User user);
//
    @Query("select c from Dosen c")// nama class
    public Page<Dosen> getAllData(Pageable pageable);

    public Dosen findFirstByNidn(String nidn);

    public Dosen findFirstByNik(String nik);

    @Query("select c from Dosen c WHERE LOWER(c.nik) = LOWER(:nik) and c.id not in(:id)")
    public Dosen findFirstByNikNotId(@Param("nik") String nik, @Param("id") Long id);

    @Query("select c from Dosen c WHERE LOWER(c.nidn) = LOWER(:nidn) and c.id not in(:id)")
    public Dosen findFirstByNidnNotId(@Param("nidn") String nik, @Param("id") Long id);

    @Query("FROM Dosen u WHERE LOWER(u.nik) like LOWER(:nik)")
    public Page<Dosen> findByNikLike(String nik, Pageable pageable);

    @Query("FROM Dosen u WHERE LOWER(u.nidn) like LOWER(:nidn)")
    public Page<Dosen> findByNidnLike(String nidn, Pageable pageable);
//
//    @Query("select c from ReportAbsensi c where c.id_user=:user")
//    public Page<ReportAbsensi> getAllDataByUser(User user,Pageable pageable);
//
//    @Query("select c from ReportAbsensi c")// nama class
//    public List<ReportAbsensi> getAllDataList();
//
//    @Query("select c from ReportAbsensi c where c.date between :dateFrom and :dateTo and c.id_user = :userId")// nama class
//    public List<ReportAbsensi> getAllDataListRangeDate(@Param("dateFrom") Date dateFrom,
//                                                       @Param("dateTo") Date dateTo,
//                                                       @Param("userId") User userId);
}
