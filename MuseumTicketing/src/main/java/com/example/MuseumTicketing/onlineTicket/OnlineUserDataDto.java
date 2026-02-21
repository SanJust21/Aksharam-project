package com.example.MuseumTicketing.onlineTicket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUserDataDto {
    private String name;

    private String phNumber;

    private String district;


    private Integer adult;
    private Integer adultTypeId;

    private Integer childTypeId;
    private Integer child;

    private Integer seniorCitizen;
    private Integer seniorCitizenTypeId;

    private Integer student;
    private Integer studentTypeId;

    private Integer teacher;
    private Integer teacherTypeId;

    private String emailId;

    private LocalDate visitDate;
    private Integer slotId;
}
