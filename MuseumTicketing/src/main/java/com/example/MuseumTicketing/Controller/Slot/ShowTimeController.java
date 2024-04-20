package com.example.MuseumTicketing.Controller.Slot;

import com.example.MuseumTicketing.DTO.Slot.ShowTimeDTO;
import com.example.MuseumTicketing.Model.ShowTime;
import com.example.MuseumTicketing.Service.Slot.ShowTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/stime")
@CrossOrigin
public class ShowTimeController {
    @Autowired
    private ShowTimeService showTimeService;

    @PostMapping(path = "/addstime")
    public ResponseEntity<?> addShowTime(@RequestBody ShowTimeDTO showTimeDTO) {
        ShowTime showTime = showTimeService.addShowTime(showTimeDTO);
        return ResponseEntity.ok(showTime);
    }

    @GetMapping(path = "/getSlot")
    public ResponseEntity<List<ShowTime>> getAllShowTime() {
        return showTimeService.getAllShowTime();
    }

    @PutMapping(path = "/updateShow/{id}")
    public ResponseEntity<ShowTime> updateShowTime(@PathVariable("id") Integer id, @RequestBody ShowTimeDTO updateShowTimeDTO) {
        ShowTime updatedShowTime = showTimeService.updateShowTime(id, updateShowTimeDTO);

        if (updatedShowTime != null) {
            return ResponseEntity.ok(updatedShowTime);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}