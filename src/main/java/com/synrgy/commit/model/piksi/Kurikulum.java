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
@Table(name = "kurikulim")
@Where(clause = "deleted_date is null")
public class Kurikulum extends AbstractDate implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @JoinColumn(name = "created_user")
    @ManyToOne
    private User createdUser;

    private String nama;


    private Integer bobotMatkulPilihan;

    private Integer bobotMatkulWajib;

    private Integer jumlahSks; //diisi oleh be : sks wajib + sks Pilihan

    @JoinColumn(name = "program_studi_id")
    @ManyToOne
    private Lookup programStudi;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tglMulaiEfektif;

}
