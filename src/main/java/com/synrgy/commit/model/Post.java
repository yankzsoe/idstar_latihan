package com.synrgy.commit.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.repository.FilePostRepository;
import com.synrgy.commit.repository.PostRepository;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.provider.HibernateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "post_user")
@Where(clause = "deleted_date is null")
public class Post extends AbstractDate implements Serializable {

    @Id
    @Column(name = "id_post")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_post;

    @Column(name = "post_desc", columnDefinition = "TEXT", nullable = false)
    private String post_desc;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FilePost> filePosts;

    @Column(name = "post_status", nullable = false)
    private Boolean post_status;

    @Column(name = "post_tags", nullable = false)
    private String post_tags;

    private Long total_like = Long.valueOf(0);

    private Long total_komentar = Long.valueOf(0);

    @Transient
    private boolean isLiked;
    @Transient
    private boolean bookmarked;

    @ManyToOne
    @Setter
    @Getter
    @JoinColumn(name = "id_user_creator")
    User user;


    public Long getId_post() {
        return id_post;
    }

    public void setId_post(Long id_post) {
        this.id_post = id_post;
    }

    public Long getTotal_like() {
        return total_like;
    }

    public void setTotal_like(Long total_like) {
        this.total_like = total_like;
    }

    public Long getTotal_komentar() {
        return total_komentar;
    }

    public void setTotal_komentar(Long total_komentar) {
        this.total_komentar = total_komentar;
    }

    public String getPost_desc() {
        return post_desc;
    }

    public void setPost_desc(String post_desc) {
        this.post_desc = post_desc;
    }

    public List<FilePost> getFilePosts() {
        return filePosts;
    }

    public void setFilePosts(List<FilePost> filePosts) {
        this.filePosts = filePosts;
    }

    public Boolean getPost_status() {
        return post_status;
    }

    public void setPost_status(Boolean post_status) {
        this.post_status = post_status;
    }

    public String[] getPost_tags() {
        String[] tagsSplit = post_tags.split(",");
        return tagsSplit;
    }

    public void setPost_tags(String post_tags) {
        this.post_tags = post_tags;
    }

    public boolean isIsLiked(boolean isLiked) {
        return isLiked;
    }

    public void setIs_liked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public Post() {
    }

    public Post(Date created_date,Long id_post, String post_desc, boolean post_status , String post_tags, Long total_like, Long total_komentar, boolean isLiked, User user, Post post) {
        this.setCreated_date(created_date);
        this.id_post = id_post;
        this.post_desc = post_desc;
        this.post_status = post_status;
        this.post_tags = post_tags;
        this.total_like = total_like;
        this.total_komentar = total_komentar;
        this.isLiked = isLiked;
        this.user = user;
        this.filePosts = post.getFilePosts();
    }

    public Post(Date created_date,Long id_post, String post_desc, boolean post_status , String post_tags, Long total_like, Long total_komentar, boolean isLiked, User user, Post post, boolean bookmarked) {
        this.setCreated_date(created_date);
        this.id_post = id_post;
        this.post_desc = post_desc;
        this.post_status = post_status;
        this.post_tags = post_tags;
        this.total_like = total_like;
        this.total_komentar = total_komentar;
        this.isLiked = isLiked;
        this.user = user;
        this.filePosts = post.getFilePosts();
        this.bookmarked = bookmarked;
    }



}