package com.example.MuseumTicketing.spotReg.userData;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SpotUserDto {

    private String name;

    private String phNumber;

    private String district;

    private Integer paymentMode;


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




}
