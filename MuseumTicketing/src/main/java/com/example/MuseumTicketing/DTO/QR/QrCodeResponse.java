package com.example.MuseumTicketing.DTO.QR;


import lombok.Data;

@Data
public class QrCodeResponse {

    private byte[] qrCodeImage;
    private String userDetails;
    private String ticketId;
    private String errorMessage;

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}