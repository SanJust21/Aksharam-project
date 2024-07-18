package com.example.MuseumTicketing.Guide.mainHeading;

import com.example.MuseumTicketing.Guide.firstSubHeading.CombinedDataSub;
import com.example.MuseumTicketing.Guide.firstSubHeading.CombinedDataSub;
import com.example.MuseumTicketing.Guide.img.backgroundImg.BackgroundImg;
import com.example.MuseumTicketing.Guide.img.mainHeading.ImgData;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.mainHeading.Mp3Data;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.mainHeading.Mp4Data;
import lombok.Data;

import java.util.List;
@Data
public class CombinedData {
    private String title;
    private String description;
    private String referenceUrl;
    private String uId;
    private String commonId;
    private String qrCodeUrl;
    private byte[] qrCodeImage;
    private String malId;
    private String engId;

    private List<ImgData> imgDataList;
    private List<BackgroundImg> backgroundImgList;
    private List<Mp3Data> mp3DataList;
    private List<Mp4Data> mp4DataList;

    private List<CombinedDataSub> combinedDataSubList;
    //private List<CombinedDataSubSub> combinedDataSubSubList;

    public CombinedData() {
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReferenceUrl() {
        return referenceUrl;
    }

    public void setReferenceUrl(String referenceUrl) {
        this.referenceUrl = referenceUrl;
    }

    public List<ImgData> getImgDataList() {
        return imgDataList;
    }

    public void setImgDataList(List<ImgData> imgDataList) {
        this.imgDataList = imgDataList;
    }

    public List<Mp3Data> getMp3DataList() {
        return mp3DataList;
    }

    public void setMp3DataList(List<Mp3Data> mp3DataList) {
        this.mp3DataList = mp3DataList;
    }

    public List<Mp4Data> getMp4DataList() {
        return mp4DataList;
    }

    public void setMp4DataList(List<Mp4Data> mp4DataList) {
        this.mp4DataList = mp4DataList;
    }

    public String getCommonId() {
        return commonId;
    }

    public void setCommonId(String commonId) {
        this.commonId = commonId;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getMalId() {
        return malId;
    }

    public void setMalId(String malId) {
        this.malId = malId;
    }

    public String getEngId() {
        return engId;
    }

    public void setEngId(String engId) {
        this.engId = engId;
    }

    public List<CombinedDataSub> getCombinedDataSubList() {
        return combinedDataSubList;
    }

    public void setCombinedDataSubList(List<CombinedDataSub> combinedDataSubList) {
        this.combinedDataSubList = combinedDataSubList;
    }

    public byte[] getQrCodeImage() {
        return qrCodeImage;
    }

    public void setQrCodeImage(byte[] qrCodeImage) {
        this.qrCodeImage = qrCodeImage;
    }

    public List<BackgroundImg> getBackgroundImgList() {
        return backgroundImgList;
    }

    public void setBackgroundImgList(List<BackgroundImg> backgroundImgList) {
        this.backgroundImgList = backgroundImgList;
    }
}

