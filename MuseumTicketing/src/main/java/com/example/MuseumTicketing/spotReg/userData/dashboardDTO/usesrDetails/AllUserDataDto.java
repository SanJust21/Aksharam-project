package com.example.MuseumTicketing.spotReg.userData.dashboardDTO.usesrDetails;

import lombok.Data;

import java.util.List;

@Data
public class AllUserDataDto {
    private List<PublicDtoData> publicDtoDataList;
    private List<InstitutionDtoData> institutionDtoDataList;
    private List<ForeignerDtoData> foreignerDtoDataList;
}
