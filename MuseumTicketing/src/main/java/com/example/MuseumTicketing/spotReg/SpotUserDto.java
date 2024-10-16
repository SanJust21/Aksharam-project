package com.example.MuseumTicketing.spotReg;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SpotUserDto {

    private String name;

    private String phNumber;

    private String district;

    private Integer paymentMode;
    private String paymentModeName;


    private Integer adult;
    private Integer adultTypeId;

    private Integer childTypeId;
    private Integer child;

    private Integer seniorCitizen;
    private Integer seniorCitizenTypeId;

    private Integer Student;
    private Integer studentTypeId;

    private Integer teacher;
    private Integer teacherTypeId;

    private LocalDate visitDate;

    private Double grandTotal;
    private Integer paymentStatusId;
    private String paymentStatus;

    private Integer slotId;
    private LocalTime slotTime;

    private String createdBy;
    private LocalTime generatedTime;
    private Double totalRevenue;
}
