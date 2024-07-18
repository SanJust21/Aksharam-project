package com.example.MuseumTicketing.appGuide.mainPara.first;

import com.example.MuseumTicketing.appGuide.audio.first.AudioFirst;
import com.example.MuseumTicketing.appGuide.img.first.ImgDataFirst;
import com.example.MuseumTicketing.appGuide.video.first.VideoFirst;
import lombok.Data;

import java.util.List;

@Data
public class CombinedSubPara {
    private String topic;
    private String description;
    private String referenceUrl;
    private String uId;
    private String mUid;
    private String fsCommonId;
    private String fsEngId;
    private String fsMalId;

    private List<ImgDataFirst> imgList;
    private List<VideoFirst> videoList;
    private List<AudioFirst> audioList;

}
