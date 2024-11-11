package com.example.MuseumTicketing.spotReg.userData.dashboardDTO.count;

import lombok.Data;

import java.util.List;

@Data
public class VisitsCountDto {
//    private List<PublicVisitorsDto> publicVisitorsDtoList;
//    private List<InstitutionVisitorsDto> institutionVisitorsDtoList;
//    private List<ForeignerVisitorsDto> foreignerVisitorsDtoList;
    private Integer publicTicketCount;
    private Integer adultCount;
    private Integer childCount;
    private Integer seniorCitizen;

    private Integer institutionTicketCount;
    private Integer teacherCount;
    private Integer studentCount;

    private Integer foreignerTicketCount;
    private Integer foreignAdult;
    private Integer foreignChild;
    private Integer totalVisitsCount;
}
