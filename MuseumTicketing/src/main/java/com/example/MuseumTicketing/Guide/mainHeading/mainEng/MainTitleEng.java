package com.example.MuseumTicketing.Guide.mainHeading.mainEng;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.nio.charset.StandardCharsets;

@Data
@Entity
@Table(name = "mainEng")
@CrossOrigin
public class MainTitleEng {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mEngId")
    private Integer mEngId;

    @Column(name = "title")
    private String title;

    @Column(name = "mEngUid")
    private String mEngUid;

    @Column(name = "description", length = 100000)
    private String description;

    @Column(name = "ref",length = 10000)
    private String ref;



    public MainTitleEng() {
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
