package com.example.MuseumTicketing.spotReg.userData.dashboardDTO.usesrDetails;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class InstitutionDtoData {
    private String name;
    private String phNumber;
    private String district;
    private Integer teacherCount;
    private Integer studentCount;

    private Double teacherCharge;
    private Double studentCharge;
    private Double studentDiscount;
    private Double payableStudentCharge;
    private Double discountAmount;

    private LocalDate visitDate;
    private LocalTime slotTime;
    private Double grandTotal;
    private String paymentModeName;
    private String paymentStatusName;
    private String ticketId;
    private String orderId;
    private String paymentId;
    private String createdBy;
    private LocalTime generatedTime;
    private boolean visitorsStatus;
    private String categoryName;
}
