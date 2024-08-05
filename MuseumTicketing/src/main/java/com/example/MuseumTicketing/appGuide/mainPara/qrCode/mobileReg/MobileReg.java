package com.example.MuseumTicketing.appGuide.mainPara.qrCode.mobileReg;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "mobileReg")
public class MobileReg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "phNumber")
    private String phNumber;
}
