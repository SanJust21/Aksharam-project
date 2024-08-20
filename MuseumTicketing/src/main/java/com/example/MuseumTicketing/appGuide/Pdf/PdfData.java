package com.example.MuseumTicketing.appGuide.Pdf;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

@Data
@Entity
@Table(name = "pdfTbl")
@CrossOrigin
public class PdfData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fName")
    private String fName;

    @Column(name = "fUrl")
    private String fUrl;

    @Column(name = "uId")
    private String uId;

    @Column(name = "commonId")
    private String commonId;

    public PdfData() {
    }

    public PdfData(String fName, String fUrl) {
        this.fName = fName;
        this.fUrl = fUrl;
    }

    public PdfData(String fName, String fUrl, String uId) {
        this.fName = fName;
        this.fUrl = fUrl;
        this.uId = uId;
    }
}