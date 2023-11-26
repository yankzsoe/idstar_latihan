package com.synrgy.commit.idstar.karyawan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.Date;
@Data
@Entity
@Table(name = "karyawan_detail")
@Where(clause = "deleted_date is null")
public class KaryawanDetail implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_date")
    private Date created_date;
    @Column(name = "updated_date")
    private Date updated_date;
    @Column(name = "deleted_date")
    private Date deleted_date;
    @Column(name = "nik", nullable = false, length = 100)
    private String nik;
    @Column(name = "npwp", nullable = false, length = 100)
    private String npwp;

}
