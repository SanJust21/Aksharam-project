package com.example.MuseumTicketing.appGuide.img.first;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

@Data
@Entity
@Table(name = "imgFirstMob")
@CrossOrigin
public class ImgDataFirst {
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

    @Column(name = "mainEngUid")
    private String MainEngUid;

    @Column(name = "mainMalUid")
    private String MainMalUid;

    @Column(name = "commonId")
    private String commonId;

    @Column(name = "imageName",length = 10000)
    private String imageName;

    @Column(name = "imageRefUrl",length = 10000)
    private String imageRefUrl;

    public ImgDataFirst() {
    }

    public ImgDataFirst(String fName, String fUrl,String commonId) {
        this.fName = fName;
        this.fUrl = fUrl;
        this.commonId=commonId;
    }

    public ImgDataFirst(String fName, String fUrl,String commonId, String imageName, String imageRefUrl) {
        this.fName = fName;
        this.fUrl = fUrl;
        this.commonId = commonId;
        this.imageName = imageName;
        this.imageRefUrl = imageRefUrl;
    }
}
