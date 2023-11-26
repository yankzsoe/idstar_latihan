package com.synrgy.commit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.synrgy.commit.model.oauth.User;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "komentar_post")
@Where(clause = "deleted_date is null")
public class Komentar extends AbstractDate implements Serializable {

    @Id
    @Column(name = "id_komentar")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_komentar;

    @Column(name = "isiKomentar", columnDefinition="TEXT")
    private String isiKomentar;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User id_user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_post")
    private Post id_post;
}
