package com.synrgy.commit.idstar.karyawan.model;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Data
@Entity
@Table(name = "rekening")
@Where(clause = "deleted_date is null")
public class Rekening implements Serializable {
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
    @Column(name = "jenis", nullable = false, length = 100)
    private String jenis;
    @Column(name = "nama", nullable = false, length = 100)
    private String nama;
    @Column(name = "norek", nullable = false, length = 100)
    private String norek;
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "karyawan",referencedColumnName = "id", nullable = false)
//    private Karyawan karyawan;

    @ManyToOne()
    @JoinColumn(name = "id_karyawan", nullable = false)
    private Karyawan karyawan;

}
