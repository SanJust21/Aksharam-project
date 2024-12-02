package com.example.MuseumTicketing.spotReg;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SpotBookingDto {
    private String name;
    private String phNumber;
    private String district;
    private Integer adultCount;
    private Integer childCount;
    private Integer seniorCitizenCount;
    private Integer teacherCount;
    private Integer studentCount;
    private LocalDate visitDate;
    private LocalTime slotStartTime;
    private Double totalAmount;
    private Double totalGstCharge;
    private Integer totalAdditionalCharges;
    private Double grandTotal;
    private String orderId;
    private String paymentId;
    private String ticketId;
    private byte[] qrCodeImage;
    private LocalTime createdTime;
}
