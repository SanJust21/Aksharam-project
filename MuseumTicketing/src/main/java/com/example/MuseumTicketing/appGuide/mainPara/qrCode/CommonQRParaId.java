package com.example.MuseumTicketing.appGuide.mainPara.qrCode;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

@Data
@Entity
@Table(name = "commonId_para")
@CrossOrigin
public class CommonQRParaId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "common_id")
    private String commonId;

    @Column(name = "malId")
    private String malId;

    @Column(name = "engId")
    private String engId;

    @Column(name = "fName")
    private String fName;

    @Column(name = "qr_code_url")
    private String qrCodeUrl;

    @Lob
    @Column(name = "qr_code_image")
    private byte[] qrCodeImage;
}
