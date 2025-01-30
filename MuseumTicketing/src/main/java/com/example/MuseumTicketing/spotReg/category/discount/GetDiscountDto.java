package com.example.MuseumTicketing.spotReg.category.discount;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class GetDiscountDto {
    private Integer id;
    private Integer disCount;

    private Integer categoryId;

    private String categoryName;


    private Integer typeId;
    private String typeName;

    private String userType;
}
