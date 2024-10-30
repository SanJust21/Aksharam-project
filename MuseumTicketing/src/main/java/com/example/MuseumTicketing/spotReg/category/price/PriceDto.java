package com.example.MuseumTicketing.spotReg.category.price;

import lombok.Data;

@Data
public class PriceDto {
    private Integer categoryId;
    private String categoryName;
    private Integer typeId;
    private String typeName;
    private Double price;
}
