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
    private Double adultCharge;


    private Integer child;
    private Double childCharge;

    private Integer seniorCitizen;
    private Double seniorCitizenCharge;


    private Integer Student;
    private Double studentCharge;
    private Double studentDiscount;
    private Double payableStudentCharge;
    private Double discountAmount;

    private Integer teacher;
    private Double teacherCharge;

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
