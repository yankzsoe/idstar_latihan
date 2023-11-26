package com.synrgy.commit.model.piksi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.synrgy.commit.model.AbstractDate;
import com.synrgy.commit.model.oauth.User;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "sosialisasi_sekolah")
@Where(clause = "deleted_date is null")
public class SosialisasiSekolah extends AbstractDate implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @JoinColumn(name = "created_user")
    @ManyToOne
    private User createdUser;

    @Column(name = "nama")
    private String nama;

    @Column(name = "hp")
    private String hp;

    @Column(name = "email")
    private String email;

    @JoinColumn(name = "nama_sekolah")
    @ManyToOne
    private Sekolah sekolah;

    @Column(name = "jurusan")
    private String jurusan;

    @JoinColumn(name = "jenis_sekolah")
    @ManyToOne
    private JenisSekolah jenisSekolah;

    @JoinColumn(name = "kecamatan_id")
    @ManyToOne
    private Lookup kecamatan;

    @Column(name = "status")
    private String status;// terkirim

}
