package com.example.MuseumTicketing.spotReg.userData.dashboardDTO;

import lombok.Data;

@Data
public class GetRevenueDetails {
    private Double publicRevenue;
    private Double institutionRevenue;
    private Double foreignerRevenue;
    private Double overAllRevenue;
}
