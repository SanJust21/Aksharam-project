package com.example.MuseumTicketing.spotReg.userData.dashboardDTO.count;

import lombok.Data;

@Data
public class VisitorsAmountDto {
    private Double publicIncome;
    private Integer publicTicketCount;
    private Double institutionIncome;
    private Integer institutionTicketCount;
    private Double foreignerIncome;
    private Integer foreignerTicketCount;
    private Integer totalTicketCount;
    private Double totalIncome;
}
