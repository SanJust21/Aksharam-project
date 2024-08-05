package com.example.MuseumTicketing.appGuide.mainPara.first;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

@Data
@Entity
@Table(name = "subParaMal")
@CrossOrigin
public class FirstTopicMal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "topic")
    private String topic;

    @Column(name = "description", length = 100000)
    private String description;


    @Column(name = "ref", length = 10000)
    private String ref;

    @Column(name = "fsUid")
    private String fsUid;

    @Column(name ="mainUid")
    private String mainUid;
}
