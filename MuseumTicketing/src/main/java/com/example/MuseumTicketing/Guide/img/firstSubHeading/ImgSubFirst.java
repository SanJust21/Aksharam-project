package com.example.MuseumTicketing.Guide.img.firstSubHeading;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

@Data
@Entity
@Table(name = "imgSub1")
@CrossOrigin
@Component
public class ImgSubFirst {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imgID")
    private Integer imgID;

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

    public ImgSubFirst() {
    }
    @PrePersist
    @PreUpdate
    public void setDefault(){
        if (fName==null){
            fName="No Data";
        }if (fUrl==null){
            fUrl="No Data";
        }if (engId==null){
            engId="No Data";
        }if (malId==null){
            malId="No Data";
        }if(MainEngUid==null){
            MainEngUid="No data";
        }if (MainMalUid==null){
            MainMalUid="No data";
        }if(commonId==null){
            commonId="No data";
        }
    }



    public ImgSubFirst(String fName, String fUrl, String commonId) {
        this.fName = fName;
        this.fUrl = fUrl;
        this.commonId = commonId;
    }

}
