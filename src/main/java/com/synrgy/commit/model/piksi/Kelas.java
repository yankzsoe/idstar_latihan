package com.synrgy.commit.model.piksi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.synrgy.commit.model.AbstractDate;
import com.synrgy.commit.model.oauth.User;
import lombok.Data;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "kelas")
@Where(clause = "deleted_date is null")
public class Kelas extends AbstractDate implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @JoinColumn(name = "created_user")
    @ManyToOne
    private User createdUser;

    @JoinColumn(name = "program_studi_id")
    @ManyToOne
    private Lookup programStudi;


    @JoinColumn(name = "semester_id")
    @ManyToOne
    private Lookup semester; // genereta otomatsi 2015 sd 2025 : butuh genearate FE

    @JoinColumn(name = "mata_kuliah_id")
    @ManyToOne
    private MataKuliah mataKuliah;

    String nama;

    @JoinColumn(name = "lingkup_id")
    @ManyToOne
    private Lookup lingkup;//lingkup_kelas : intenal exsternal campuran

    @JoinColumn(name = "mode_id")
    @ManyToOne
    private Lookup mode;//mode_kelas: online offline campuran

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tglMulaiEfektif;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tglAkhirEfektif;


}
