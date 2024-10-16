package com.example.MuseumTicketing.spotReg.category.category;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "categoryTbl")
public class CategoryData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "category")
    private String category;
}
