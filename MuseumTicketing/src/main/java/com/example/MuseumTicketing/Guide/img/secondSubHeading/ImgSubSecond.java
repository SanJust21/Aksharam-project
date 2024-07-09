package com.example.MuseumTicketing.Guide.img.secondSubHeading;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
@Data
@Entity
@Table(name = "imgSub2")
@CrossOrigin
@Component
public class ImgSubSecond {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imgID")
    private Integer imgID;

    @Column(name = "fName")
    private String fName;

    @Column(name = "fUrl")
    private String fUrl;

    @Column(name = "bgName")
    private String bgName;

    @Column(name ="bgUrl")
    private String bgUrl;

    @Column(name = "engId")
    private String engId;

    @Column(name = "malId")
    private String malId;

    @Column(name = "fsEngUid")
    private String fsEngUid;

    @Column(name = "fsMalUid")
    private String fsMalUid;

    @Column(name = "commonId")
    private String commonId;

    public ImgSubSecond() {
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
        }if (fsEngUid==null){
            fsEngUid="No Data";
        }if (fsMalUid==null){
            fsMalUid="No Data";
        }if (commonId==null){
            commonId="No Data";
        }if (bgName==null){
            bgName="No Data";
        }if (bgUrl==null){
            bgUrl="No Data";
        }
    }

    public ImgSubSecond(String fName, String fUrl, String bgName, String bgUrl, String engId, String malId, String commonId) {
        this.fName = fName;
        this.fUrl = fUrl;
        this.bgName = bgName;
        this.bgUrl = bgUrl;
        this.engId = engId;
        this.malId = malId;
        this.commonId = commonId;
    }

    //    public ImgSubSecond(String fName, String fUrl, String engId, String malId, String commonId) {
//        this.fName = fName;
//        this.fUrl = fUrl;
//        this.engId = engId;
//        this.malId = malId;
//        this.commonId = commonId;
//    }
}
