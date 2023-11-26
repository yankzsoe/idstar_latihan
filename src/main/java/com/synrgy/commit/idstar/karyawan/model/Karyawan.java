package com.synrgy.commit.idstar.karyawan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "karyawan")
@Where(clause = "deleted_date is null")
public class Karyawan implements Serializable {
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


    @NotNull(message = "Tidak Boleh Kosong")
    @Size(min = 4, max = 100, message= "Minimal 4 karakter dan maksimal 100 karakter")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull(message = "Tidak Boleh Kosong")
    @Column(name = "status", nullable = false, length = 100)
    private String status;

    @NotNull(message = "Tidak Boleh Kosong")
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull(message = "Tidak Boleh Kosong")
    @Column(name = "dob", nullable = false)
    private Date dob;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "karyawanDetail",referencedColumnName = "id", nullable = false)
    private KaryawanDetail karyawanDetail;

//    @JsonIgnore

//    @ManyToOne()
//    @JoinColumn(name = "id_rekening", nullable = false)
//    private Rekening rekening;
//    @JsonIgnore
//    @OneToMany(mappedBy = "karyawan")
//    private Set<KaryawanTraining> karyawanTrainingSet;

}
