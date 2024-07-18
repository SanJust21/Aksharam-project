package com.example.MuseumTicketing.appGuide.audio.first;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "audioFirst")
public class AudioFirst {
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

    @Column(name ="mainEngId")
    private String mainEngId;

    @Column(name = "mainMalId")
    private String mainMalId;

    public AudioFirst() {
    }

    public AudioFirst(String fName, String fUrl, String dtId) {
        this.fName = fName;
        this.fUrl = fUrl;
        this.dtId = dtId;
    }
}
