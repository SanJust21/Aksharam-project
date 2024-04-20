package com.example.MuseumTicketing.Controller.Slot;

import com.example.MuseumTicketing.Model.CalendarEvent;
import com.example.MuseumTicketing.Repo.CalendarRepo;
import com.example.MuseumTicketing.Service.Slot.CalendarEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/calEve")
@CrossOrigin
@Slf4j
public class CalendarEventController {
    @Autowired
    private CalendarEventService calendarEventService;
    //@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate selectedDate
    @Autowired
    private CalendarRepo calendarRepo;

    @GetMapping(path = "/eventCal")
    public ResponseEntity<List<CalendarEvent>> createCalendar(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            return calendarEventService.createCalendar(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}