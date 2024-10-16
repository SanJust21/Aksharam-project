package com.example.MuseumTicketing.spotReg.category.additionCharge;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "additnCharge")
public class AdditionCharge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "addChargeName")
    private String addChargeName;

    @Column(name = "charge")
    private Integer charge;
}
