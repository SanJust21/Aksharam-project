package com.example.MuseumTicketing.appGuide.mainPara.main;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

@Data
@Entity
@Table(name = "mainTopicMal")
@CrossOrigin
public class MainTopicMal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mMalId")
    private Integer mMalId;

    @Column(name = "topic")
    private String topic;

    @Column(name = "mMalUid")
    private String mMalUid;

    @Column(name = "description", length = 100000)
    private String description;

    @Column(name = "ref", length = 10000)
    private String ref;
}
