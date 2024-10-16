package com.example.MuseumTicketing.spotReg.category.type;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "typeTbl")
public class TypeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "type")
    private String type;

    @Column(name = "categoryId")
    private Integer categoryId;
}
