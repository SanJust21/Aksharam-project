package com.example.MuseumTicketing.Guide.img.mainHeading;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

@Data
@Entity
@Table(name = "imgTbl")
@CrossOrigin
public class ImgData {
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

    @Column(name = "commonId")
    private String commonId;

    public ImgData() {
    }

    public ImgData(String fName, String fUrl) {
        this.fName = fName;
        this.fUrl = fUrl;
    }

    public ImgData(String engId, String malId, String commonId) {
        this.engId = engId;
        this.malId = malId;
        this.commonId = commonId;
    }

    public ImgData(String fName, String fUrl, String engId, String malId, String commonId) {

        this.fName = fName;
        this.fUrl = fUrl;
        this.engId = engId;
        this.malId = malId;
        this.commonId = commonId;
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
        }if(commonId==null){
            commonId="No Data";
        }
    }
}
