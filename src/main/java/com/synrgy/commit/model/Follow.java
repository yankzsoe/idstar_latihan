package com.synrgy.commit.model;

import com.synrgy.commit.model.oauth.User;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "follow")
@Where(clause = "deleted_date is null")
public class Follow extends AbstractDate implements Serializable {

    @Id
    @Column(name = "id_follow")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_follow;


    @JoinColumn(name = "id_user")
    private Long id_user;


    @JoinColumn(name = "id_user_following")
    private Long id_user_following;

    @Column(name = "is_follow", nullable = false)
    private Boolean is_follow = false;
}
