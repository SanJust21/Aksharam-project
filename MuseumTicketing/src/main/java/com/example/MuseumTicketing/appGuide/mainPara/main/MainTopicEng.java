package com.example.MuseumTicketing.appGuide.mainPara.main;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "mainTopicEng")
public class MainTopicEng {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mEngId")
    private Integer mEngId;

    @Column(name = "topic")
    private String topic;

    @Column(name = "mEngUid")
    private String mEngUid;

    @Column(name = "description", length = 100000)
    private String description;

    @Column(name = "ref", length = 10000)
    private String ref;
}
