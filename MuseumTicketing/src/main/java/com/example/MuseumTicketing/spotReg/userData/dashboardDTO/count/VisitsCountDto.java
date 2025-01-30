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

    private Double publicGrandTotal;
    private Double adultGrandTotal;
    private Double childGrandTotal;
    private Double seniorCitizenGrandTotal;

    private Integer institutionTicketCount;
    private Integer teacherCount;
    private Integer studentCount;
    private Integer no_Of_Discount;

    private Double institutionGrandTotal;
    private Double teacherGrandTotal;
    private Double studentGrandTotal;

    private Double overAllDiscountPercentage;

    private Integer foreignerTicketCount;
    private Integer foreignAdult;
    private Integer foreignChild;
    private Integer totalVisitsCount;

    private Double foreignerGrandTotal;
    private Double foreignerAdultGrandTotal;
    private Double foreignerChildGrandTotal;

    private Double overAllIncome;
}
