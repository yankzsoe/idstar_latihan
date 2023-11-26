package com.synrgy.commit.idstar.karyawan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
@Data
@Entity
@Table(name = "training")
@Where(clause = "deleted_date is null")
public class Training implements Serializable {
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
    @Column(name = "tema", nullable = false, length = 100)
    private String tema;
    @Column(name = "pengajar", nullable = false, length = 100)
    private String pengajar;
//    @JsonIgnore
//    @OneToMany(mappedBy = "training")
//    private Set<KaryawanTraining> karyawanTraining;
}
