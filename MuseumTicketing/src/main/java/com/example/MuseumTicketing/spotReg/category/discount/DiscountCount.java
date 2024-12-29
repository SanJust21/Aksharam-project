package com.example.MuseumTicketing.spotReg.category.discount;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "discountCount")
public class DiscountCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "disCount")
    private Integer disCount;

    @Column(name = "userType")
    private String userType;
}
