package com.example.MuseumTicketing.spotReg.userData.publicUser;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "publicData")
public class PublicData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phNumber")
    private String phNumber;

    @Column(name = "adult")
    private Integer adult;

    @Column(name = "child")
    private Integer child;

    @Column(name = "seniorCitizen")
    private Integer seniorCitizen;

    @Column(name = "visitDate")
    private LocalDate visitDate;

    @Column(name = "totalAmount")
    private Double totalAmount;

    @Column(name = "totalGstCharge")
    private Double totalGstCharge;

    @Column(name = "totalAdditionalCharges")
    private Integer totalAdditionalCharges;

    @Column(name = "grandTotal")
    private Double grandTotal;

    @Column(name = "orderId")
    private String orderId;

    @Column(name = "paymentId")
    private String paymentId;

    @Column(name = "paymentMode")
    private Integer paymentMode;

    @Column(name = "paymentStatusId")
    private Integer paymentStatusId;

    @Column(name = "ticketId")
    private String ticketId;

    @Column(name = "createdTime")
    private LocalTime createdTime;

    @Column(name = "slotId")
    private Integer slotId;

    @Column(name = "createdBy")
    private String createdBy;

}
