package com.example.MuseumTicketing.spotReg.userData.dashboardDTO.count;

import lombok.Data;

import java.util.List;

@Data
public class VisitsCountDto {
    private List<PublicVisitorsDto> publicVisitorsDtoList;
    private List<InstitutionVisitorsDto> institutionVisitorsDtoList;
    private List<ForeignerVisitorsDto> foreignerVisitorsDtoList;
}
