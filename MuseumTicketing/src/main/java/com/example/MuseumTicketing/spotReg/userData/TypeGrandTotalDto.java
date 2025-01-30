package com.example.MuseumTicketing.spotReg.userData;

import lombok.Data;

@Data
public class TypeGrandTotalDto {
    private Long tableId;
    private Double adultGrandTotal;
    private Double childGrandTotal;
    private Double seniorCitizenGrandTotal;

    private Double teacherGrandTotal;
    private Double studentGrandTotal;


}
