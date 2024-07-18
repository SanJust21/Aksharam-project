package com.example.MuseumTicketing.DTO.Slot;
import com.example.MuseumTicketing.Model.ShowTime;
import com.example.MuseumTicketing.Model.ShowTime;
import lombok.Data;

import java.util.List;

@Data
public class ShowTimeResponse {

    private List<ShowTime> showTimes;

    public ShowTimeResponse(List<ShowTime> showTimes) {
        this.showTimes = showTimes;
    }

    public List<ShowTime> getShowTimes() {
        return showTimes;
    }

    public void setShowTimes(List<ShowTime> showTimes) {
        this.showTimes = showTimes;
    }

}
