package com.example.MuseumTicketing.Guide.SecondSubHeading.commonId;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ssCommonIds")
public class CommonIdSs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ssCommonId")
    private String ssCommonId;

    @Column(name = "ssEngId")
    private String ssEngId;

    @Column(name = "ssMalId")
    private String ssMalId;
}