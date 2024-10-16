package com.example.MuseumTicketing.spotReg.bookingDetails;

import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingDetails;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingSpotRepo;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SlotSpotDto;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SpotSlot;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SpotSlotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class SlotDetailsService {
    @Autowired
    private SpotSlotRepo spotSlotRepo;
    @Autowired
    private BookingSpotRepo bookingSpotRepo;

    public ResponseEntity<?> addSlotDetails(SpotSlot spotSlot) {
        return new ResponseEntity<>(spotSlotRepo.save(spotSlot), HttpStatus.OK);
    }

    public ResponseEntity<List<SpotSlot>> getAllSlotDetails() {
        return new ResponseEntity<>(spotSlotRepo.findAll(),HttpStatus.OK);
    }

    public ResponseEntity<?> updateSlotDetails(SlotSpotDto spotSlotDto, Integer id) {
        Optional<SpotSlot> spotSlotOptional =spotSlotRepo.findById(id);
        if (spotSlotOptional.isPresent()){
            SpotSlot spotSlot = spotSlotOptional.get();
            spotSlot.setSlotStartTime(spotSlotDto.getSlotStartTime());
            spotSlot.setSlotEndTime(spotSlotDto.getSlotEndTime());
            spotSlot.setStatus(spotSlotDto.getStatus());
            spotSlot.setTotalCapacity(spotSlotDto.getCapacity());
            spotSlotRepo.save(spotSlot);
            return new ResponseEntity<>(spotSlot,HttpStatus.OK);
        }return new ResponseEntity<>("id isn't valid",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> updateCapacityStatus(Integer id, SlotSpotDto slotSpotDto) {
        Optional<SpotSlot> spotSlotOptional =spotSlotRepo.findById(id);
        if (spotSlotOptional.isPresent()){
            SpotSlot spotSlot = spotSlotOptional.get();
            spotSlot.setStatus(slotSpotDto.getStatus());
            spotSlot.setTotalCapacity(slotSpotDto.getCapacity());
            spotSlotRepo.save(spotSlot);
            return new ResponseEntity<>(spotSlot,HttpStatus.OK);
        }return new ResponseEntity<>("Id isn't valid",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> deleteSlot(Integer id) {
        Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(id);
        if (spotSlotOptional.isPresent()){
            SpotSlot spotSlot = spotSlotOptional.get();
            LocalTime sTime = spotSlot.getSlotStartTime();
           spotSlotRepo.delete(spotSlot);
           return new ResponseEntity<>(sTime+"  deleted",HttpStatus.OK);
        }
        return new ResponseEntity<>("Id isn't valid",HttpStatus.BAD_REQUEST);
    }






    public ResponseEntity<?> generateDateAndSlot(LocalDate bDate) {
        List<BookingDetails> bookingDetailsList = bookingSpotRepo.findByBookDate(bDate);
        if (!bookingDetailsList.isEmpty()){
            for (BookingDetails bookingDetails : bookingDetailsList){
                Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(bookingDetails.getSlotId());
                if (spotSlotOptional.isPresent()){
                    SpotSlot spotSlot = spotSlotOptional.get();
                    LocalTime now = LocalTime.now();
                    if (bookingDetails.getBookDate().isEqual(bDate)&& now.isAfter(spotSlot.getSlotStartTime()) && now.isBefore(spotSlot.getSlotEndTime())){
                        return new ResponseEntity<>(bookingDetails,HttpStatus.OK);
                    }else {
                        List<SpotSlot> spotSlotList = spotSlotRepo.findAll();
                        if (!spotSlotList.isEmpty()){
                            for (SpotSlot slot : spotSlotList){
                                if (now.isAfter(slot.getSlotStartTime()) && now.isBefore(slot.getSlotEndTime())){
                                    bookingDetails.setSlotId(slot.getId());
                                    bookingDetails.setSlotStartTime(slot.getSlotStartTime());
                                    bookingDetails.setSlotEndTime(slot.getSlotEndTime());
                                    bookingDetails.setTotalCapacity(slot.getTotalCapacity());
                                    bookingDetails.setPresentStatus(slot.getStatus());
                                    bookingDetails.setPresentCapacity(slot.getTotalCapacity());
                                    bookingSpotRepo.save(bookingDetails);
                                    return new ResponseEntity<>(bookingDetails,HttpStatus.CREATED);
                                }
                            }
                        }
                    }
                }
            }
        }
//        Optional<BookingDetails> bookingDetailsOptional = bookingSpotRepo.findByBookDate(bDate);
//        if (bookingDetailsOptional.isPresent()){
//            BookingDetails bookingDetails = bookingDetailsOptional.get();
//            LocalTime now = LocalTime.now();
//            Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(bookingDetails.getSlotId());
//            if (spotSlotOptional.isPresent()){
//                SpotSlot spotSlot = spotSlotOptional.get();
//                if (bookingDetails.getBookDate().isEqual(bDate)&&now.isAfter(spotSlot.getSlotStartTime())&&now.isBefore(spotSlot.getSlotEndTime())){
//                    return new ResponseEntity<>(bookingDetails,HttpStatus.OK);
//                }else {
//                    List<SpotSlot> spotSlotList = spotSlotRepo.findAll();
//                    if (!spotSlotList.isEmpty()){
//                        for (SpotSlot spotSlots:spotSlotList){
//                            if (now.isAfter(spotSlots.getSlotStartTime()) && now.isBefore(spotSlots.getSlotStartTime())){
//                                bookingDetails.setSlotId(spotSlots.getId());
//                                bookingDetails.setTotalCapacity(spotSlots.getTotalCapacity());
//                                bookingDetails.setPresentCapacity(spotSlots.getTotalCapacity());
//                                bookingDetails.setPresentStatus(spotSlots.getStatus());
//                                bookingSpotRepo.save(bookingDetails);
//                                return new ResponseEntity<>(bookingDetails,HttpStatus.OK);
//                            }
//                        }
//                    }
//                }
//            }
//
//        }
        BookingDetails bookingDetails = new BookingDetails();
        bookingDetails.setBookDate(bDate);
        LocalTime now = LocalTime.now();
        List<SpotSlot> spotSlotList = spotSlotRepo.findAll();
        if (!spotSlotList.isEmpty()){
            for (SpotSlot spotSlot : spotSlotList){
                if (now.isAfter(spotSlot.getSlotStartTime()) && now.isBefore(spotSlot.getSlotEndTime())){
                    bookingDetails.setSlotId(spotSlot.getId());
                    bookingDetails.setSlotStartTime(spotSlot.getSlotStartTime());
                    bookingDetails.setSlotEndTime(spotSlot.getSlotEndTime());
                    bookingDetails.setTotalCapacity(spotSlot.getTotalCapacity());
                    bookingDetails.setPresentCapacity(spotSlot.getTotalCapacity());
                    bookingDetails.setPresentStatus(spotSlot.getStatus());
                    bookingSpotRepo.save(bookingDetails);
                    return new ResponseEntity<>(bookingDetails,HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("No slots are available at "+now,HttpStatus.NO_CONTENT);
    }
}
