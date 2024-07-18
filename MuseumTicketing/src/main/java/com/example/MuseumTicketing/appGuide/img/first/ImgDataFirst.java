package com.example.MuseumTicketing.appGuide.img.first;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "imgFirstMob")
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

    public ImgDataFirst() {
    }

    public ImgDataFirst(String fName, String fUrl,String commonId) {
        this.fName = fName;
        this.fUrl = fUrl;
        this.commonId=commonId;
    }


}
