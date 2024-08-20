package com.example.MuseumTicketing.appGuide.mainPara.qrCode;

import com.example.MuseumTicketing.appGuide.Pdf.PdfData;
import com.example.MuseumTicketing.appGuide.audio.main.AudioMain;
import com.example.MuseumTicketing.appGuide.img.main.ImgDataMain;
import com.example.MuseumTicketing.appGuide.video.main.VideoMain;
import lombok.Data;

import java.util.List;
@Data
public class CombinedGetAllPara {

    private String topic;
    private String description;
    private String ref;
    private String mainUniqueId;
    private String mainCommonId;
    private String engUniqueId;
    private String malUniqueId;
    private String qrCodeUrl;
    private byte[] qrCodeImage;

    private List<CombinedAllSubPara> combinedAllSubParaList;
    private List<ImgDataMain>imgDataMainList;
    private List<AudioMain>audioMainList;
    private List<VideoMain>videoMainList;
    private List<PdfData>pdfDataList;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getMainUniqueId() {
        return mainUniqueId;
    }

    public void setMainUniqueId(String mainUniqueId) {
        this.mainUniqueId = mainUniqueId;
    }

    public String getMainCommonId() {
        return mainCommonId;
    }

    public void setMainCommonId(String mainCommonId) {
        this.mainCommonId = mainCommonId;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public byte[] getQrCodeImage() {
        return qrCodeImage;
    }

    public void setQrCodeImage(byte[] qrCodeImage) {
        this.qrCodeImage = qrCodeImage;
    }

    public List<CombinedAllSubPara> getCombinedAllSubParaList() {
        return combinedAllSubParaList;
    }

    public void setCombinedAllSubParaList(List<CombinedAllSubPara> combinedAllSubParaList) {
        this.combinedAllSubParaList = combinedAllSubParaList;
    }

    public List<ImgDataMain> getImgDataMainList() {
        return imgDataMainList;
    }

    public void setImgDataMainList(List<ImgDataMain> imgDataMainList) {
        this.imgDataMainList = imgDataMainList;
    }

    public List<AudioMain> getAudioMainList() {
        return audioMainList;
    }

    public void setAudioMainList(List<AudioMain> audioMainList) {
        this.audioMainList = audioMainList;
    }

    public List<VideoMain> getVideoMainList() {
        return videoMainList;
    }

    public void setVideoMainList(List<VideoMain> videoMainList) {
        this.videoMainList = videoMainList;
    }

    public List<PdfData> getPdfDataList() {
        return pdfDataList;
    }
}
