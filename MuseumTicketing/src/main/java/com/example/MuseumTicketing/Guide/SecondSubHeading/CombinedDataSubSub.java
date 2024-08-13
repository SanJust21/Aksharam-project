package com.example.MuseumTicketing.Guide.SecondSubHeading;

import com.example.MuseumTicketing.Guide.img.backgroundImg.BackgroundImg;
import com.example.MuseumTicketing.Guide.img.secondSubHeading.ImgSubSecond;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.secondSub.Mp3Data2;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.secondSub.Mp4Data2;
import lombok.Data;

import java.util.List;
@Data
public class CombinedDataSubSub {
    private String title;
    private String description;
    private String referenceUrl;
    private String uId;
    private String mUid;
    private String ssCommonId;
    private String ssEngId;
    private String ssMalId;
    private String fsCommonId;


    private List<ImgSubSecond> imgData2List;
    private List<BackgroundImg> backgroundImgList;
    private List<Mp3Data2> mp3Data2List;
    private List<Mp4Data2> mp4Data2List;

    public CombinedDataSubSub() {
    }

    public String getFsCommonId() {
        return fsCommonId;
    }

    public void setFsCommonId(String fsCommonId) {
        this.fsCommonId = fsCommonId;
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

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }

    public List<BackgroundImg> getBackgroundImgList() {
        return backgroundImgList;
    }

    public void setBackgroundImgList(List<BackgroundImg> backgroundImgList) {
        this.backgroundImgList = backgroundImgList;
    }
}
