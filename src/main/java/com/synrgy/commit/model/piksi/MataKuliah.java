package com.synrgy.commit.model.piksi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.synrgy.commit.model.AbstractDate;
import com.synrgy.commit.model.oauth.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "mata_kuliah")
@Where(clause = "deleted_date is null")
public class MataKuliah extends AbstractDate implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @JoinColumn(name = "created_user")
    @ManyToOne
    private User createdUser;

    private String kode;

    @JoinColumn(name = "program_studi_id")
    @ManyToOne
    private Lookup programStudi;

    private Integer bobotMatkul; //( sks Tatap Muka + sks Praktikum + sks Praktek Lapangan + sks Simulasi )

    private Integer bobotPratikum;

    private Integer bobotSimulasi;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tglMulaiEfektif;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tglAkhirEfektif;

    private String nama;

    @JoinColumn(name = "jenis_matkul_id")
    @ManyToOne
    private Lookup jenisMatkul;

    private Integer bobotTatapMuka;

    private Integer bobotPraktekLapangan;

    private String metode;
}
