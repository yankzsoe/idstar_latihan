package com.synrgy.commit.model;

import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "bookmark")
@Where(clause = "deleted_date is null")
public class Bookmark extends AbstractDate implements Serializable {
    @Id
    @Column(name = "id_bookmark")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_bookmark;
    @Column(name = "id_user")
    private Long id_user;
    @Column(name = "id_post")
    private Long id_post;
}
