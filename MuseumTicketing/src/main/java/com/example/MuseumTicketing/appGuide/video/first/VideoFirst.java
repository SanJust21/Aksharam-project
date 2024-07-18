package com.example.MuseumTicketing.appGuide.video.first;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "videoFirst")
public class VideoFirst {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fName")
    private String fName;

    @Column(name = "fUrl")
    private String fUrl;

    @Column(name = "dtId")
    private String dtId;

    @Column(name = "fsEngId")
    private String fsEngId;

    @Column(name = "fsMalId")
    private String fsMalId;


    @Column(name ="mainEngId")
    private String mainEngId;

    @Column(name = "mainMalId")
    private String mainMalId;

    public VideoFirst() {
    }

    public VideoFirst(String fName, String fUrl, String dtId) {
        this.fName = fName;
        this.fUrl = fUrl;
        this.dtId = dtId;
    }
}
