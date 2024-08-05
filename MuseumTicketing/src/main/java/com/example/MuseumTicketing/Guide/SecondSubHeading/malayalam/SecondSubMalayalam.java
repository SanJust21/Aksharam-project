package com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.nio.charset.StandardCharsets;

@Data
@Entity
@Table(name = "secondSubMal")
@CrossOrigin
public class SecondSubMalayalam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", length = 100000)
    private String description;


    @Column(name = "ref", length = 10000)
    private String ref;

    @Column(name = "ssUid")
    private String ssUid;

    @Column(name ="fsUid")
    private String fsUid;

    public SecondSubMalayalam() {
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
