package com.example.MuseumTicketing.spotReg.category.price;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "priceData")
public class PriceData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "categoryId")
    private Integer categoryId;

    @Column(name = "typeId")
    private Integer typeId;

    @Column(name = "price")
    private Double price;

}
