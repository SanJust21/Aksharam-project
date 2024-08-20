package com.example.MuseumTicketing.appGuide.mainPara;

import com.example.MuseumTicketing.appGuide.Pdf.PdfData;
import com.example.MuseumTicketing.appGuide.audio.main.AudioMain;
import com.example.MuseumTicketing.appGuide.img.main.ImgDataMain;
import com.example.MuseumTicketing.appGuide.mainPara.first.CombinedSubPara;
import com.example.MuseumTicketing.appGuide.video.main.VideoMain;
import lombok.Data;

import java.util.List;

@Data
public class CombinedPara {
    private String topic;
    private String description;
    private String referenceUrl;
    private String uId;
    private String commonId;
    private String qrCodeUrl;
    private byte[] qrCodeImage;
    private String malId;
    private String engId;

    private List<ImgDataMain> imgList;
    private List<PdfData> pdfDataList;
    private List<AudioMain> audioList;
    private List<VideoMain> videoList;

    private List<CombinedSubPara> combinedDataSubList;
}
