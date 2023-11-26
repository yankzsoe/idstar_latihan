package com.synrgy.commit.model;

import com.synrgy.commit.model.oauth.User;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "report_comment")
@Where(clause = "deleted_date is null")
public class ReportComment extends AbstractDate implements Serializable {
    @Id
    @Column(name = "id_report_comment")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_report_comment;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User id_user;

    @ManyToOne
    @JoinColumn(name = "id_komentar")
    private Komentar id_komentar;

    @Column(name = "reason")
    private String reason;
}
