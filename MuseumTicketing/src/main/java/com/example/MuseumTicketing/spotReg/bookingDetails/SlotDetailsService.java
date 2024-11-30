package com.example.MuseumTicketing.spotReg.bookingDetails;

import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingDetails;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingDetailsDto;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingSpotRepo;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SlotSpotDto;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SpotSlot;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SpotSlotGetDto;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SpotSlotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SlotDetailsService {
    @Autowired
    private SpotSlotRepo spotSlotRepo;
    @Autowired
    private BookingSpotRepo bookingSpotRepo;

    public ResponseEntity<?> addSlotDetails(SpotSlot spotSlot) {
        SpotSlot spotDto = new SpotSlot();
        LocalTime newStart = spotSlot.getSlotStartTime();
        LocalTime newEnd = spotSlot.getSlotEndTime();
        spotDto.setSlotStartTime(newStart.minusHours(5).minusMinutes(30));
        spotDto.setSlotEndTime(newEnd.minusHours(5).minusMinutes(30));
        spotDto.setTotalCapacity(spotSlot.getTotalCapacity());
        spotDto.setStatus(spotSlot.getStatus());
        spotSlotRepo.save(spotDto);
        return new ResponseEntity<>("Start Time : "+newStart+" and End Time : "+newEnd+" slot added successfully+", HttpStatus.OK);
    }

    public ResponseEntity<List<SpotSlotGetDto>> getAllSlotDetails() {
        List<SpotSlotGetDto> spotSlotGetDtoList = new ArrayList<>();
        List<SpotSlot> spotSlotList = spotSlotRepo.findAll();
        spotSlotList.sort(Comparator.comparing(SpotSlot::getId));
        for (SpotSlot spotSlot : spotSlotList){
            SpotSlotGetDto spotSlotGetDto = new SpotSlotGetDto();
            LocalTime newStart = spotSlot.getSlotStartTime();
            LocalTime newEnd = spotSlot.getSlotEndTime();
            spotSlotGetDto.setSlotId(spotSlot.getId());
            spotSlotGetDto.setSlotStartTime(newStart.plusHours(5).plusMinutes(30));
            spotSlotGetDto.setSlotEndTime(newEnd.plusHours(5).plusMinutes(30));
            spotSlotGetDto.setCapacity(spotSlot.getTotalCapacity());
            spotSlotGetDto.setStatus(spotSlot.getStatus());
            spotSlotGetDtoList.add(spotSlotGetDto);
        }
        return new ResponseEntity<>(spotSlotGetDtoList,HttpStatus.OK);
    }

    public ResponseEntity<?> updateSlotDetails(SlotSpotDto spotSlotDto, Integer id) {
        Optional<SpotSlot> spotSlotOptional =spotSlotRepo.findById(id);
        if (spotSlotOptional.isPresent()){
            SpotSlot spotSlot = spotSlotOptional.get();
            LocalTime newStart = spotSlotDto.getSlotStartTime();
            LocalTime newEnd = spotSlotDto.getSlotEndTime();
            spotSlot.setSlotStartTime(newStart.minusHours(5).minusMinutes(30));
            spotSlot.setSlotEndTime(newEnd.minusHours(5).minusMinutes(30));
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
//                    LocalTime nowTime =LocalTime.now();
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

    public ResponseEntity<?> updateCapacityByDateAndSlotId(LocalDate bookDate, Integer slotId, BookingDetailsDto bookingDetailsDto) {
        Optional<BookingDetails> bookingDetailsOptional = bookingSpotRepo.findByBookDateAndSlotId(bookDate,slotId);
        if (bookingDetailsOptional.isPresent()){
            BookingDetails bookingDetails = bookingDetailsOptional.get();
            LocalTime newStart = bookingDetailsDto.getSlotStartTime();
            LocalTime newStartUtc = newStart.minusHours(5).minusMinutes(30);
            bookingDetails.setSlotStartTime(newStartUtc);
            LocalTime newEnd = bookingDetails.getSlotEndTime();
            LocalTime newEndUtc = newEnd.minusHours(5).minusMinutes(30);
            bookingDetails.setSlotEndTime(newEndUtc);
            bookingDetails.setPresentCapacity(bookingDetailsDto.getPresentCapacity());
            bookingDetails.setPresentStatus(bookingDetailsDto.getPresentStatus());
            bookingSpotRepo.save(bookingDetails);
            return new ResponseEntity<>(bookingDetails,HttpStatus.OK);
        }
        return new ResponseEntity<>("Date : "+bookDate+" and slotId : "+slotId+" are not matching",HttpStatus.BAD_REQUEST);
    }
}
