package com.example.MuseumTicketing.tribal.tribalVideo;

import lombok.Data;

@Data
public class TribalVideoDto {
    private String fileName;
    private String fileUrl;
    private String commonId;
    private String malayalamId;
    private String englishId;

    public TribalVideoDto(String fileName, String fileUrl) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public TribalVideoDto(String fileName, String fileUrl, String commonId, String malayalamId, String englishId) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.commonId = commonId;
        this.malayalamId = malayalamId;
        this.englishId = englishId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getCommonId() {
        return commonId;
    }

    public String getMalayalamId() {
        return malayalamId;
    }

    public String getEnglishId() {
        return englishId;
    }
}
