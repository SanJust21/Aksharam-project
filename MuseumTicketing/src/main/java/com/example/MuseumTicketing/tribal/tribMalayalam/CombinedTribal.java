package com.example.MuseumTicketing.tribal.tribMalayalam;

import lombok.Data;

@Data
public class CombinedTribal {
    private String title;
    private String description;
    private String uniqueId;

    public CombinedTribal() {
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
}
