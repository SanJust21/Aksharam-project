package com.example.MuseumTicketing.spotReg.userData.dashboardDTO.count;

import lombok.Data;

@Data
public class PublicVisitorsDto {
    private Integer publicCount;
    private Integer adultCount;
    private Integer childCount;
    private Integer seniorCitizen;

}
