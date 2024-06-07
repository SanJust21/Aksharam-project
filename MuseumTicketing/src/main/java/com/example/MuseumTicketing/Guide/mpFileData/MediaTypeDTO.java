package com.example.MuseumTicketing.Guide.mpFileData;

public class MediaTypeDTO {

    private String fName;
    private String fUrl;
    private String dtId;

    public MediaTypeDTO(String fName, String fUrl, String dtId) {
        this.fName = fName;
        this.fUrl = fUrl;
        this.dtId = dtId;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getfUrl() {
        return fUrl;
    }

    public void setfUrl(String fUrl) {
        this.fUrl = fUrl;
    }

    public String getDtId() {
        return dtId;
    }

    public void setDtId(String dtId) {
        this.dtId = dtId;
    }
}
