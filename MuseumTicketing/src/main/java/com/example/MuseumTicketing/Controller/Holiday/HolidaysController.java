package com.example.MuseumTicketing.Controller.Holiday;
import com.example.MuseumTicketing.Model.Holidays;
import com.example.MuseumTicketing.Service.Holidays.HolidaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/holidays")
public class HolidaysController {

    @Autowired
    private HolidaysService holidaysService;

    @PostMapping(path = "addDayData")
    public ResponseEntity<?> addHoliDays(@RequestBody Holidays holiDays){
        Holidays holiDays1 = holidaysService.addHolidays(holiDays);
        return ResponseEntity.ok(holiDays1);
    }

    @GetMapping(path = "/getDayList")
    public ResponseEntity<List<Holidays>> getAllHoliDays(){

        return holidaysService.getAllHoliDays();
    }

    @PutMapping(path = "/updateDate/{id}")
    public ResponseEntity<?> updateHolidaysById(@PathVariable("id") Integer id,
                                                @RequestBody Holidays updateDays){
        Holidays existingDays = holidaysService.updateHolidaysById(id,updateDays);
        if (existingDays!=null){
            return ResponseEntity.ok(existingDays);
        }else {
            return ResponseEntity.notFound().build();
        }

    }

}