package com.example.MuseumTicketing.Guide.mainHeading.mainMal;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.nio.charset.StandardCharsets;

@Data
@Entity
@Table(name = "mainMal")
@CrossOrigin
public class MainTitleMal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mMalId")
    private Integer mMalId;

    @Column(name = "title")
    private String title;

    @Column(name = "mMalUid")
    private String mMalUid;

    @Column(name = "description", length = 100000)
    private String description;

    @Column(name = "ref", length = 10000)
    private String ref;

    public MainTitleMal() {
    }

//    @PrePersist
//    @PreUpdate
//    public void setDefault(){
//        if (description==null){
//            description="No Data";
//        }if (ref==null){
//            ref="No Data";
//        }
//    }
}
