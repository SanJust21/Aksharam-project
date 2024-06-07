package com.example.MuseumTicketing.Guide.mainHeading.mainMal;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

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

    @Column(name = "description", length = 6000)
    private String description;

    @Column(name = "ref")
    private String ref;

    public MainTitleMal() {
    }

    @PrePersist
    @PreUpdate
    public void setDefault(){
        if (description==null){
            description="No Data";
        }if (ref==null){
            ref="No Data";
        }
    }
}
