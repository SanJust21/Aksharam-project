package com.example.MuseumTicketing.spotReg.userData.dashboardDTO;

import lombok.Data;

import java.time.LocalTime;

@Data
public class SlotIdDto {
    private Integer slotId;
    private Integer count;

    public SlotIdDto(Integer slotId, Integer count) {
        this.slotId = slotId;
        this.count = count;
    }
}
