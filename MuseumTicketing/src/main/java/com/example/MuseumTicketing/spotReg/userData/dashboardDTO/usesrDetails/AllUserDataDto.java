package com.example.MuseumTicketing.spotReg.userData.dashboardDTO.usesrDetails;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class AllUserDataDto {
    private String name;
    private String phNumber;
    private Integer adultCount;
    private Double adultCharge;
    private Integer childCount;
    private Double childCharge;
    private Integer seniorCitizenCount;
    private Double seniorCitizenCharge;
    private String district;
    private Integer teacherCount;
    private Double teacherCharge;
    private Integer studentCount;
    private Double studentCharge;
    private Double studentDiscount;
    private Double payableStudentCharge;
    private Double discountAmount;
    private LocalDate visitDate;
    private LocalDate bookDate;
    private LocalTime slotTime;
    private Double grandTotal;
    private String paymentModeName;
    private String paymentStatusName;
    private String ticketId;
    private String orderId;
    private String paymentId;
    private String createdBy;
    private LocalTime generatedTime;
    private boolean visitStatus;
    private String categoryName;
}
