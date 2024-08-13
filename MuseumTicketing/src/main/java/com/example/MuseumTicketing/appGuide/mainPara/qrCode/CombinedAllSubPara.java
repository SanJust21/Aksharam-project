package com.example.MuseumTicketing.appGuide.mainPara.qrCode;

import com.example.MuseumTicketing.appGuide.audio.first.AudioFirst;
import com.example.MuseumTicketing.appGuide.img.first.ImgDataFirst;
import com.example.MuseumTicketing.appGuide.video.first.VideoFirst;
import lombok.Data;

import java.util.List;
@Data
public class CombinedAllSubPara {
    private String topic;
    private String description;
    private String reference;
    private String subUniqueId;
    private String mainUniqueId;
    private String SubCommonId;
    private String engUniqueId;
    private String malUniqueId;

    private List<ImgDataFirst>imgDataFirstList;
    private List<AudioFirst>audioFirstList;
    private List<VideoFirst>videoFirstList;

    public CombinedAllSubPara() {
    }

    public String getEngUniqueId() {
        return engUniqueId;
    }

    public void setEngUniqueId(String engUniqueId) {
        this.engUniqueId = engUniqueId;
    }

    public String getMalUniqueId() {
        return malUniqueId;
    }

    public void setMalUniqueId(String malUniqueId) {
        this.malUniqueId = malUniqueId;
    }

    public String getSubCommonId() {
        return SubCommonId;
    }

    public void setSubCommonId(String subCommonId) {
        SubCommonId = subCommonId;
    }

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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSubUniqueId() {
        return subUniqueId;
    }

    public void setSubUniqueId(String subUniqueId) {
        this.subUniqueId = subUniqueId;
    }

    public String getMainUniqueId() {
        return mainUniqueId;
    }

    public void setMainUniqueId(String mainUniqueId) {
        this.mainUniqueId = mainUniqueId;
    }

    public List<ImgDataFirst> getImgDataFirstList() {
        return imgDataFirstList;
    }

    public void setImgDataFirstList(List<ImgDataFirst> imgDataFirstList) {
        this.imgDataFirstList = imgDataFirstList;
    }

    public List<AudioFirst> getAudioFirstList() {
        return audioFirstList;
    }

    public void setAudioFirstList(List<AudioFirst> audioFirstList) {
        this.audioFirstList = audioFirstList;
    }

    public List<VideoFirst> getVideoFirstList() {
        return videoFirstList;
    }

    public void setVideoFirstList(List<VideoFirst> videoFirstList) {
        this.videoFirstList = videoFirstList;
    }
}

