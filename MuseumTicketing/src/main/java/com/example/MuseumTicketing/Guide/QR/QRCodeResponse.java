package com.example.MuseumTicketing.Guide.QR;

public class QRCodeResponse {

    private String commonId;
    private String message;

    public QRCodeResponse(String commonId, String message) {
        this.commonId = commonId;
        this.message = message;
    }

    public String getCommonId() {
        return commonId;
    }

    public String getMessage() {
        return message;
    }


}
