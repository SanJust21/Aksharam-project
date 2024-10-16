package com.example.MuseumTicketing.spotReg.category.paymentStatus;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "paymentStatusTbl")
public class PaymentStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "statusName")
    private String statusName;
}
