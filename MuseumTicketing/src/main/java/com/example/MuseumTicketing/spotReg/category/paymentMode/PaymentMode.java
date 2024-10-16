package com.example.MuseumTicketing.spotReg.category.paymentMode;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "paymentModeTbl")
public class PaymentMode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "paymentType")
    private String paymentType;
}
