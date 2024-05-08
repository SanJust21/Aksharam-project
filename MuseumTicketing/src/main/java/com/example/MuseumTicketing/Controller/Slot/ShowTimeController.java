package com.example.MuseumTicketing.Controller.Slot;

import com.example.MuseumTicketing.DTO.AdminScanner.CustomResponse;
import com.example.MuseumTicketing.DTO.Slot.ShowTimeDTO;
import com.example.MuseumTicketing.Model.ShowTime;
import com.example.MuseumTicketing.Service.Slot.ShowTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/stime")
@CrossOrigin
public class ShowTimeController {
    @Autowired
    private ShowTimeService showTimeService;

    @PostMapping(path = "/addstime")
    public ResponseEntity<?> addShowTime(@RequestBody ShowTimeDTO showTimeDTO) {
        try {
            ShowTime showTime = showTimeService.addShowTime(showTimeDTO);
            return ResponseEntity.ok(showTime);
        } catch (IllegalArgumentException e) {
            // Handle the error here
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PostMapping(path = "/addSlots")
    public ResponseEntity<List<Integer>> addSlots(@RequestBody List<ShowTimeDTO> slotDTOs) {
        List<Integer> slotIds = new ArrayList<>();
        for (ShowTimeDTO slotDTO : slotDTOs) {
            ShowTime showTime = showTimeService.addShowTime(slotDTO);
            slotIds.add(showTime.getId());
        }
        return ResponseEntity.ok(slotIds);
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