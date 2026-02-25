package com.example.MuseumTicketing.onlineTicket;

import com.example.MuseumTicketing.Guide.util.AlphaNumeric;
import com.example.MuseumTicketing.onlineTicket.foreginer.ForeignerUserOnline;
import com.example.MuseumTicketing.onlineTicket.foreginer.ForeignerUserOnlineRepository;
import com.example.MuseumTicketing.onlineTicket.institutionUser.InstitutionUserOnline;
import com.example.MuseumTicketing.onlineTicket.institutionUser.InstitutionUserOnlineRepository;
import com.example.MuseumTicketing.onlineTicket.publicUser.PublicUserOnline;
import com.example.MuseumTicketing.onlineTicket.publicUser.PublicUserOnlineRepository;
import com.example.MuseumTicketing.spotReg.AmountCalculation;
import com.example.MuseumTicketing.spotReg.SpotQRcodeService;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingDetails;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingSpotRepo;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SlotSpotDto;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SpotSlot;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SpotSlotRepo;
import com.example.MuseumTicketing.spotReg.category.category.CategoryData;
import com.example.MuseumTicketing.spotReg.category.category.CategoryRepo;
import com.example.MuseumTicketing.spotReg.userData.SpotUpdateDto;
import com.example.MuseumTicketing.spotReg.userData.SpotUserDto;
import com.google.zxing.WriterException;
import com.itextpdf.kernel.geom.PageSize;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class OnlineUserService {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId ;
    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret ;

    private static final String currency = "INR";

    private static final String BOOKING_ID_PREFIX = "AKM";

    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private PublicUserOnlineRepository publicUserOnlineRepository;
    @Autowired
    private InstitutionUserOnlineRepository institutionUserOnlineRepository;
    @Autowired
    private ForeignerUserOnlineRepository foreignerUserOnlineRepository;
    @Autowired
    private AmountCalculation amountCalculation;
    @Autowired
    private AlphaNumeric alphaNumeric;
    @Autowired
    private SpotQRcodeService spotQRcodeService;
//    @Autowired
//    private TicketPdfService ticketPdfService;
    @Autowired
    private SpotSlotRepo slotRepo;
    @Autowired
    private BookingSpotRepo bookingSpotRepo;

    public ResponseEntity<Map<String, Object>> onlinePublicTicketBooking(OnlineUserDataDto userDto, Integer category) {
        Map<String,Object> response = new HashMap<>();

        Integer slotId = userDto.getSlotId();
        LocalDate visitDate = userDto.getVisitDate();

        Integer countOfPeople = userDto.getAdult() + userDto.getChild();
        PublicUserOnline userOnline = new PublicUserOnline();
        userOnline.setName(userDto.getName());
        userOnline.setPhNumber(userDto.getPhNumber());
        userOnline.setEmailId(userDto.getEmailId());
        userOnline.setAdult(userDto.getAdult());
        userOnline.setChild(userDto.getChild());
        userOnline.setCountOfPeople(countOfPeople);
        Double totalAdultCharge = 0.0; Double totalChildCharge = 0.0;
        Double grandTotal ; Integer userCount,typeId;

        if (userDto.getAdult()>0){
            typeId=userDto.getAdultTypeId();
            userCount=userDto.getAdult();
            totalAdultCharge=amountCalculation.calculatePublicCharge(category,typeId,userCount);
        }
        userOnline.setAdultGrandTotal(totalAdultCharge);
        if (userDto.getChild()>0){
            typeId=userDto.getChildTypeId();
            userCount=userDto.getChild();
            totalChildCharge = amountCalculation.calculatePublicCharge(category,typeId,userCount);
        }
        userOnline.setChildGrandTotal(totalChildCharge);

        Double totalCharge = totalAdultCharge+totalChildCharge;
        Double totalGstRate=0.0;
        Double totalUserGst=totalGstRate*totalCharge;
        Integer extraCharge=0;
        grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharge);
        BigDecimal roundedGrandTotal = BigDecimal.valueOf(grandTotal).setScale(0, RoundingMode.HALF_UP);
        grandTotal= roundedGrandTotal.doubleValue();
        userOnline.setTotalAmount(totalCharge);
        userOnline.setTotalGstCharge(totalUserGst);
        userOnline.setGrandTotal(grandTotal);
        userOnline.setSessionId(alphaNumeric.generateRandomNumber());
        userOnline.setBookingId(alphaNumeric.generateOnlineRandomNumber());
        userOnline.setVisitDate(visitDate);
        userOnline.setBookDate(LocalDate.now());
        userOnline.setSlotId(slotId);
        LocalTime slotStartTime = getSlotStartTimeBySlotId(slotId);
        LocalTime slotEndTime = getSlotEndTimeBySlotId(slotId);
        userOnline.setSlotStartTime(slotStartTime);
        userOnline.setSlotEndTime(slotEndTime);
        userOnline.setCreatedAt(LocalTime.now());
        log.info("publicUserOnline : "+userOnline);
        response=lockUserSeatForTemporaryTimePeriod(slotId,visitDate,countOfPeople);
        publicUserOnlineRepository.save(userOnline);

        response.put("userData",userOnline);
        response.put("categoryId",category);
        response.put("categoryName","public");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    private Map<String, Object> lockUserSeatForTemporaryTimePeriod(Integer slotId, LocalDate visitDate, Integer countOfPeople) {
        Map<String,Object> response = new HashMap<>();
        Optional<BookingDetails> bookingSpotRepoOptional = bookingSpotRepo.findByBookDateAndSlotId(visitDate,slotId);
        if (bookingSpotRepoOptional.isEmpty()){
            response.put("message","can't be reduce the seatCapacity.!!!!");
            response.put("visitDate",visitDate);
            response.put("slotId",slotId);
            response.put("slotStartTime",bookingSpotRepoOptional.get().getSlotStartTime());
            response.put("slotEndTime",bookingSpotRepoOptional.get().getSlotEndTime());
            return response;
        }
        BookingDetails bDetails = bookingSpotRepoOptional.get();
        Integer pCapacity = bDetails.getPresentCapacity();
        if (countOfPeople>pCapacity){
            response.put("message","presentCapacity is underFlow");
            response.put("seat",countOfPeople);
            response.put("presentCapacity",pCapacity);
            return response;
        }else {
            pCapacity -= countOfPeople;
            bDetails.setPresentCapacity(pCapacity);
            bookingSpotRepo.save(bDetails);
            response.put("bookingDetails",bDetails);
            return response;
        }
    }


    private LocalTime getSlotEndTimeBySlotId(Integer slotId) {
        Optional<SpotSlot> spotSlotOptional = slotRepo.findById(slotId);
        if (spotSlotOptional.isEmpty()){
            return null;
        }
        SpotSlot slot = spotSlotOptional.get();
        LocalTime slotTime = slot.getSlotEndTime();
        return slotTime;
    }

    private LocalTime getSlotStartTimeBySlotId(Integer slotId) {
        Optional<SpotSlot> spotSlotOptional = slotRepo.findById(slotId);
        if (spotSlotOptional.isEmpty()){
            return null;
        }
        SpotSlot slot = spotSlotOptional.get();
        LocalTime slotTime = slot.getSlotStartTime();
        return slotTime;
    }

    public ResponseEntity<Map<String, Object>> onlineInstitutionTicketBooking(OnlineUserDataDto userDto, Integer category) {
        Map<String,Object> response = new HashMap<>();
        try {
            Integer countOfPeople = userDto.getTeacher() + userDto.getStudent();
            Integer slotId = userDto.getSlotId();
            InstitutionUserOnline userOnline = new InstitutionUserOnline();
            userOnline.setName(userDto.getName());
            userOnline.setPhNumber(userDto.getPhNumber());
            userOnline.setDistrict(userDto.getDistrict());
            userOnline.setEmailId(userDto.getEmailId());
            userOnline.setStudent(userDto.getStudent());
            userOnline.setTeacher(userDto.getTeacher());
            userOnline.setCountOfPeople(countOfPeople);
            Double totalTeacherCharge=0.0;  Double totalStudentCharge=0.0;
            Double grandTotal;Integer userCount,typeId;
            if (userDto.getTeacher()>0){
                typeId=userDto.getTeacherTypeId();
                userCount = userDto.getTeacher();
                totalTeacherCharge = amountCalculation.calculateInstitutionCharge(category,typeId,userCount);
            }

            if (userDto.getStudent()>0){
                typeId=userDto.getStudentTypeId();
                userCount = userDto.getStudent();
                totalStudentCharge = amountCalculation.calculateInstitutionCharge(category,typeId,userCount);
            }

            Double totalCharges = totalStudentCharge+totalTeacherCharge;
            userOnline.setStudentTicketCharge(totalStudentCharge);
            userOnline.setTeacherTicketCharge(totalTeacherCharge);

            Double totalGstRate = amountCalculation.CalculateGST(),totalUserGST;
            totalUserGST = totalGstRate*totalCharges;

            Integer extraCharge=0;
            grandTotal=amountCalculation.calculateGrandTotal(totalUserGST,extraCharge,totalCharges);
            BigDecimal roundedGrandTotal = BigDecimal.valueOf(grandTotal).setScale(0,RoundingMode.HALF_UP);
            grandTotal= roundedGrandTotal.doubleValue();

            userOnline.setTotalAmount(totalCharges);
            userOnline.setTotalGstCharge(totalUserGST);
            userOnline.setGrandTotal(grandTotal);
            userOnline.setSessionId(alphaNumeric.generateRandomNumber());
            userOnline.setBookingId(alphaNumeric.generateOnlineRandomNumber());
            LocalDate visitDate = userDto.getVisitDate();
            userOnline.setVisitDate(visitDate);
            userOnline.setBookDate(LocalDate.now());
            userOnline.setSlotId(userDto.getSlotId());
            LocalTime slotStartTime = getSlotStartTimeBySlotId(slotId);
            LocalTime slotEndTime = getSlotEndTimeBySlotId(slotId);
            userOnline.setSlotStartTime(slotStartTime);
            userOnline.setSlotEndTime(slotEndTime);
            userOnline.setCreatedAt(LocalTime.now());
            response = lockUserSeatForTemporaryTimePeriod(slotId,visitDate,countOfPeople);
            institutionUserOnlineRepository.save(userOnline);
            response.put("userData",userOnline);
            response.put("categoryName","institution");
            response.put("categoryId",category);
            return new ResponseEntity<>(response,HttpStatus.OK);

        }catch (Exception e){
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Map<String, Object>> onlineForeignerTicketBooking(OnlineUserDataDto userDto, Integer category) {
        Map<String,Object> response = new HashMap<>();
        try {
            Integer slotId = userDto.getSlotId();
            ForeignerUserOnline userOnline = new ForeignerUserOnline();
            userOnline.setName(userDto.getName());
            userOnline.setPhNumber(userDto.getPhNumber());
            userOnline.setAdult(userDto.getAdult());
            userOnline.setChild(userDto.getChild());
            userOnline.setEmailId(userDto.getEmailId());
            Integer countOfPeople = userDto.getAdult() + userDto.getChild();
            userOnline.setCountOfPeople(countOfPeople);
            Double totalAdultCharge=0.0;    Double totalChildCharge=0.0;    Double grandTotal;
            Integer userCount,typeId;
            if (userDto.getAdult()>0){      // calculating ticket charge of foreign adult ticket charge
                typeId=userDto.getAdultTypeId();
                userCount = userDto.getAdult();

                totalAdultCharge=amountCalculation.calculateForeignerCharge(category,typeId,userCount);
            }
            userOnline.setAdultGrandTotal(totalAdultCharge);

            if (userDto.getChild()>0){      // calculating ticket charge of foreign child ticket charge
                typeId=userDto.getChildTypeId();
                userCount = userDto.getChild();

                totalChildCharge=amountCalculation.calculateForeignerCharge(category,typeId,userCount);
            }
            userOnline.setChildGrandTotal(totalChildCharge);
            Double totalCharges=totalAdultCharge+totalChildCharge;

            Double totalGstRate =amountCalculation.CalculateGST(),totalUserGst;
            totalUserGst = totalGstRate*totalCharges;

            Integer extraCharge=0;

            grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);
            BigDecimal roundedGrandTotal = BigDecimal.valueOf(grandTotal).setScale(0,RoundingMode.HALF_UP);
            grandTotal= roundedGrandTotal.doubleValue();

            userOnline.setTotalAmount(totalCharges);
            userOnline.setTotalGstCharge(totalUserGst);
            userOnline.setGrandTotal(grandTotal);
            userOnline.setSessionId(alphaNumeric.generateRandomNumber());
            userOnline.setBookingId(alphaNumeric.generateOnlineRandomNumber());
            LocalDate visitDate = userDto.getVisitDate();
            userOnline.setVisitDate(visitDate);
            userOnline.setBookDate(LocalDate.now());
            LocalTime slotStartTime = getSlotStartTimeBySlotId(slotId);
            LocalTime slotEndTime = getSlotEndTimeBySlotId(slotId);
            userOnline.setSlotId(slotId);
            userOnline.setSlotStartTime(slotStartTime);
            userOnline.setSlotEndTime(slotEndTime);
            userOnline.setCreatedAt(LocalTime.now());
            response = lockUserSeatForTemporaryTimePeriod(slotId,visitDate,countOfPeople);
            foreignerUserOnlineRepository.save(userOnline);
            response.put("userData",userOnline);
            response.put("categoryName","foreigner");
            response.put("categoryId",category);
            return new ResponseEntity<>(response,HttpStatus.OK);

        }catch (Exception e){
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Map<String, Object>> onlinePublicUserRegModify(Integer categoryId, SpotUpdateDto spotUpdateDto, String sessionId) {
        Map<String,Object> response = new HashMap<>();
        Optional<PublicUserOnline> publicUserOnlineOptional = publicUserOnlineRepository.findBySessionId(sessionId);
        if (publicUserOnlineOptional.isEmpty()){
            response.put("message","user is not found");
            response.put("sessionId",sessionId);
            return new ResponseEntity<>(response,HttpStatus.NO_CONTENT);
        }
        PublicUserOnline userOnline = publicUserOnlineOptional.get();
        userOnline.setAdult(spotUpdateDto.getAdult());
        userOnline.setChild(spotUpdateDto.getChild());
        Double totalAdultCharge=0.0;    Double totalChildCharge=0.0;
        Double grandTotal; Integer userCount,typeId;
        if (spotUpdateDto.getAdult()>0){
            typeId = spotUpdateDto.getAdultTypeId();
            userCount = spotUpdateDto.getAdult();
            totalAdultCharge =amountCalculation.calculatePublicCharge(categoryId,typeId,userCount);
        }

        if (spotUpdateDto.getChild()>0){ //calculating total child ticket charge
            typeId=spotUpdateDto.getChildTypeId();
            userCount = spotUpdateDto.getChild();
            totalChildCharge = amountCalculation.calculatePublicCharge(categoryId,typeId,userCount);
        }

        Double totalCharges = totalAdultCharge+totalChildCharge;

        userOnline.setAdultGrandTotal(totalAdultCharge);
        userOnline.setChildGrandTotal(totalChildCharge);

        Double totalGstRate =0.0; Double totalUserGst;
        totalGstRate = amountCalculation.CalculateGST(); // calculating GST charge
        totalUserGst = totalGstRate*totalCharges;

        Integer extraCharge=0;

        grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);
        BigDecimal roundedGrandTotal = BigDecimal.valueOf(grandTotal).setScale(0,RoundingMode.HALF_UP);
        grandTotal= roundedGrandTotal.doubleValue();

        userOnline.setTotalAmount(totalCharges);
        userOnline.setTotalGstCharge(totalUserGst);
        userOnline.setGrandTotal(grandTotal);
        publicUserOnlineRepository.save(userOnline);
        response.put("userData",userOnline);
        response.put("categoryName","public");
        response.put("categoryId",categoryId);
        response.put("message","public user details are updataed");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    public ResponseEntity<Map<String, Object>> onlineInstitutionUserRegModify(Integer categoryId, SpotUpdateDto spotUpdateDto, String sessionId) {
        Map<String,Object> response = new HashMap<>();
        Optional<InstitutionUserOnline> institutionUserOnlineOptional = institutionUserOnlineRepository.findBySessionId(sessionId);
        if (institutionUserOnlineOptional.isEmpty()){
            response.put("message","User is not found .");
            response.put("sessionId",sessionId);
            return new ResponseEntity<>(response,HttpStatus.NO_CONTENT);
        }
        InstitutionUserOnline userOnline = institutionUserOnlineOptional.get();
        userOnline.setTeacher(spotUpdateDto.getTeacher());
        userOnline.setStudent(spotUpdateDto.getStudent());

        Double totalTeacherCharge=0.0;  Double totalStudentCharge=0.0;
        Double grandTotal;Integer userCount,typeId;
        if (spotUpdateDto.getTeacher()>0){    // calculating total teacher's ticket charge0
            typeId = spotUpdateDto.getTeacherTypeId();
            userCount = spotUpdateDto.getTeacher(); // total no.of teacher's count
            //calculating total teacher's ticket charge by categoryId, userTypeId and count
            totalTeacherCharge = amountCalculation.calculateInstitutionCharge(categoryId,typeId,userCount);
        }
        if (spotUpdateDto.getStudent()>0){ // calculating total students ticket charge
            typeId=spotUpdateDto.getStudentTypeId();
            userCount=spotUpdateDto.getStudent();     // total no.of students count.
            //calculating total students ticket charge by categoryId, userTypeId and count
            totalStudentCharge = amountCalculation.calculateInstitutionCharge(categoryId,typeId,userCount);
        }
        Double totalCharges = totalStudentCharge+totalTeacherCharge;

        userOnline.setStudentTicketCharge(totalStudentCharge);
        userOnline.setTeacherTicketCharge(totalTeacherCharge);

        Double totalGstRate =amountCalculation.CalculateGST(),totalUserGst;
        totalUserGst = totalGstRate*totalCharges;

        Integer extraCharge=0;

        grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);
        BigDecimal roundedGrandTotal = BigDecimal.valueOf(grandTotal).setScale(0,RoundingMode.HALF_UP);
        grandTotal= roundedGrandTotal.doubleValue();

        userOnline.setTotalAmount(totalCharges);
        userOnline.setTotalGstCharge(totalUserGst);
        userOnline.setGrandTotal(grandTotal);
        institutionUserOnlineRepository.save(userOnline);
        response.put("userData",userOnline);
        response.put("message","user details are updated");
        response.put("categoryId",categoryId);
        response.put("categoryName","institution");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    public ResponseEntity<Map<String, Object>> onlineForeignerUserRegModify(Integer categoryId, SpotUpdateDto spotUpdateDto, String sessionId) {
        Map<String,Object> response = new HashMap<>();
        Optional<ForeignerUserOnline> foreignerUserOnlineOptional = foreignerUserOnlineRepository.findBySessionId(sessionId);
        if (foreignerUserOnlineOptional.isEmpty()){
            response.put("message","user not found");
            response.put("sessionId",sessionId);
            return new ResponseEntity<>(response,HttpStatus.NO_CONTENT);
        }

        ForeignerUserOnline userOnline = foreignerUserOnlineOptional.get();
        userOnline.setAdult(spotUpdateDto.getAdult());
        userOnline.setChild(spotUpdateDto.getChild());

        Double totalAdultCharge=0.0;    Double totalChildCharge=0.0;    Double grandTotal;
        Integer userCount,typeId;
        if (spotUpdateDto.getAdult()>0){      // calculating ticket charge of foreign adult ticket charge
            typeId=spotUpdateDto.getAdultTypeId();
            userCount = spotUpdateDto.getAdult();

            totalAdultCharge=amountCalculation.calculateForeignerCharge(categoryId,typeId,userCount);
        }
        if (spotUpdateDto.getChild()>0){      // calculating ticket charge of foreign child ticket charge
            typeId=spotUpdateDto.getChildTypeId();
            userCount = spotUpdateDto.getChild();

            totalChildCharge=amountCalculation.calculateForeignerCharge(categoryId,typeId,userCount);
        }
        Double totalCharges=totalAdultCharge+totalChildCharge;

        userOnline.setAdultGrandTotal(totalAdultCharge);
        userOnline.setChildGrandTotal(totalChildCharge);

        Double totalGstRate =amountCalculation.CalculateGST(),totalUserGst;
        totalUserGst = totalGstRate*totalCharges;

        Integer extraCharge=0;

        grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);
        BigDecimal roundedGrandTotal = BigDecimal.valueOf(grandTotal).setScale(0,RoundingMode.HALF_UP);
        grandTotal= roundedGrandTotal.doubleValue();
        userOnline.setTotalAmount(totalCharges);
        userOnline.setTotalGstCharge(totalUserGst);
        userOnline.setGrandTotal(grandTotal);
        foreignerUserOnlineRepository.save(userOnline);
        response.put("userData",userOnline);
        response.put("categoryId",categoryId);
        response.put("categoryName","foreigner");
        response.put("message","foreigner details are updated");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    public Map<String, Object> makeOnlinePayment(Integer categoryId, String sessionId, double payAmount) {
        Map<String, Object> response = new HashMap<>();
        try {
            String orderId =generateOrderId(payAmount);
            log.info("orderId : "+orderId);

            String categoryName = categoryRepo.findById(categoryId).map(CategoryData::getCategory).orElse(null);

            if ("Public".equalsIgnoreCase(categoryName)){
                response=publicMakePayment(sessionId,orderId,payAmount);
                response.put("categoryId",categoryId);
                return response;
            }
            if ("Institution".equalsIgnoreCase(categoryName)){
                response=institutionMakePayment(sessionId,orderId,payAmount);
                response.put("categoryId",categoryId);
                return response;
            }
            if ("Foreigner".equalsIgnoreCase(categoryName)){
                response=foreignerMakePayment(sessionId,orderId,payAmount);
                response.put("categoryId",categoryId);
                return response;
            }
            response.put("Error","categoryName is not find out");
            response.put("categoryName",categoryName);
            return response;
        }catch (Exception e){
            response.put("Error",e.getMessage());
            return response;
        }
    }

    private Map<String, Object> foreignerMakePayment(String sessionId, String orderId,double payAmount) {
        Map<String,Object> response= new HashMap<>();
        Optional<ForeignerUserOnline> foreignerUserOnlineOptional = foreignerUserOnlineRepository.findBySessionId(sessionId);
        if (foreignerUserOnlineOptional.isPresent()){
            ForeignerUserOnline userOnline = foreignerUserOnlineOptional.get();
            userOnline.setOrderId(orderId);
            foreignerUserOnlineRepository.save(userOnline);
            double payAmount_=payAmount*100;
            response = getOrderResponse(razorpayKeyId,payAmount_,currency,orderId,sessionId);
            return response;
        }
        response.put("Error","user is not find out");
        response.put("sessionId",sessionId);
        return response;
    }

    private Map<String, Object> institutionMakePayment(String sessionId, String orderId,double payAmount) {
        Map<String,Object> response= new HashMap<>();
        Optional<InstitutionUserOnline> institutionUserOnlineOptional = institutionUserOnlineRepository.findBySessionId(sessionId);
        if (institutionUserOnlineOptional.isPresent()){
            InstitutionUserOnline userOnline = institutionUserOnlineOptional.get();
            userOnline.setOrderId(orderId);
            institutionUserOnlineRepository.save(userOnline);
            double payAmount_=payAmount*100;
            response = getOrderResponse(razorpayKeyId,payAmount_,currency,orderId,sessionId);
            return response;
        }
        response.put("Error","user is not find out");
        response.put("sessionId",sessionId);
        return response;
    }

    private Map<String, Object> publicMakePayment(String sessionId, String orderId,double payAmount) {
        Map<String,Object> response= new HashMap<>();
        Optional<PublicUserOnline> publicUserOnlineOptional = publicUserOnlineRepository.findBySessionId(sessionId);
        if (publicUserOnlineOptional.isPresent()){
            PublicUserOnline userOnline = publicUserOnlineOptional.get();
            userOnline.setOrderId(orderId);
            publicUserOnlineRepository.save(userOnline);
            double payAmount_=payAmount*100;
            response = getOrderResponse(razorpayKeyId,payAmount_,currency,orderId,sessionId);
            return response;
        }
        response.put("Error","user is not find out");
        response.put("sessionId",sessionId);
        return response;
    }

    private Map<String, Object> getOrderResponse(String razorpayKeyId, double payAmount, String currency, String orderId, String sessionId) {
        Map<String, Object> response = new HashMap<>();
        response.put("razorpayKeyId",razorpayKeyId);
        response.put("amount",payAmount);
        response.put("currency",currency);
        response.put("orderId",orderId);
        response.put("sessionId",sessionId);
        return response;

    }

    private String generateOrderId(double payAmount) throws RazorpayException {
        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId,razorpayKeySecret);

            JSONObject options = new JSONObject();
            options.put("amount",payAmount*100);// Razorpay expects amount in paisa
            options.put("currency",currency);
            options.put("receipt", "order_rcptid_" + System.currentTimeMillis());
            options.put("payment_capture", 1); // Auto capture

            Order order = razorpayClient.orders.create(options);
            return order.get("id");
        }catch (RazorpayException e){
            throw new RuntimeException("Failed to create Razorpay order",e);
        }
    }

    public Map<String, Object> verifyUserPayment(String orderId, String paymentId, String signatureData, Integer categoryId) throws RazorpayException{
        Map<String,Object> response = new HashMap<>();
        response = verifyPayment(orderId,paymentId,signatureData);
        String ticketId = (String) response.get("ticketId");
        String paymentStatus = (String) response.get("paymentStatus");

        String categoryName = categoryRepo.findById(categoryId).map(CategoryData::getCategory).orElse(null);
        if ("Public".equalsIgnoreCase(categoryName)){
            setPublicTicketIdAndPaymentStatus(orderId,paymentId,ticketId,paymentStatus);
        }
        if ("Institution".equalsIgnoreCase(categoryName)){
            setInstitutionTicketIdAndPaymentStatus(orderId,paymentId,ticketId);
        }
        if ("Foreigner".equalsIgnoreCase(categoryName)){
            setForeignerTicketIdAndPaymentStatus(orderId,paymentId,ticketId);
        }
        response.put("Error","categoryName is not find");
        response.put("categoryId",categoryId);
        return response;
    }

    private void setForeignerTicketIdAndPaymentStatus(String orderId, String paymentId, String ticketId) {
        Optional<ForeignerUserOnline> foreignerUserOnlineOptional = foreignerUserOnlineRepository.findByOrderId(orderId);
        if (foreignerUserOnlineOptional.isPresent()){
            ForeignerUserOnline userOnline = foreignerUserOnlineOptional.get();
            userOnline.setTicketId(ticketId);
            userOnline.setPaymentId(paymentId);
            userOnline.setPaymentStatus(true);
            foreignerUserOnlineRepository.save(userOnline);
        }
    }

    private void setInstitutionTicketIdAndPaymentStatus(String orderId, String paymentId, String ticketId) {
        Optional<InstitutionUserOnline> institutionUserOnlineOptional = institutionUserOnlineRepository.findByOrderId(orderId);
        if (institutionUserOnlineOptional.isPresent()){
            InstitutionUserOnline userOnline = institutionUserOnlineOptional.get();
            userOnline.setTicketId(ticketId);
            userOnline.setPaymentId(paymentId);
            userOnline.setPaymentStatus(true);
            institutionUserOnlineRepository.save(userOnline);
        }
    }

    private void setPublicTicketIdAndPaymentStatus(String orderId, String paymentId, String ticketId, String paymentStatus) {
        Optional<PublicUserOnline> publicUserOnlineOptional = publicUserOnlineRepository.findByOrderId(orderId);
        if (publicUserOnlineOptional.isPresent()){
            PublicUserOnline userOnline=publicUserOnlineOptional.get();
            userOnline.setTicketId(ticketId);
            userOnline.setPaymentId(paymentId);
            userOnline.setPaymentStatus(true);
            publicUserOnlineRepository.save(userOnline);
        }
    }

    private Map<String, Object> verifyPayment(String orderId, String paymentId, String signatureData) throws RazorpayException{
        Map<String,Object> response = new HashMap<>();
        try {
            if (!verifySignature(orderId,paymentId,signatureData)){
                throw new RuntimeException("Signature verification failed.");
            }

            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId,razorpayKeySecret);

            Payment payment = razorpayClient.payments.fetch(paymentId);

            boolean paymentStatus = "captured".equals(payment.get("status"));
            if (paymentStatus){
                String ticketId = generateTicketId();
                response.put("ticketId",ticketId);
                response.put("paymentStatus",paymentStatus);
                return response;
            }
            response.put("paymentStatus",paymentStatus);
            return response;
        }catch (RazorpayException e){
            response.put("Error",e.getMessage());
            return response;
        }
    }

    private String generateTicketId() {

        String randomNumber = generateRandomNumber();
        return BOOKING_ID_PREFIX+randomNumber;
    }

    private String generateRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(90000) + 10000; // Generates a random number between 10000 and 99999
        return String.valueOf(randomNumber);
    }

    private boolean verifySignature(String orderId, String paymentId, String signatureData) {
//        String secret = "iOSGwx2YAmHsl2dNuzfi1bSa";

        String generatedSignature = orderId+"|"+paymentId;
        generatedSignature = HmacUtils.hmacSha256Hex(razorpayKeySecret,generatedSignature);

        return generatedSignature.equals(signatureData);
    }


    public Map<String, Object> generateTicketQrCode(Integer categoryId, String paymentId) throws WriterException, IOException {
        Map<String,Object> response = new HashMap<>();
        if (categoryId==null||categoryId<0){
            response.put("Error","categoryId is required");
            response.put("categoryId",categoryId);
            return response;
        }

        if (paymentId.isBlank()||paymentId.isEmpty()||paymentId==null){
            response.put("Error","paymentId is required.");
            response.put("paymentId",paymentId);
            return response;
        }
        Optional<CategoryData> categoryDataOptional = categoryRepo.findById(categoryId);
        if (categoryDataOptional.isEmpty()){
            response.put("Error","categoryId is not present.");
            response.put("categoryId",categoryId);
            return response;
        }
        CategoryData categoryData = categoryDataOptional.get();
        String categoryName = categoryData.getCategory();
        if ("public".equalsIgnoreCase(categoryName)){
            Optional<PublicUserOnline> publicUserOnlineOptional = publicUserOnlineRepository.findByPaymentId(paymentId);
            if (publicUserOnlineOptional.isEmpty()){
                response.put("Error","paymentId is not present in "+categoryName);
                response.put("paymentId",paymentId);
                response.put("categoryId",categoryId);
                response.put("categoryName",categoryName);
                return response;
            }
            PublicUserOnline userOnline = publicUserOnlineOptional.get();
            response.put("userDetails",userOnline);
            String ticketId = userOnline.getTicketId();
            response.put("qrCodeImage",spotQRcodeService.generateQRCode(ticketId));
//            response = sendTicketEmail(ticketId,categoryId);

            return response;
        }
        if ("Institution".equalsIgnoreCase(categoryName)){
            Optional<InstitutionUserOnline> institutionUserOnlineOptional = institutionUserOnlineRepository.findByPaymentId(paymentId);
            if (institutionUserOnlineOptional.isEmpty()){
                response.put("Error","paymentId is not present in "+categoryName);
                response.put("paymentId",paymentId);
                response.put("categoryId",categoryId);
                response.put("categoryName",categoryName);
                return response;
            }
            InstitutionUserOnline userOnline = institutionUserOnlineOptional.get();
            response.put("userDetails",userOnline);
            String ticketId = userOnline.getTicketId();
            response.put("qrCodeImage",spotQRcodeService.generateQRCode(ticketId));
            return response;
        }
        if ("Foreigner".equalsIgnoreCase(categoryName)){
            Optional<ForeignerUserOnline> foreignerUserOnlineOptional = foreignerUserOnlineRepository.findByPaymentId(paymentId);
            if (foreignerUserOnlineOptional.isEmpty()){
                response.put("Error","paymentId is not present in "+categoryName);
                response.put("paymentId",paymentId);
                response.put("categoryId",categoryId);
                response.put("categoryName",categoryName);
                return response;
            }
            ForeignerUserOnline userOnline = foreignerUserOnlineOptional.get();
            response.put("userDetails",userOnline);
            String ticketId = userOnline.getTicketId();
            response.put("qrCodeImage",spotQRcodeService.generateQRCode(ticketId));
            return response;
        }
        response.put("categoryName",categoryName);
        response.put("categoryId",categoryId);
        return response;
    }

//    private Map<String, Object> sendTicketEmail(String ticketId,Integer categoryId) {
//        Map<String,Object> response = new HashMap<>();
//        if (ticketId==null||ticketId.isEmpty()||ticketId.isBlank()||categoryId==null){
//            response.put("Error","ticketId and categoryId are required.");
//            response.put("ticketId",ticketId);
//            response.put("categoryId",categoryId);
//            return response;
//        }
//
//        try {
//            String categoryName =categoryRepo.findById(categoryId).map(CategoryData::getCategory).orElse(null);
//            byte[] pdfBytes = ticketPdfService.generateTicketPdf(ticketId,categoryName);
//            response.put("pdfBytes",pdfBytes);
//            return response;
//        } catch (Exception e) {
//            log.error("Failed to send ticket email to {} for ticket {}: {}",ticketId,e.getMessage(),e);
//            throw new RuntimeException("Failed to send ticket email"+e.getMessage(),e);
//        }
//    }
}
