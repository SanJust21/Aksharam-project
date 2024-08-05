package com.example.MuseumTicketing.appGuide.audio.main;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

@Data
@Entity
@Table(name = "audionMain")
@CrossOrigin
public class AudioMain {
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

    @Column(name = "commonId")
    private String commonId;

    public AudioMain() {
    }

    public AudioMain(String fName, String fUrl) {
        this.fName = fName;
        this.fUrl = fUrl;
    }

    public AudioMain(String fName, String fUrl, String dtId) {
        this.fName = fName;
        this.fUrl = fUrl;
        this.dtId = dtId;
    }


}
