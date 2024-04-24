package com.example.MuseumTicketing.Controller.Slot;

import com.example.MuseumTicketing.Service.Slot.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping(path = "api/booking")
@CrossOrigin
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping(path = "/lock")
    //@CrossOrigin(origins = "http://localhost:8081")
    public ResponseEntity<Integer> lockCapacity(@RequestParam Integer capacity , @RequestParam LocalDate visitDate,
                                                @RequestParam LocalTime slotName, @RequestParam String category){
        try {

            Integer bookingId1 = bookingService.lockCapacity(capacity,visitDate,slotName,category);
            bookingService.confirmBooking(bookingId1);

            return ResponseEntity.ok(bookingId1);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Integer.valueOf(0));
        }
    }
}