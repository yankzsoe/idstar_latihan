package com.synrgy.commit.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "file_post")
@Where(clause = "deleted_date is null")
public class FilePost extends AbstractDate implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, name = "url")
    private String url;

    @ManyToOne(targetEntity = Post.class)
    @JoinColumn(name = "post")
    @JsonBackReference
    private Post post;

}