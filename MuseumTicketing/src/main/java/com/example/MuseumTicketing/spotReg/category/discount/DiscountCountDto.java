package com.example.MuseumTicketing.spotReg.category.discount;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class DiscountCountDto {
    private Integer disCount;

    private String userType;
}
