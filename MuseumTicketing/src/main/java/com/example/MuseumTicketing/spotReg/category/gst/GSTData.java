package com.example.MuseumTicketing.spotReg.category.gst;

import com.example.MuseumTicketing.Model.Price;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "gstTbl")
public class GSTData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "item")
    private String item;

    @Column(name = "amount")
    private Double amount;
}
