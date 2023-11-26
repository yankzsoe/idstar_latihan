package com.synrgy.commit.model.piksi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.synrgy.commit.model.AbstractDate;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "lookup")
@Where(clause = "deleted_date is null")
public class Lookup extends AbstractDate implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String nama;

    @Column()
    private String type;

    @JsonIgnore
    @Column(name = "id_parent")
    private Long idParent;


}
