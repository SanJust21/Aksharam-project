package com.example.MuseumTicketing.spotReg.userData;

import lombok.Data;

@Data
public class SpotUpdateDto {
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
