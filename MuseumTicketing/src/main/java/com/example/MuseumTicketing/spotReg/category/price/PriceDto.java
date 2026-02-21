package com.example.MuseumTicketing.spotReg.category.price;

import lombok.Data;

@Data
public class PriceDto {
    private Integer id;
    private Integer categoryId;
    private String categoryName;
    private Integer typeId;
    private String typeName;
    private String type;
    private Double price;
}
