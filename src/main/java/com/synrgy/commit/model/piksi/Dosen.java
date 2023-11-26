package com.synrgy.commit.model.piksi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.synrgy.commit.model.AbstractDate;
import com.synrgy.commit.model.oauth.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "dosen")
@Where(clause = "deleted_date is null")
public class Dosen extends AbstractDate implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @JoinColumn(name = "created_user")
    @ManyToOne
    private User createdUser;


    private String nidn;

    //1 kependudukan
    private String nik;

    private String photo;
    @Transient
    private String photoUrl;

    @JoinColumn(name = "agama_id")
    @ManyToOne
    private Lookup agama;

    @JoinColumn(name = "negara_id")
    @ManyToOne
    private Lookup negara;

    //2 alamat dan kontak
//    private String email; sudah ada di username
    private String alamat;
    private String rt;
    private String rw;
    private String dusun;
    private String kodePos;
    private String telpRumah;
//    private String hp;

    @JoinColumn(name = "provinsi_id")
    @ManyToOne
    private Lookup provinsi;

    @JoinColumn(name = "kabupaten_id")
    @ManyToOne
    private Lookup kabupaten;

    @JoinColumn(name = "kecamatan_id")
    @ManyToOne
    private Lookup kecamatan;

    @JoinColumn(name = "desa_id")
    @ManyToOne
    private Lookup desa;

    //lain2
    private String npwp;
    private String namaNpwp;
    private String sintaId;

    private String status="aktif";

    @Setter
    @Getter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Transient
    private  String password;


}
