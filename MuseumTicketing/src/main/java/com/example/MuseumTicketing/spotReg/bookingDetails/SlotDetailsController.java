package com.example.MuseumTicketing.spotReg.bookingDetails;

import com.example.MuseumTicketing.Guide.util.ErrorService;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingDetails;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingDetailsDto;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingSpotRepo;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SlotSpotDto;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SpotSlot;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SpotSlotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/slot")
public class SlotDetailsController {
    @Autowired
    private ErrorService errorService;
    @Autowired
    private SlotDetailsService slotDetailsService;
    @Autowired
    private SpotSlotRepo spotSlotRepo;
    @Autowired
    private BookingSpotRepo bookingSpotRepo;

    @PostMapping(path = "/addSlot")
    public ResponseEntity<?>addSlotDetails(@RequestBody SpotSlot spotSlot){
        try {
            return slotDetailsService.addSlotDetails(spotSlot);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @GetMapping(path = "/getAllSlot")
    public ResponseEntity<List<SpotSlot>> getAllSlot(){
        try {
            return slotDetailsService.getAllSlotDetails();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path = "/updateSlot")
    public ResponseEntity<?>updateSlotDetails(@RequestBody SlotSpotDto spotSlotDto, @RequestParam Integer id){
        try {
           return slotDetailsService.updateSlotDetails(spotSlotDto,id);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @PutMapping(path = "/updateSlotCapacity")
    public ResponseEntity<?>updateCapacityStatus(@RequestParam Integer id,@RequestBody SlotSpotDto slotSpotDto){
        try {
            return slotDetailsService.updateCapacityStatus(id,slotSpotDto);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @DeleteMapping(path = "/deleteSlot/{id}")
    public ResponseEntity<?>deleteSlotById(@PathVariable Integer id){
        try {
            return slotDetailsService.deleteSlot(id);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @GetMapping(path = "/bookDate")
    public ResponseEntity<?>getDateAndSlot(@RequestParam LocalDate bDate){
        try {
            return slotDetailsService.generateDateAndSlot(bDate);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @PutMapping(path = "/updateCapacityByDateAndSlotId")
    public ResponseEntity<?>updateCapacityByDateAndSlotId(@RequestParam LocalDate bookDate,
                                                          @RequestParam Integer slotId, @RequestBody BookingDetailsDto bookingDetailsDto){
        try {
            return slotDetailsService.updateCapacityByDateAndSlotId(bookDate,slotId,bookingDetailsDto);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

}
