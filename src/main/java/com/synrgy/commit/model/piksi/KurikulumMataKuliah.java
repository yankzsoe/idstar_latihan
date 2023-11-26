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
public class KurikulumMataKuliah extends AbstractDate implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @JoinColumn(name = "created_user")
    @ManyToOne
    private User createdUser;

    @JoinColumn(name = "mata_kuliah_id")
    @ManyToOne
    private MataKuliah matkul;

    @JoinColumn(name = "kurikulum_id")
    @ManyToOne
    private Kurikulum kurikulum;

    private String semester;//angka 1

    private Boolean type;// true : wajib, false : no wajib
}
