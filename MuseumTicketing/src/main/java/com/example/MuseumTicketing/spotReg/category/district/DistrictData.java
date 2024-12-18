package com.example.MuseumTicketing.spotReg.category.district;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "districtTbl")
public class DistrictData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "district")
    private String district;
}
