package com.synrgy.commit.idstar.karyawan.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.Date;
@Data
@Entity
@Table(name = "karyawan_training")
@Where(clause = "deleted_date is null")
public class KaryawanTraining implements Serializable {
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
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(name = "training_date", nullable = false)
    private Date training_date;

    @ManyToOne()
    @JoinColumn(name = "id_karyawan", nullable = false)
    private Karyawan karyawan;

    @ManyToOne()
    @JoinColumn(name = "id_training", nullable = false)
    private Training training;
}
