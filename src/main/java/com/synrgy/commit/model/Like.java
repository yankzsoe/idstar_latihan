package com.synrgy.commit.model;

import com.synrgy.commit.model.oauth.User;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "like_post")
@Where(clause = "deleted_date is null")
public class Like extends AbstractDate implements Serializable {

    @Id
    @Column(name = "id_like")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_like;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User id_user;

    @JoinColumn(name = "id_post")
    private Long id_post;
}
