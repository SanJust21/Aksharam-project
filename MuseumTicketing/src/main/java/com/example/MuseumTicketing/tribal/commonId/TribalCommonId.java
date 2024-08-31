package com.example.MuseumTicketing.tribal.commonId;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "triCommonTbl")
public class TribalCommonId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "malayalamId")
    private String malayalamId;
    @Column(name = "englishId")
    private String englishId;
    @Column(name = "commonId")
    private String commonId;
}
