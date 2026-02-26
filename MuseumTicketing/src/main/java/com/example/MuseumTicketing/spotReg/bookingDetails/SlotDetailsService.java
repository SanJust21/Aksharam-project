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
        String messageId = null;
        if (modeType==null || modeType.isEmpty() || modeType.isBlank()){
            messageId = "modeType is required.";
            return new ResponseEntity<>(messageId,HttpStatus.NOT_FOUND);
        }
        if ("Online".equalsIgnoreCase(modeType)){
            List<BookingDetails> bookingDetailsList = bookingSpotRepo.findByBookDate(bDate);
            if (bookingDetailsList.isEmpty()){
                return createOnlineNewBookingDetailsByBookDate(bDate,modeType);
            }
            return existingBookingInOnlineByBookDate(bookingDetailsList,modeType,bDate);
        }

        if ("SpotBooking".equalsIgnoreCase(modeType)){
            List<BookingDetails> bookingDetailsList = bookingSpotRepo.findByBookDate(bDate);
            if (bookingDetailsList.isEmpty()){
                return createNewBookingDetailsByBookDate(bDate,modeType);
            }
            return existingBookingSeatDetailsByBookDate(bookingDetailsList,modeType,bDate);
        }
        messageId = "ModeType cannot be find.";
        return new ResponseEntity<>(messageId,HttpStatus.NOT_FOUND);
//        List<BookingDetails> bookingDetailsList = bookingSpotRepo.findByBookDate(bDate);
//        if (!bookingDetailsList.isEmpty()){
//            for (BookingDetails bookingDetails : bookingDetailsList){
//                Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(bookingDetails.getSlotId());
//                if (spotSlotOptional.isPresent()){
//                    SpotSlot spotSlot = spotSlotOptional.get();
//                    LocalTime now = LocalTime.now();
//                    if (bookingDetails.getBookDate().isEqual(bDate)&& now.isAfter(spotSlot.getSlotStartTime()) && now.isBefore(spotSlot.getSlotEndTime())){
//                        return new ResponseEntity<>(bookingDetails,HttpStatus.OK);
//                    }else {
//                        List<SpotSlot> spotSlotList = spotSlotRepo.findAll();
//                        if (!spotSlotList.isEmpty()){
//                            for (SpotSlot slot : spotSlotList){
//                                if (now.isAfter(slot.getSlotStartTime()) && now.isBefore(slot.getSlotEndTime())){
//                                    bookingDetails.setSlotId(slot.getId());
//                                    bookingDetails.setSlotStartTime(slot.getSlotStartTime());
//                                    bookingDetails.setSlotEndTime(slot.getSlotEndTime());
//                                    bookingDetails.setTotalCapacity(slot.getTotalCapacity());
//                                    bookingDetails.setPresentStatus(slot.getStatus());
//                                    bookingDetails.setPresentCapacity(slot.getTotalCapacity());
//                                    bookingSpotRepo.save(bookingDetails);
//                                    return new ResponseEntity<>(bookingDetails,HttpStatus.CREATED);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        BookingDetails bookingDetails = new BookingDetails();
//        bookingDetails.setBookDate(bDate);
//        LocalTime now = LocalTime.now();
//        List<SpotSlot> spotSlotList = spotSlotRepo.findAll();
//        if (!spotSlotList.isEmpty()){
//            for (SpotSlot spotSlot : spotSlotList){
//                if (now.isAfter(spotSlot.getSlotStartTime()) && now.isBefore(spotSlot.getSlotEndTime())){
//                    bookingDetails.setSlotId(spotSlot.getId());
//                    bookingDetails.setSlotStartTime(spotSlot.getSlotStartTime());
//                    bookingDetails.setSlotEndTime(spotSlot.getSlotEndTime());
//                    bookingDetails.setTotalCapacity(spotSlot.getTotalCapacity());
//                    bookingDetails.setPresentCapacity(spotSlot.getTotalCapacity());
//                    bookingDetails.setPresentStatus(spotSlot.getStatus());
//                    bookingSpotRepo.save(bookingDetails);
//                    return new ResponseEntity<>(bookingDetails,HttpStatus.OK);
//                }
//            }
//        }
//        return new ResponseEntity<>("No slots are available at "+now,HttpStatus.NO_CONTENT);
    }

    private ResponseEntity<?> createOnlineNewBookingDetailsByBookDate(LocalDate bDate, String modeType) {
        BookingDetails bookingDetails = new BookingDetails();
        bookingDetails.setBookDate(bDate);
        LocalTime nowTime = LocalTime.now();
        List<SpotSlot> spotSlotList = spotSlotRepo.findAll();
        if (spotSlotList.isEmpty()){
            return new ResponseEntity<>("Create slot at least one.!",HttpStatus.NOT_FOUND);
        }
        for (SpotSlot slot : spotSlotList){
            bookingDetails.setBookDate(bDate);
            bookingDetails.setSlotId(slot.getId());
            bookingDetails.setSlotStartTime(slot.getSlotStartTime());
            bookingDetails.setSlotEndTime(slot.getSlotEndTime());
            bookingDetails.setPresentCapacity(slot.getTotalCapacity());
            bookingDetails.setTotalCapacity(slot.getTotalCapacity());
            bookingDetails.setPresentStatus(slot.getStatus());
            bookingSpotRepo.save(bookingDetails);
        }
        List<BookingSlotDto> slotDtoList = new ArrayList<>();
        List<BookingDetails> bookingDetailsList = bookingSpotRepo.findByBookDate(bDate);
        if (!bookingDetailsList.isEmpty()){
            for (BookingDetails bDetails : bookingDetailsList){
                if (bDetails.getSlotEndTime().isAfter(nowTime)){
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
        }
        return new ResponseEntity<>(slotDtoList,HttpStatus.OK);
    }

    private ResponseEntity<?> existingBookingInOnlineByBookDate(List<BookingDetails> bookingDetailsList, String modeType, LocalDate bDate) {
        if (!bookingDetailsList.isEmpty()){
            List<BookingSlotDto> slotDtoList = new ArrayList<>();
            LocalTime nowTime = LocalTime.now();
            for (BookingDetails bDetails:bookingDetailsList){
                if (bDetails.getBookDate().isEqual(bDate) && bDetails.getSlotEndTime().isAfter(nowTime)){
                    BookingSlotDto slotDto = new BookingSlotDto();
                    Integer slotId = bDetails.getSlotId();
                    checkPublicTicketHold(bDate,slotId,bDetails);
                    checkInstitutionTicketHold(bDate,slotId,bDetails);
                    checkForeignerTicketHold(bDate,slotId,bDetails);

                    slotDto.setSlotId(bDetails.getSlotId());
                    slotDto.setBookDate(bDetails.getBookDate());
                    slotDto.setSlotStartTime(bDetails.getSlotStartTime());
                    slotDto.setSlotEndTime(bDetails.getSlotEndTime());
                    slotDto.setPresentStatus(bDetails.getPresentStatus());
                    slotDto.setPresentCapacity(bDetails.getPresentCapacity());
                    slotDto.setTotalCapacity(bDetails.getTotalCapacity());
                    slotDtoList.add(slotDto);
                    return new ResponseEntity<>(slotDtoList,HttpStatus.OK);
                }else {
                    LocalDate currentDay = LocalDate.now();
                    checkPublicByCurrentDate(currentDay);
                    checkInstitutionByCurrentDate(currentDay);
                    checkForeignerByCurrentDate(currentDay);
                    BookingSlotDto slotDto = new BookingSlotDto();
                    if (bDetails.getSlotEndTime().isAfter(nowTime)){
                        slotDto.setSlotId(bDetails.getSlotId());
                        slotDto.setBookDate(bDetails.getBookDate());
                        slotDto.setSlotStartTime(bDetails.getSlotStartTime());
                        slotDto.setSlotEndTime(bDetails.getSlotEndTime());
                        slotDto.setPresentCapacity(bDetails.getPresentCapacity());
                        slotDto.setPresentStatus(bDetails.getPresentStatus());
                        slotDto.setTotalCapacity(bDetails.getTotalCapacity());
                        slotDtoList.add(slotDto);
                    }
                    return new ResponseEntity<>(slotDtoList,HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(createOnlineNewBookingDetailsByBookDate(bDate,modeType),HttpStatus.OK);
    }

    private void checkForeignerTicketHold(LocalDate bDate, Integer slotId, BookingDetails bDetails) {
        List<ForeignerUserOnline> foreignerUserOnlineList = fUserOnlineRepository.findByBookDateAndSlotId(bDate,slotId);
        if (!foreignerUserOnlineList.isEmpty()){
            for (ForeignerUserOnline userOnline : foreignerUserOnlineList){
                LocalTime createdAt = userOnline.getCreatedAt();
                Integer countOfTicket = userOnline.getCountOfPeople();
                LocalTime expiryTime = createdAt.plusMinutes(onlineTimeInterval);
                LocalTime currentTime = LocalTime.now();
                if (currentTime.isAfter(expiryTime)){
                    String paymentId = userOnline.getPaymentId();
                    Boolean paymentStatus = userOnline.isPaymentStatus();
                    if (paymentId==null || Boolean.FALSE.equals(paymentStatus)){
                        Integer capacity = bDetails.getPresentCapacity() + countOfTicket;
                        bDetails.setPresentCapacity(capacity);
                        bookingSpotRepo.save(bDetails);
                    }
                }
            }
        }else {
            LocalDate currentDate = LocalDate.now();
            checkForeignerByCurrentDate(currentDate);
        }
    }

    private void checkForeignerByCurrentDate(LocalDate currentDate) {
        List<ForeignerUserOnline> foreignerUserOnlineList = fUserOnlineRepository.findByBookDate(currentDate);
        if (!foreignerUserOnlineList.isEmpty()){
            for (ForeignerUserOnline userOnline:foreignerUserOnlineList){
                LocalTime createdAt = userOnline.getCreatedAt();
                Integer countOfTicket = userOnline.getCountOfPeople();
                LocalTime expiryTime = createdAt.plusMinutes(onlineTimeInterval);
                LocalTime currentTime = LocalTime.now();
                if (currentTime.isAfter(expiryTime)){
                    String paymentId = userOnline.getPaymentId();
                    Boolean paymentStatus = userOnline.isPaymentStatus();
                    if (paymentId.isEmpty() || paymentId.isBlank() || paymentId==null ||
                            Boolean.FALSE.equals(paymentStatus)){
                        List<BookingDetails> bookingDetailsList = bookingSpotRepo.findByBookDate(currentDate);
                        if (!bookingDetailsList.isEmpty()){
                            for (BookingDetails bookDetails:bookingDetailsList){
                                Integer capacity = bookDetails.getPresentCapacity() + countOfTicket;
                                bookDetails.setPresentCapacity(capacity);
                                bookingSpotRepo.save(bookDetails);
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkInstitutionTicketHold(LocalDate bDate, Integer slotId, BookingDetails bDetails) {
        List<InstitutionUserOnline> institutionUserOnlineList = iUserOnlineRepository.findByBookDateAndSlotId(bDate,slotId);
        if (!institutionUserOnlineList.isEmpty()){
            for (InstitutionUserOnline userOnline : institutionUserOnlineList){
                LocalTime createdAt = userOnline.getCreatedAt();
                Integer countOfTicket = userOnline.getCountOfPeople();
                LocalTime expiryTime = createdAt.plusMinutes(onlineTimeInterval);
                LocalTime currentTime = LocalTime.now();
                if (currentTime.isAfter(expiryTime)){
                    String paymentId = userOnline.getPaymentId();
                    Boolean paymentStatus = userOnline.isPaymentStatus();
                    if (paymentId==null || Boolean.FALSE.equals(paymentStatus)){
                        Integer capacity = bDetails.getPresentCapacity() + countOfTicket;
                        bDetails.setPresentCapacity(capacity);
                        bookingSpotRepo.save(bDetails);
                    }
                }
            }
        }else {
            LocalDate currentDate = LocalDate.now();
            checkInstitutionByCurrentDate(currentDate);
        }
    }

    private void checkInstitutionByCurrentDate(LocalDate currentDate) {
        List<InstitutionUserOnline> institutionUserOnlineList = iUserOnlineRepository.findByBookDate(currentDate);
        if (!institutionUserOnlineList.isEmpty()){
            for (InstitutionUserOnline userOnline : institutionUserOnlineList){
                LocalTime createdAt = userOnline.getCreatedAt();
                Integer countOfTicket = userOnline.getCountOfPeople();
                LocalTime expiryTime = createdAt.plusMinutes(onlineTimeInterval);
                LocalTime currentTime = LocalTime.now();
                if (currentTime.isAfter(expiryTime)){
                    String paymentId = userOnline.getPaymentId();
                    Boolean paymentStatus = userOnline.isPaymentStatus();
                    if (paymentId == null || paymentId.isEmpty() || paymentId.isBlank()
                            || Boolean.FALSE.equals(paymentStatus)){
                        List<BookingDetails> bookingDetailsLists = bookingSpotRepo.findByBookDate(currentDate);
                        if (!bookingDetailsLists.isEmpty()){
                            for (BookingDetails bookDetails:bookingDetailsLists){
                                Integer capacity = bookDetails.getPresentCapacity() + countOfTicket;
                                bookDetails.setPresentCapacity(capacity);
                                bookingSpotRepo.save(bookDetails);
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkPublicTicketHold(LocalDate bDate, Integer slotId, BookingDetails bDetails) {
        List<PublicUserOnline> publicUserOnlineList = pUserOnlineRepository.findByBookDateAndSlotId(bDate,slotId);
        if (!publicUserOnlineList.isEmpty()){
            for (PublicUserOnline userOnline : publicUserOnlineList){
                LocalTime createdAt = userOnline.getCreatedAt();
                Integer countOfTicket = userOnline.getCountOfPeople();
                LocalTime expiryTime = createdAt.plusMinutes(onlineTimeInterval);
                LocalTime currentTime = LocalTime.now();
                if (currentTime.isAfter(expiryTime)){
                    String paymentId = userOnline.getPaymentId();
                    Boolean paymentStatus = userOnline.isPaymentStatus();
                    if (paymentId==null ||paymentId.isBlank()||paymentId.isEmpty()|| Boolean.FALSE.equals(paymentStatus)){
                        Integer capacity = bDetails.getPresentCapacity() + countOfTicket;
                        bDetails.setPresentCapacity(capacity);
                        bookingSpotRepo.save(bDetails);
                    }
                }
            }
        }else {
            LocalDate currentDate = LocalDate.now();
            checkPublicByCurrentDate(currentDate);
        }
    }

    private void checkPublicByCurrentDate(LocalDate currentDay) {
        LocalDate currentDate = currentDay;
        List<PublicUserOnline> publicUserOnlinesList = pUserOnlineRepository.findByBookDate(currentDate);
        if (!publicUserOnlinesList.isEmpty()){
            for (PublicUserOnline userOnline : publicUserOnlinesList){
                LocalTime createdAt = userOnline.getCreatedAt();
                Integer countOfTicket = userOnline.getCountOfPeople();
                LocalTime expiryTime = createdAt.plusMinutes(onlineTimeInterval);
                LocalTime currentTime = LocalTime.now();
                if (currentTime.isAfter(expiryTime)){
                    String paymentId = userOnline.getPaymentId();
                    Boolean paymentStatus = userOnline.isPaymentStatus();
                    if (paymentId==null || paymentId.isBlank() || paymentId.isEmpty() ||
                            Boolean.FALSE.equals(paymentStatus)){
                        List<BookingDetails> bookingDetailsLists = bookingSpotRepo.findByBookDate(currentDate);
                        if (!bookingDetailsLists.isEmpty()){
                            for (BookingDetails bookDetails:bookingDetailsLists){
                                Integer capacity = bookDetails.getPresentCapacity() + countOfTicket;
                                bookDetails.setPresentCapacity(capacity);
                                bookingSpotRepo.save(bookDetails);
                            }
                        }
                    }
                }
            }
        }
    }

    private ResponseEntity<?> existingBookingSeatDetailsByBookDate(List<BookingDetails> bookingDetailsList, String modeType,LocalDate bDate) {
        if (!bookingDetailsList.isEmpty()){
            LocalTime nowTime = LocalTime.now();
            for (BookingDetails bDetails :bookingDetailsList){
                if (bDetails.getBookDate().isEqual(bDate) && nowTime.isAfter(bDetails.getSlotStartTime()) && nowTime.isBefore(bDetails.getSlotEndTime())){
                    Integer slotId = bDetails.getSlotId();
                    checkPublicSpotTicketHold(bDate,slotId,bDetails);
                    checkInstitutionSpotTicketHold(bDate,slotId,bDetails);
                    checkForeignerSpotTicketHold(bDate,slotId,bDetails);
                    return new ResponseEntity<>(bDetails,HttpStatus.OK);
                }else {
                    bookingSpotRepo.delete(bDetails);
                    return new ResponseEntity<>(createNewBookingDetailsByBookDate(bDate,modeType),HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(bookingDetailsList,HttpStatus.OK);
    }

    private void checkForeignerSpotTicketHold(LocalDate bDate, Integer slotId, BookingDetails bDetails) {
        List<ForeignerData> foreignerDataList = foreignerDataRepo.findByVisitDateAndSlotId(bDate,slotId);
        if (!foreignerDataList.isEmpty()){
            for (ForeignerData userOnline:foreignerDataList){
                LocalTime createdAt = userOnline.getCreatedTime();
                Integer countOfTicket = userOnline.getCountOfPeople();
                LocalTime expiryTime = createdAt.plusMinutes(spotTimeInterval);
                LocalTime currentTime = LocalTime.now();
                if (currentTime.isAfter(expiryTime)){
                    String paymentId = userOnline.getPaymentId();
                    Integer paymentStatusId = userOnline.getPaymentStatusId();
                    String paymentStatus = paymentStatusRepo.findById(paymentStatusId).map(PaymentStatus::getStatusName).orElse(null);
                    if (paymentId.isBlank()||paymentId.isEmpty()||paymentId==null||
                    "Pending".equalsIgnoreCase(paymentStatus)){
                        Integer capacity = bDetails.getPresentCapacity() + countOfTicket;
                        bDetails.setPresentCapacity(capacity);
                        bookingSpotRepo.save(bDetails);
                    }
                }
            }
        }else {
            LocalDate currentDate = LocalDate.now();
            checkForeignerSpotByCurrentDate(currentDate);
        }
    }

    private void checkForeignerSpotByCurrentDate(LocalDate currentDate) {
        List<ForeignerData> foreignerDataList = foreignerDataRepo.findByVisitDate(currentDate);
        if (!foreignerDataList.isEmpty()){
            for (ForeignerData userOnline : foreignerDataList){
                LocalTime createdAt = userOnline.getCreatedTime();
                Integer countOfTicket = userOnline.getCountOfPeople();
                LocalTime expiryTime = createdAt.plusMinutes(spotTimeInterval);
                LocalTime currentTime = LocalTime.now();
                if (currentTime.isAfter(expiryTime)){
                    String paymnetId =userOnline.getPaymentId();
                    Integer paymentStatusId = userOnline.getPaymentStatusId();
                    String paymentStatus = paymentStatusRepo.findById(paymentStatusId).map(PaymentStatus::getStatusName).orElse(null);
                    if (paymnetId.isEmpty()||paymnetId.isBlank()||paymnetId==null||"Pending".equalsIgnoreCase(paymentStatus)){
                        List<BookingDetails> bookingDetailsList = bookingSpotRepo.findByBookDate(currentDate);
                        if (!bookingDetailsList.isEmpty()){
                            for (BookingDetails bookDetails:bookingDetailsList){
                                Integer capacity = bookDetails.getPresentCapacity() + countOfTicket;
                                bookDetails.setPresentCapacity(capacity);
                                bookingSpotRepo.save(bookDetails);
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkInstitutionSpotTicketHold(LocalDate bDate, Integer slotId, BookingDetails bDetails) {
        List<InstitutionData> institutionDataList = institutionDataRepo.findByVisitDateAndSlotId(bDate,slotId);
        if (!institutionDataList.isEmpty()){
            for (InstitutionData userOnline:institutionDataList){
                LocalTime createdAt = userOnline.getCreatedTime();
                Integer countOfTicket = userOnline.getCountOfPeople();
                LocalTime expiryTime = createdAt.plusMinutes(spotTimeInterval);
                LocalTime currentTime = LocalTime.now();
                if (currentTime.isAfter(expiryTime)){
                    String paymentId = userOnline.getPaymentId();
                    Integer paymentStatusId = userOnline.getPaymentStatusId();
                    String paymentStatus = paymentStatusRepo.findById(paymentStatusId).map(PaymentStatus::getStatusName).orElse(null);
                    if (paymentId.isBlank()||paymentId.isEmpty()||paymentId==null||"Pending".equalsIgnoreCase(paymentStatus)){
                        Integer capacity = bDetails.getPresentCapacity() + countOfTicket;
                        bDetails.setPresentCapacity(capacity);
                        bookingSpotRepo.save(bDetails);
                    }
                }
            }
        }else {
            LocalDate currentDate = LocalDate.now();
            checkInstitutionSpotCurrentDate(currentDate);
        }
    }

    private void checkInstitutionSpotCurrentDate(LocalDate currentDate) {
        List<InstitutionData> institutionDataList =institutionDataRepo.findByVisitDate(currentDate);
        if (!institutionDataList.isEmpty()){
            for (InstitutionData userOnline:institutionDataList){
                LocalTime createdAt = userOnline.getCreatedTime();
                Integer countOfTicket = userOnline.getCountOfPeople();
                LocalTime expiryTime = createdAt.plusMinutes(spotTimeInterval);
                LocalTime currentTime = LocalTime.now();
                if (currentTime.isAfter(expiryTime)){
                    String paymentId = userOnline.getPaymentId();
                    Integer paymentStatusId = userOnline.getPaymentStatusId();
                    String paymentStatus = paymentStatusRepo.findById(paymentStatusId).map(PaymentStatus::getStatusName).orElse(null);
                    if (paymentId.isEmpty()||paymentId.isBlank()||paymentId==null||"Pending".equalsIgnoreCase(paymentStatus)){
                        List<BookingDetails> bookingDetailsLists = bookingSpotRepo.findByBookDate(currentDate);
                        if (!bookingDetailsLists.isEmpty()){
                            for (BookingDetails bookDetails:bookingDetailsLists){
                                Integer capacity = bookDetails.getPresentCapacity() + countOfTicket;
                                bookDetails.setPresentCapacity(capacity);
                                bookingSpotRepo.save(bookDetails);
                            }
                        }
                    }
                }
            }
        }

    }

    private void checkPublicSpotTicketHold(LocalDate bDate, Integer slotId, BookingDetails bDetails) {
        List<PublicData> publicDataList = publicRepo.findByVisitDateAndSlotId(bDate,slotId);
        if (!publicDataList.isEmpty()){
            for (PublicData userOnline : publicDataList){
                LocalTime createdAt = userOnline.getCreatedTime();
                Integer countOfTicket = userOnline.getCountOfPeople();
                LocalTime expiryTime = createdAt.plusMinutes(spotTimeInterval);
                LocalTime currentTime = LocalTime.now();
                if (currentTime.isAfter(expiryTime)){
                    String paymentId = userOnline.getPaymentId();
                    Integer paymentStatusId = userOnline.getPaymentStatusId();
                    String paymentStatus = paymentStatusRepo.findById(paymentStatusId).map(PaymentStatus::getStatusName).orElse(null);
                    if (paymentId.isBlank()||paymentId.isEmpty()||paymentId==null ||"Pending".equalsIgnoreCase(paymentStatus)){
                        Integer capacity = bDetails.getPresentCapacity() + countOfTicket;
                        bDetails.setPresentCapacity(capacity);
                        bookingSpotRepo.save(bDetails);
                    }
                }
            }
        }else {
            LocalDate currentDate = LocalDate.now();
            checkPublicSpotByCurrentDate(currentDate);
        }
    }

    private void checkPublicSpotByCurrentDate(LocalDate currentDate) {
        List<PublicData> publicDataList = publicRepo.findByVisitDate(currentDate);
        if (!publicDataList.isEmpty()){
            for (PublicData userOnline:publicDataList){
                LocalTime createdAt = userOnline.getCreatedTime();
                Integer countOfTicket = userOnline.getCountOfPeople();
                LocalTime expiryTime = createdAt.plusMinutes(spotTimeInterval);
                LocalTime currentTime = LocalTime.now();
                if (currentTime.isAfter(expiryTime)){
                    String paymentId = userOnline.getPaymentId();
                    Integer paymentStatusId = userOnline.getPaymentStatusId();
                    String paymentStatus = paymentStatusRepo.findById(paymentStatusId).map(PaymentStatus::getStatusName).orElse(null);
                    if (paymentId.isEmpty()||paymentId.isBlank()||paymentId==null||"Pending".equalsIgnoreCase(paymentStatus)){
                        List<BookingDetails> bookingDetailsLists = bookingSpotRepo.findByBookDate(currentDate);
                        if (!bookingDetailsLists.isEmpty()){
                            for (BookingDetails bookDetails:bookingDetailsLists){
                                Integer capacity = bookDetails.getPresentCapacity() + countOfTicket;
                                bookDetails.setPresentCapacity(capacity);
                                bookingSpotRepo.save(bookDetails);
                            }
                        }
                    }
                }
            }
        }
    }

    private ResponseEntity<?> createNewBookingDetailsByBookDate(LocalDate bDate,String modeType) {
        BookingDetails bookingDetails = new BookingDetails();
        bookingDetails.setBookDate(bDate);
        LocalTime nowTime = LocalTime.now();
        List<SpotSlot> spotSlotList = spotSlotRepo.findAll();
        if (spotSlotList.isEmpty()){
            return new ResponseEntity<>("Create slot at least one.!",HttpStatus.NOT_FOUND);
        }
        for (SpotSlot spotSlot : spotSlotList){
            if (nowTime.isAfter(spotSlot.getSlotStartTime()) && nowTime.isBefore(spotSlot.getSlotEndTime())){
                bookingDetails.setSlotId(spotSlot.getId());
                bookingDetails.setSlotStartTime(spotSlot.getSlotStartTime());
                bookingDetails.setSlotEndTime(spotSlot.getSlotEndTime());
                bookingDetails.setTotalCapacity(spotSlot.getTotalCapacity());
                bookingDetails.setPresentCapacity(spotSlot.getTotalCapacity());
                bookingDetails.setPresentStatus(spotSlot.getStatus());
                bookingSpotRepo.save(bookingDetails);
                return new ResponseEntity<>(bookingDetails,HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(bookingDetails,HttpStatus.OK);
    }

    public ResponseEntity<?> updateCapacityByDateAndSlotId(LocalDate bookDate, Integer slotId, BookingDetailsDto bookingDetailsDto) {
        Optional<BookingDetails> bookingDetailsOptional = bookingSpotRepo.findByBookDateAndSlotId(bookDate,slotId);
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
