package com.synrgy.commit.repository.piksi;

import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.model.piksi.Lookup;
import com.synrgy.commit.model.piksi.ReportAbsensi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface LookupRepository extends PagingAndSortingRepository<Lookup, Long> {
    @Query("select c from Lookup c WHERE c.id = :id")
    public Lookup getbyID(@Param("id") Long id);

    @Query("select c from Lookup c WHERE LOWER(c.nama)=LOWER(:nama) and c.type=:type")
    public Lookup findByNamaAndType(@Param("nama") String nama,@Param("type") String type);

    @Query("select c from Lookup c WHERE  c.type=:type")
    public Lookup findByType(@Param("type") String type);

    public Lookup findFirstByType(String type);

    @Query("select c from Lookup c where  LOWER(c.nama) like LOWER(:nama) and LOWER(c.type) = LOWER(:type)")
    public Page<Lookup> getAllData(String nama, String type , Pageable pageable);

    @Query("select c from Lookup c where LOWER(c.type) = LOWER(:type)")
    public Page<Lookup> getAllDataBytype(  String type , Pageable pageable);

    @Query("select c from Lookup c")
    public Page<Lookup> getAllData(  Pageable pageable);

    @Query("select distinct(c.type) from Lookup c where c.type is not null")
    public Page<String> getAllDataDistinctType(Pageable pageable);

    @Query("select distinct(c.type) from Lookup c where LOWER(c.type) = LOWER(:type)")
    public Page<String> getAllDataDistinctType( String type ,Pageable pageable);

    @Query("select c from Lookup c where LOWER(c.nama) like LOWER(:nama) and c.idParent=:idParent")
    public Page<Lookup> getAllChildByParentLikeNama(@Param("nama")  String nama,@Param("idParent") Long idParent , Pageable pageable);

    @Query("select c from Lookup c where c.idParent=:idParent")
    public Page<Lookup> getAllChildByParent( Long idParent , Pageable pageable);
}
