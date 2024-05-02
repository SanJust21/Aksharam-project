package com.example.MuseumTicketing.DTO;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


@Data
public class PriceRequest {


   private int id;

    private String type;

    private int price;

    private String category;
}
