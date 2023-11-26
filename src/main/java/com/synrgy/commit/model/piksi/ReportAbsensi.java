package com.synrgy.commit.model.piksi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synrgy.commit.model.AbstractDate;
import com.synrgy.commit.model.oauth.User;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "report_absensi")
@Where(clause = "deleted_date is null")
public class ReportAbsensi extends AbstractDate implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "id_user")
    @ManyToOne
    private User id_user;

    @Column(name = "alasan", columnDefinition="TEXT")
    private String alasan;

    @Column(name = "type")
    private String type;//chekin -checkout

    @JoinColumn(name = "id_penugasan")
    @ManyToOne
    private Penugasan penugasan;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "Asia/Jakarta")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false)
    @UpdateTimestamp
    private Date date;

    @Column(name = "longitude ")
    private String longitude ;

    @Column(name = "latitude ")
    private String latitude ;

    @Column(name = "photoUrl ")
    private String photoUrl ;



}
