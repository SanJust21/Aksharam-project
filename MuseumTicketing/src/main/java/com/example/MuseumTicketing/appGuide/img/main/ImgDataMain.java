package com.example.MuseumTicketing.appGuide.img.main;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Data
@Entity
@Table(name = "imgMainMob")
@CrossOrigin
public class ImgDataMain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fName")
    private String fName;

    @Column(name = "fUrl")
    private String fUrl;

    @Column(name = "engId")
    private String engId;

    @Column(name = "malId")
    private String malId;

    @Column(name = "commonId")
    private String commonId;

    public ImgDataMain() {
    }

    public ImgDataMain(String fName, String fUrl,String commonId) {
        this.fName = fName;
        this.fUrl = fUrl;
        this.commonId = commonId;
    }

}
