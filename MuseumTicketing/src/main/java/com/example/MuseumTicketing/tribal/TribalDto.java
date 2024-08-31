package com.example.MuseumTicketing.tribal;

import lombok.Data;

@Data
public class TribalDto {
    private String title;
    private String description;

    public TribalDto() {

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
