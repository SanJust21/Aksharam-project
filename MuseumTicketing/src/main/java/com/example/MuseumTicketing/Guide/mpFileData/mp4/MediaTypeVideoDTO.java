package com.example.MuseumTicketing.Guide.mpFileData.mp4;

import lombok.Data;

@Data
public class MediaTypeVideoDTO {
    private String fName;
    private String fUrl;
    private String dtId;
    private String engId;
    private String malId;
    private String thumbnailUrl;
    private String thumbnailName;

    public MediaTypeVideoDTO(String fName, String fUrl, String dtId, String engId, String malId, String thumbnailUrl, String thumbnailName) {
        this.fName = fName;
        this.fUrl = fUrl;
        this.dtId = dtId;
        this.engId = engId;
        this.malId = malId;
        this.thumbnailUrl = thumbnailUrl;
        this.thumbnailName = thumbnailName;
    }
}
