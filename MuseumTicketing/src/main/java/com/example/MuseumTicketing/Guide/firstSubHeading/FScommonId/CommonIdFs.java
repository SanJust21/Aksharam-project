package com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "fsCommonId")
public class CommonIdFs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fsCommonId")
    private String fsCommonId;

    @Column(name = "fsEngId")
    private String fsEngId;

    @Column(name = "fsMalId")
    private String fsMalId;
}