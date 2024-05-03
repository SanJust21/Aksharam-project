package com.example.MuseumTicketing.Controller.Holiday;
import com.example.MuseumTicketing.DTO.AdminScanner.CustomResponse;
import com.example.MuseumTicketing.Model.Holidays;
import com.example.MuseumTicketing.Service.Holidays.HolidaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/api/holidays")
@CrossOrigin
public class HolidaysController {

    @Autowired
    private HolidaysService holidaysService;

    @PostMapping(path = "/addDayData")
    public ResponseEntity<?> addHoliDays(@RequestBody Holidays holiDays){

        if (Objects.isNull(holiDays.getName()) || Objects.isNull(holiDays.getDate()) || holiDays.getName().isEmpty() || holiDays.getDate().toString().isEmpty()) {
            return ResponseEntity.badRequest().body(new CustomResponse("Name and date fields cannot be null or empty!", HttpStatus.BAD_REQUEST.value()));
        }

        Holidays holiDays1 = holidaysService.addHolidays(holiDays);
        if(holiDays != null) {
            return ResponseEntity.ok(holiDays1);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse("Error while saving holidays!", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
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
    @DeleteMapping("/deleteDate/{id}")
    public void deletePrice(@PathVariable Integer id) {
        holidaysService.deleteHolidaysById(id);
    }

}