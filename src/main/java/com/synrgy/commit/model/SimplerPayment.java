package com.synrgy.commit.model;

import com.synrgy.commit.model.oauth.User;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "simpler_payment")
@Where(clause = "deleted_date is null")
public class SimplerPayment extends AbstractDate implements Serializable {

    @Id
    @Column(name = "id_payment")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_payment;

    @Column(name = "plan", nullable = false)
    private Long plan;

    @Column(name = "payment_method", nullable = false)
    private String payment_method;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "total_paid", nullable = false)
    private String total_paid;

    @Column(name = "image_payment", nullable = false)
    private String image_payment;

    @Column(name = "transaction_id", nullable = false)
    private String transaction_id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User id_user;
}
