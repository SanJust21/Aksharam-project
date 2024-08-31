package com.example.MuseumTicketing.tribal.tribMalayalam;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tribalMal")
public class TribalMalayalam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "description",length = 100000)
    private String description;
    @Column(name = "tribMalUid")
    private String tribMalUid;
}
