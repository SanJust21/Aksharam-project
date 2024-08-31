package com.example.MuseumTicketing.tribal.tribEnglish;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tribEng")
public class TribalEnglish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "description",length = 100000)
    private String description;
    @Column(name = "tribEngUid")
    private String tribEngUid;
}
