package com.example.MuseumTicketing.appGuide.video.main;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

@Data
@Entity
@Table(name = "videoMain")
@CrossOrigin
public class VideoMain {
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

    @Column(name = "engId")
    private String engId;

    @Column(name = "malId")
    private String malId;

    public VideoMain() {
    }

    public VideoMain(String fName, String fUrl) {
        this.fName = fName;
        this.fUrl = fUrl;
    }

    public VideoMain(String fName, String fUrl, String dtId) {
        this.fName = fName;
        this.fUrl = fUrl;
        this.dtId = dtId;
    }
}
