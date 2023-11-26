package com.synrgy.commit.model;

import com.synrgy.commit.model.oauth.User;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "report_user")
@Where(clause = "deleted_date is null")
public class ReportUser extends AbstractDate implements Serializable {
    @Id
    @Column(name = "id_report")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_report_user;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User id_user;

    @ManyToOne
    @JoinColumn(name = "otherUser")
    private User otherUser;

    @Column(name = "reason")
    private String reason;
}
