package com.example.MuseumTicketing.tribal.tribalVideo;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

@Data
@Entity
@Table(name = "tribalVideo")
@CrossOrigin
public class TribalVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "fileUrl")
    private String fileUrl;

    @Column(name = "commonId")
    private String commonId;

    @Column(name = "englishId")
    private String englishId;

    @Column(name = "malayalamId")
    private String malayalamId;

    public TribalVideo() {
    }


    public TribalVideo(String fileName, String fileUrl, String commonId) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.commonId = commonId;
    }
}
