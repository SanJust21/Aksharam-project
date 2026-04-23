package com.example.MuseumTicketing.spotReg.userData.dashboardDTO.usesrDetails;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ForeignerDtoData {
    private String name;
    private String phNumber;
    private Integer adultCount;
    private Integer childCount;

    private Double adultCharge;
    private Double childCharge;

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
    private boolean visitorsStatus;
    private String categoryName;
}
