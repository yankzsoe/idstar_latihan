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
@Table(name = "penugasan")
@Where(clause = "deleted_date is null")
public class Penugasan extends AbstractDate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deskripsi;
}
