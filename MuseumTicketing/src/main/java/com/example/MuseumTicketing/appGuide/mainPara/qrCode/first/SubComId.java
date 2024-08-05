package com.example.MuseumTicketing.appGuide.mainPara.qrCode.first;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

@Data
@Entity
@Table(name = "subComId")
@CrossOrigin
public class SubComId {
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
