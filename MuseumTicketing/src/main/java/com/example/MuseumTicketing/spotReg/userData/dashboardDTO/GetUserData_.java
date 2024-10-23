package com.example.MuseumTicketing.spotReg.userData.dashboardDTO;

import lombok.Data;

import java.time.LocalTime;

@Data
public class GetUserData_ {
    private String name;

    private String phNumber;

    private String district;

    private String paymentModeName;


    private Integer adult;


    private Integer child;

    private Integer seniorCitizen;


    private Integer Student;


    private Integer teacher;

    private Double totalRevenue;

    private Double grandTotal;

    private String paymentStatus;


    private LocalTime slotTime;

    private String createdBy;
    private LocalTime generatedTime;

    private String ticketId;
    private String orderId;
    private String paymentId;
}
