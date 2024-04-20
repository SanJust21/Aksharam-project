package com.example.MuseumTicketing.DTO.AdminScanner;

import lombok.Data;

@Data
public class CustomResponse {
    private String message;
    private int errorCode;

    public CustomResponse(String message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

}
