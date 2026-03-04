package com.example.MuseumTicketing.spotReg.bookingDetails;

import com.example.MuseumTicketing.onlineTicket.foreginer.ForeignerUserOnline;
import com.example.MuseumTicketing.onlineTicket.foreginer.ForeignerUserOnlineRepository;
import com.example.MuseumTicketing.onlineTicket.institutionUser.InstitutionUserOnline;
import com.example.MuseumTicketing.onlineTicket.institutionUser.InstitutionUserOnlineRepository;
import com.example.MuseumTicketing.onlineTicket.publicUser.PublicUserOnline;
import com.example.MuseumTicketing.onlineTicket.publicUser.PublicUserOnlineRepository;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingDetails;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingDetailsDto;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingSlotDto;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingSpotRepo;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SlotSpotDto;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SpotSlot;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SpotSlotRepo;
import com.example.MuseumTicketing.spotReg.category.paymentStatus.PaymentStatus;
import com.example.MuseumTicketing.spotReg.category.paymentStatus.PaymentStatusRepo;
import com.example.MuseumTicketing.spotReg.userData.Institution.InstitutionData;
import com.example.MuseumTicketing.spotReg.userData.Institution.InstitutionDataRepo;
import com.example.MuseumTicketing.spotReg.userData.foreigner.ForeignerData;
import com.example.MuseumTicketing.spotReg.userData.foreigner.ForeignerDataRepo;
import com.example.MuseumTicketing.spotReg.userData.publicUser.PublicData;
import com.example.MuseumTicketing.spotReg.userData.publicUser.PublicRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    @Autowired
    private PublicUserOnlineRepository pUserOnlineRepository;
    @Autowired
    private InstitutionUserOnlineRepository iUserOnlineRepository;
    @Autowired
    private ForeignerUserOnlineRepository fUserOnlineRepository;
    @Value("${onlineTicket.lockTime.interval.minutes}")
    private Integer onlineTimeInterval;

    @Value("${spotTicket.lockTime.interval.minutes}")
    private Integer spotTimeInterval;
    @Autowired
    private PublicRepo publicRepo;
    @Autowired
    private InstitutionDataRepo institutionDataRepo;
    @Autowired
    private ForeignerDataRepo foreignerDataRepo;
    @Autowired
    private PaymentStatusRepo paymentStatusRepo;
    public ResponseEntity<?> addSlotDetails(SpotSlot spotSlot) {

        LocalTime sStartTime = spotSlot.getSlotStartTime();
        LocalTime sEndTime = spotSlot.getSlotEndTime();
        Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findBySlotStartTimeAndSlotEndTime(sStartTime,sEndTime);
        if (spotSlotOptional.isPresent()){
            return new ResponseEntity<>("StartTime : "+sStartTime+" and EndTime : "+sEndTime+" are already existing.",HttpStatus.CONFLICT);
        }else {
            SpotSlot slot = new SpotSlot();
            slot.setSlotStartTime(sStartTime);
            slot.setSlotEndTime(sEndTime);
            slot.setTotalCapacity(spotSlot.getTotalCapacity());
            slot.setStatus(spotSlot.getStatus());
            spotSlotRepo.save(slot);
            return new ResponseEntity<>(slot,HttpStatus.OK);
        }
    }

    public ResponseEntity<List<SpotSlot>> getAllSlotDetails() {
        List<SpotSlot> spotSlotList = spotSlotRepo.findAll();
        spotSlotList.sort(Comparator.comparing(SpotSlot::getId));
        return new ResponseEntity<>(spotSlotList,HttpStatus.OK);
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

    public ResponseEntity<?> generateDateAndSlot(LocalDate bDate,String modeType) {
        bookingSpotRepo.deleteByBookDateBefore(LocalDate.now());
        String messageId = null;
        if (modeType==null || modeType.isBlank()){
            modeType = "SpotBooking";
        }
        if (bDate==null){
            messageId = "bookDate is required.";
            return new ResponseEntity<>(messageId,HttpStatus.BAD_REQUEST);
        }
        List<BookingDetails> bookingDetailsList = bookingSpotRepo.findByBookDate(bDate);
        if (bookingDetailsList.isEmpty()){
            List<SpotSlot> spotSlotList = spotSlotRepo.findAll();
            if (spotSlotList.isEmpty()){
                return new ResponseEntity<>("Create slot at least one.!",HttpStatus.NOT_FOUND);
            }
            for (SpotSlot slot : spotSlotList){
                BookingDetails bookingDetails = new BookingDetails();
                bookingDetails.setBookDate(bDate);
                bookingDetails.setSlotId(slot.getId());
                bookingDetails.setSlotStartTime(slot.getSlotStartTime());
                bookingDetails.setSlotEndTime(slot.getSlotEndTime());
                bookingDetails.setPresentCapacity(slot.getTotalCapacity());
                bookingDetails.setTotalCapacity(slot.getTotalCapacity());
                bookingDetails.setPresentStatus(slot.getStatus());
                bookingSpotRepo.save(bookingDetails);
            }
            return new ResponseEntity<>(getSlotDetailsWithTheDateAndModeType(bDate,modeType),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(getSlotDetailsWithTheDateAndModeType(bDate,modeType),HttpStatus.OK);

        }
    }

    private ResponseEntity<?> getSlotDetailsWithTheDateAndModeType(LocalDate bDate, String modeType) {
        LocalDate currentDate = LocalDate.now();
        List<BookingDetails> bookingDetailsList = bookingSpotRepo.findByBookDate(bDate);
        List<BookingSlotDto> slotDtoList = new ArrayList<>();
        String messageId=null;
        if ("Online".equalsIgnoreCase(modeType)){
            bookingDetailsList.sort(Comparator.comparing(BookingDetails::getSlotStartTime)); //sort slot based on start time
            if (currentDate.isEqual(bDate)&& !bookingDetailsList.isEmpty()){ // in Response : runningSlotDetails + upComingSlotDetails.
                //bookDate == visitDate
                for (BookingDetails bDetails:bookingDetailsList){
                    if (bDetails.getSlotEndTime().isAfter(LocalTime.now())){
                        BookingSlotDto slotDto = new BookingSlotDto();
                        slotDto.setSlotId(bDetails.getSlotId());
                        slotDto.setBookDate(bDetails.getBookDate());
                        slotDto.setSlotStartTime(bDetails.getSlotStartTime());
                        slotDto.setSlotEndTime(bDetails.getSlotEndTime());
                        slotDto.setPresentCapacity(bDetails.getPresentCapacity());
                        slotDto.setTotalCapacity(bDetails.getTotalCapacity());
                        slotDto.setPresentStatus(bDetails.getPresentStatus());
                        slotDtoList.add(slotDto);
                    }
                }
                return new ResponseEntity<>(slotDtoList,HttpStatus.OK);

            }else {
                // in bDate Response(upcomingDate) : all slots are upComing, allSlotDetails.
                //visitDate and bookDate are different.
                if (!bookingDetailsList.isEmpty()){
                    for (BookingDetails bDetails:bookingDetailsList){
                        BookingSlotDto slotDto = new BookingSlotDto();
                        slotDto.setSlotId(bDetails.getSlotId());
                        slotDto.setBookDate(bDetails.getBookDate());
                        slotDto.setSlotStartTime(bDetails.getSlotStartTime());
                        slotDto.setSlotEndTime(bDetails.getSlotEndTime());
                        slotDto.setPresentCapacity(bDetails.getPresentCapacity());
                        slotDto.setTotalCapacity(bDetails.getTotalCapacity());
                        slotDto.setPresentStatus(bDetails.getPresentStatus());
                        slotDtoList.add(slotDto);
                    }

                }
                return new ResponseEntity<>(slotDtoList,HttpStatus.OK);
            }
        }

        if ("SpotBooking".equalsIgnoreCase(modeType)){
            LocalTime nowTime = LocalTime.now();
            if (currentDate.isEqual(bDate)&& !bookingDetailsList.isEmpty()){
                for (BookingDetails bDetails:bookingDetailsList){
                    if (!nowTime.isBefore(bDetails.getSlotStartTime()) && nowTime.isBefore(bDetails.getSlotEndTime())){
                        BookingSlotDto slotDto = new BookingSlotDto();
                        slotDto.setSlotId(bDetails.getSlotId());
                        slotDto.setBookDate(bDetails.getBookDate());
                        slotDto.setSlotStartTime(bDetails.getSlotStartTime());
                        slotDto.setSlotEndTime(bDetails.getSlotEndTime());
                        slotDto.setPresentCapacity(bDetails.getPresentCapacity());
                        slotDto.setTotalCapacity(bDetails.getTotalCapacity());
                        slotDto.setPresentStatus(bDetails.getPresentStatus());
                        return new ResponseEntity<>(slotDto,HttpStatus.OK);
                    }
                }
                messageId = "No slot is available at the present time. : "+nowTime;
                return new ResponseEntity<>(messageId,HttpStatus.NOT_FOUND);
            }
        }
        messageId = "modeType is not correct. : "+modeType;
        return new ResponseEntity<>(messageId,HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> updateCapacityByDateAndSlotId(LocalDate bookDate, Integer slotId, BookingDetailsDto bookingDetailsDto) {
        Optional<BookingDetails> bookingDetailsOptional = bookingSpotRepo.findByBookDateAndSlotIdForUpdate(bookDate,slotId);
        if (bookingDetailsOptional.isPresent()){
            BookingDetails bookingDetails = bookingDetailsOptional.get();
            bookingDetails.setSlotStartTime(bookingDetailsDto.getSlotStartTime());
            bookingDetails.setSlotEndTime(bookingDetailsDto.getSlotEndTime());
            bookingDetails.setPresentCapacity(bookingDetailsDto.getPresentCapacity());
            bookingDetails.setPresentStatus(bookingDetailsDto.getPresentStatus());
            bookingSpotRepo.save(bookingDetails);
            return new ResponseEntity<>(bookingDetails,HttpStatus.OK);
        }
        return new ResponseEntity<>("Date : "+bookDate+" and slotId : "+slotId+" are not matching",HttpStatus.BAD_REQUEST);
    }
}
