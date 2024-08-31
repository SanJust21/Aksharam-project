package com.example.MuseumTicketing.tribal;

import com.example.MuseumTicketing.tribal.tribalVideo.TribalVideo;
import lombok.Data;

import java.util.List;

@Data
public class CombinedTribalData {
    private String title;
    private String description;
    private String uniqueId;

    private String malayalamId;
    private String englishId;
    private String tribalCommonId;

    private List<TribalVideo> tribalVideoList;

    public CombinedTribalData() {
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public List<TribalVideo> getTribalVideoList() {
        return tribalVideoList;
    }
}
