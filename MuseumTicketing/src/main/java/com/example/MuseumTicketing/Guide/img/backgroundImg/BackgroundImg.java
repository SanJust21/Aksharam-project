package com.example.MuseumTicketing.Guide.img.backgroundImg;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

@Data
@Entity
@Table(name = "bgTbl")
@CrossOrigin
public class BackgroundImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "bgName")
    private String bgName;

    @Column(name ="bgUrl")
    private String bgUrl;

    @Column(name = "commonId")
    private String commonId;

    @Column(name = "engId")
    private String engId;

    @Column(name = "malId")
    private String malId;


    public BackgroundImg() {
    }

    @PrePersist
    @PreUpdate
    public void setDefault(){
        if (bgUrl==null){
            bgUrl="No Data";
        }if (bgName==null){
            bgName="No Data";
        }if (engId==null){
            engId="No Data";
        }if (malId==null){
            malId="No Data";
        }
    }

    public BackgroundImg(String bgName, String bgUrl, String commonId) {
        this.bgName = bgName;
        this.bgUrl = bgUrl;
        this.commonId = commonId;
    }
}
