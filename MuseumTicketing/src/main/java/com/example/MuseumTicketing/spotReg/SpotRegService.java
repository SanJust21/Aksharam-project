package com.example.MuseumTicketing.spotReg;

import com.example.MuseumTicketing.Guide.util.AlphaNumeric;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingDetails;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingSpotRepo;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SpotSlot;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SpotSlotRepo;
import com.example.MuseumTicketing.spotReg.category.additionCharge.AdditionChargeRepo;
import com.example.MuseumTicketing.spotReg.category.category.CategoryData;
import com.example.MuseumTicketing.spotReg.category.category.CategoryRepo;
import com.example.MuseumTicketing.spotReg.category.gst.GSTRepo;
import com.example.MuseumTicketing.spotReg.category.paymentMode.PaymentMode;
import com.example.MuseumTicketing.spotReg.category.paymentMode.PaymentModeRepo;
import com.example.MuseumTicketing.spotReg.category.paymentStatus.PaymentStatus;
import com.example.MuseumTicketing.spotReg.category.paymentStatus.PaymentStatusRepo;
import com.example.MuseumTicketing.spotReg.category.price.PriceDataRepo;
import com.example.MuseumTicketing.spotReg.category.type.TypeRepo;
import com.example.MuseumTicketing.spotReg.userData.SpotPaymentDto;
import com.example.MuseumTicketing.spotReg.userData.SpotUpdateDto;
import com.example.MuseumTicketing.spotReg.userData.SpotUserDto;
import com.example.MuseumTicketing.spotReg.userData.dashboardDTO.*;
import com.example.MuseumTicketing.spotReg.userData.Institution.InstitutionData;
import com.example.MuseumTicketing.spotReg.userData.Institution.InstitutionDataRepo;
import com.example.MuseumTicketing.spotReg.userData.dashboardDTO.count.ForeignerVisitorsDto;
import com.example.MuseumTicketing.spotReg.userData.dashboardDTO.count.InstitutionVisitorsDto;
import com.example.MuseumTicketing.spotReg.userData.dashboardDTO.count.PublicVisitorsDto;
import com.example.MuseumTicketing.spotReg.userData.dashboardDTO.count.VisitsCountDto;
import com.example.MuseumTicketing.spotReg.userData.dashboardDTO.usesrDetails.AllUserDataDto;
import com.example.MuseumTicketing.spotReg.userData.dashboardDTO.usesrDetails.ForeignerDtoData;
import com.example.MuseumTicketing.spotReg.userData.dashboardDTO.usesrDetails.InstitutionDtoData;
import com.example.MuseumTicketing.spotReg.userData.dashboardDTO.usesrDetails.PublicDtoData;
import com.example.MuseumTicketing.spotReg.userData.foreigner.ForeignerData;
import com.example.MuseumTicketing.spotReg.userData.foreigner.ForeignerDataRepo;
import com.example.MuseumTicketing.spotReg.userData.publicUser.PublicData;
import com.example.MuseumTicketing.spotReg.userData.publicUser.PublicRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class
SpotRegService {
    private static final Logger log = LoggerFactory.getLogger(SpotRegService.class);
    @Autowired
    private PublicRepo publicRepo;
    @Autowired
    private InstitutionDataRepo institutionDataRepo;
    @Autowired
    private ForeignerDataRepo foreignerDataRepo;
    @Autowired
    private PriceDataRepo priceDataRepo;
    @Autowired
    private GSTRepo gstRepo;
    @Autowired
    private AdditionChargeRepo additionChargeRepo;
    @Autowired
    private PaymentModeRepo paymentModeRepo;
    @Autowired
    private PaymentStatusRepo paymentStatusRepo;
    @Autowired
    private AlphaNumeric alphaNumeric;
    @Autowired
    private TypeRepo typeRepo;
    @Autowired
    private AmountCalculation amountCalculation;
    @Autowired
    private BookingSpotRepo bookingSpotRepo;
    @Autowired
    private SpotSlotRepo spotSlotRepo;
    @Autowired
    private CategoryRepo categoryRepo;


    public ResponseEntity<?> publicUserReg(SpotUserDto spotUserDto, Integer category) {
        PublicData publicDetails = new PublicData();
        publicDetails.setName(spotUserDto.getName());
        publicDetails.setPhNumber(spotUserDto.getPhNumber());
        publicDetails.setAdult(spotUserDto.getAdult());
        publicDetails.setChild(spotUserDto.getChild());
        publicDetails.setSeniorCitizen(spotUserDto.getSeniorCitizen());
        Double totalAdultCharge=0.0;    Double totalChildCharge=0.0;    Double totalSeniorCitizenCharge=0.0;
        Double grandTotal;
        Integer userCount,typeId;
        if (spotUserDto.getAdult()>0){ //Calculating Adult ticket charge
            typeId = spotUserDto.getAdultTypeId();
            userCount = spotUserDto.getAdult();
            //calculating total user charge using categoryId,userTypeId and no.ofUserCount.
            totalAdultCharge =amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        if (spotUserDto.getChild()>0){ //calculating total child ticket charge
            typeId=spotUserDto.getChildTypeId();
            userCount = spotUserDto.getChild();
            totalChildCharge = amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        if (spotUserDto.getSeniorCitizen()>0){ // calculating total senior citizen ticket charge.
            typeId = spotUserDto.getSeniorCitizenTypeId();
            userCount = spotUserDto.getSeniorCitizen();
            totalSeniorCitizenCharge = amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        //total ticket charge by total no.of adult + total no.of child + total no.of senior citizen
        Double totalCharges = totalAdultCharge+totalChildCharge+totalSeniorCitizenCharge;
        Double totalGstRate =0.0; Double totalUserGst;
        totalGstRate = amountCalculation.CalculateGST(); // calculating GST charge
        totalUserGst = totalGstRate*totalCharges;   // total user GST charge
        Integer extraCharge= amountCalculation.calculateAdditionalCharges(); // additional charges
        // calculating grandTotal = totalUserGST charge + additionalCharge + totalTicket charge
        grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);
        publicDetails.setTotalAmount(totalCharges);
        publicDetails.setTotalGstCharge(totalUserGst);
        publicDetails.setTotalAdditionalCharges(extraCharge);
        publicDetails.setGrandTotal(grandTotal);
        publicDetails.setPaymentMode(spotUserDto.getPaymentMode());
        publicDetails.setOrderId(alphaNumeric.generateRandomNumber());
        publicRepo.save(publicDetails);
        return new ResponseEntity<>(publicDetails,HttpStatus.OK);
    }

    public ResponseEntity<?> institutionReg(SpotUserDto spotUserDto, Integer category) {
        InstitutionData institutionData = new InstitutionData();
        institutionData.setName(spotUserDto.getName());
        institutionData.setPhNumber(spotUserDto.getPhNumber());
        institutionData.setDistrict(spotUserDto.getDistrict());
        institutionData.setStudent(spotUserDto.getStudent());
        institutionData.setTeacher(spotUserDto.getTeacher());

        Double totalTeacherCharge=0.0;  Double totalStudentCharge=0.0;
        Double grandTotal;Integer userCount,typeId;
        if (spotUserDto.getTeacher()>0){    // calculating total teacher's ticket charge0
            typeId = spotUserDto.getTeacherTypeId();
            userCount = spotUserDto.getTeacher(); // total no.of teacher's count
            //calculating total teacher's ticket charge by categoryId, userTypeId and count
            totalTeacherCharge = amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        if (spotUserDto.getStudent()>0){ // calculating total students ticket charge
            typeId=spotUserDto.getStudentTypeId();
            userCount=spotUserDto.getStudent();     // total no.of students count.
            //calculating total students ticket charge by categoryId, userTypeId and count
            totalStudentCharge = amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        // total ticket charge = total student ticket charge + total teachers ticket charge
        Double totalCharges = totalStudentCharge+totalTeacherCharge;

        //calculate total user GST charge
        Double totalGstRate =amountCalculation.CalculateGST(),totalUserGst;
        totalUserGst = totalGstRate*totalCharges;

        //calculating additional chagres
        Integer extraCharge= amountCalculation.calculateAdditionalCharges();

        //Grand total = total GST charge + total additional charge + total ticket charge
        grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);

        institutionData.setTotalAmount(totalCharges);
        institutionData.setPaymentMode(spotUserDto.getPaymentMode());
        institutionData.setTotalGstCharge(totalUserGst);
        institutionData.setTotalAdditionalCharges(extraCharge);
        institutionData.setGrandTotal(grandTotal);
        institutionData.setOrderId(alphaNumeric.generateRandomNumber());
        institutionDataRepo.save(institutionData);
        return new ResponseEntity<>(institutionData,HttpStatus.OK);
    }

    public ResponseEntity<?> ForeignerReg(SpotUserDto spotUserDto, Integer category) {
        ForeignerData foreignerData = new ForeignerData();
        foreignerData.setName(spotUserDto.getName());
        foreignerData.setPhNumber(spotUserDto.getPhNumber());
        foreignerData.setAdult(spotUserDto.getAdult());
        foreignerData.setChild(spotUserDto.getChild());

        Double totalAdultCharge=0.0;    Double totalChildCharge=0.0;    Double grandTotal;
        Integer userCount,typeId;
        if (spotUserDto.getAdult()>0){      // calculating ticket charge of foreign adult ticket charge
            typeId=spotUserDto.getAdultTypeId();
            userCount = spotUserDto.getAdult();

            totalAdultCharge=amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        if (spotUserDto.getChild()>0){      // calculating ticket charge of foreign child ticket charge
            typeId=spotUserDto.getChildTypeId();
            userCount = spotUserDto.getChild();

            totalChildCharge=amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        Double totalCharges=totalAdultCharge+totalChildCharge;      // total ticket charge = adult charge + child charge

        //calculating GST charge
        Double totalGstRate =amountCalculation.CalculateGST(),totalUserGst;
        totalUserGst = totalGstRate*totalCharges;

        //Calculating additional charge
        Integer extraCharge= amountCalculation.calculateAdditionalCharges();

        //grand total = GST + additionalCharge + ticketCharge
        grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);

        foreignerData.setTotalAmount(totalCharges);
        foreignerData.setTotalGstCharge(totalUserGst);
        foreignerData.setTotalAdditionalCharges(extraCharge);
        foreignerData.setGrandTotal(grandTotal);
        foreignerData.setPaymentMode(spotUserDto.getPaymentMode());
        foreignerData.setOrderId(alphaNumeric.generateRandomNumber());
        foreignerDataRepo.save(foreignerData);
        return new ResponseEntity<>(foreignerData,HttpStatus.OK);
    }

    public ResponseEntity<?> UserDetailsUpdate(Integer categoryId, SpotUpdateDto spotUpdateDto, String orderId) {
        Optional<PublicData> publicDataOptional = publicRepo.findByOrderId(orderId);
        Optional<InstitutionData> institutionDataOptional = institutionDataRepo.findByOrderId(orderId);
        Optional<ForeignerData> foreignerDataOptional = foreignerDataRepo.findByOrderId(orderId);
        if (publicDataOptional.isPresent()){
            PublicData publicData = publicDataOptional.get();
            publicData.setAdult(spotUpdateDto.getAdult());
            publicData.setChild(spotUpdateDto.getChild());
            publicData.setSeniorCitizen(spotUpdateDto.getSeniorCitizen());

            Double totalAdultCharge=0.0;    Double totalChildCharge=0.0;    Double totalSeniorCitizenCharge=0.0;
            Double grandTotal;
            Integer userCount,typeId;
            if (spotUpdateDto.getAdult()>0){ //Calculating Adult ticket charge
                typeId = spotUpdateDto.getAdultTypeId();
                userCount = spotUpdateDto.getAdult();
                //calculating total user charge using categoryId,userTypeId and no.ofUserCount.
                totalAdultCharge =amountCalculation.calculateTotalUserCharge(categoryId,typeId,userCount);
            }
            if (spotUpdateDto.getChild()>0){ //calculating total child ticket charge
                typeId=spotUpdateDto.getChildTypeId();
                userCount = spotUpdateDto.getChild();
                totalChildCharge = amountCalculation.calculateTotalUserCharge(categoryId,typeId,userCount);
            }
            if (spotUpdateDto.getSeniorCitizen()>0){ // calculating total senior citizen ticket charge.
                typeId = spotUpdateDto.getSeniorCitizenTypeId();
                userCount = spotUpdateDto.getSeniorCitizen();
                totalSeniorCitizenCharge = amountCalculation.calculateTotalUserCharge(categoryId,typeId,userCount);
            }
            //total ticket charge by total no.of adult + total no.of child + total no.of senior citizen
            Double totalCharges = totalAdultCharge+totalChildCharge+totalSeniorCitizenCharge;
            Double totalGstRate =0.0; Double totalUserGst;
            totalGstRate = amountCalculation.CalculateGST(); // calculating GST charge
            totalUserGst = totalGstRate*totalCharges;   // total user GST charge
            Integer extraCharge= amountCalculation.calculateAdditionalCharges(); // additional charges
            // calculating grandTotal = totalUserGST charge + additionalCharge + totalTicket charge
            grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);
            publicData.setTotalAmount(totalCharges);
            publicData.setTotalGstCharge(totalUserGst);
            publicData.setTotalAdditionalCharges(extraCharge);
            publicData.setGrandTotal(grandTotal);
            publicRepo.save(publicData);
            return new ResponseEntity<>(publicData,HttpStatus.OK);
        } else if (institutionDataOptional.isPresent()) {
            InstitutionData institutionData = institutionDataOptional.get();
            institutionData.setTeacher(spotUpdateDto.getTeacher());
            institutionData.setTeacher(spotUpdateDto.getTeacher());
            Double totalTeacherCharge=0.0;  Double totalStudentCharge=0.0;
            Double grandTotal;Integer userCount,typeId;
            if (spotUpdateDto.getTeacher()>0){    // calculating total teacher's ticket charge0
                typeId = spotUpdateDto.getTeacherTypeId();
                userCount = spotUpdateDto.getTeacher(); // total no.of teacher's count
                //calculating total teacher's ticket charge by categoryId, userTypeId and count
                totalTeacherCharge = amountCalculation.calculateTotalUserCharge(categoryId,typeId,userCount);
            }
            if (spotUpdateDto.getStudent()>0){ // calculating total students ticket charge
                typeId=spotUpdateDto.getStudentTypeId();
                userCount=spotUpdateDto.getStudent();     // total no.of students count.
                //calculating total students ticket charge by categoryId, userTypeId and count
                totalStudentCharge = amountCalculation.calculateTotalUserCharge(categoryId,typeId,userCount);
            }
            // total ticket charge = total student ticket charge + total teachers ticket charge
            Double totalCharges = totalStudentCharge+totalTeacherCharge;

            //calculate total user GST charge
            Double totalGstRate =amountCalculation.CalculateGST(),totalUserGst;
            totalUserGst = totalGstRate*totalCharges;

            //calculating additional chagres
            Integer extraCharge= amountCalculation.calculateAdditionalCharges();

            //Grand total = total GST charge + total additional charge + total ticket charge
            grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);

            institutionData.setTotalAmount(totalCharges);
            institutionData.setTotalGstCharge(totalUserGst);
            institutionData.setTotalAdditionalCharges(extraCharge);
            institutionData.setGrandTotal(grandTotal);
            institutionDataRepo.save(institutionData);
            return new ResponseEntity<>(institutionData,HttpStatus.OK);
        } else if (foreignerDataOptional.isPresent()) {
            ForeignerData foreignerData = foreignerDataOptional.get();
            foreignerData.setAdult(spotUpdateDto.getAdult());
            foreignerData.setChild(spotUpdateDto.getChild());

            Double totalAdultCharge=0.0;    Double totalChildCharge=0.0;    Double grandTotal;
            Integer userCount,typeId;
            if (spotUpdateDto.getAdult()>0){      // calculating ticket charge of foreign adult ticket charge
                typeId=spotUpdateDto.getAdultTypeId();
                userCount = spotUpdateDto.getAdult();

                totalAdultCharge=amountCalculation.calculateTotalUserCharge(categoryId,typeId,userCount);
            }
            if (spotUpdateDto.getChild()>0){      // calculating ticket charge of foreign child ticket charge
                typeId=spotUpdateDto.getChildTypeId();
                userCount = spotUpdateDto.getChild();

                totalChildCharge=amountCalculation.calculateTotalUserCharge(categoryId,typeId,userCount);
            }
            Double totalCharges=totalAdultCharge+totalChildCharge;      // total ticket charge = adult charge + child charge

            //calculating GST charge
            Double totalGstRate =amountCalculation.CalculateGST(),totalUserGst;
            totalUserGst = totalGstRate*totalCharges;

            //Calculating additional charge
            Integer extraCharge= amountCalculation.calculateAdditionalCharges();

            //grand total = GST + additionalCharge + ticketCharge
            grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);

            foreignerData.setTotalAmount(totalCharges);
            foreignerData.setTotalGstCharge(totalUserGst);
            foreignerData.setTotalAdditionalCharges(extraCharge);
            foreignerData.setGrandTotal(grandTotal);
            foreignerDataRepo.save(foreignerData);
            return new ResponseEntity<>(foreignerData,HttpStatus.OK);
        }
        return new ResponseEntity<>("OrderId : "+orderId+" isn't valid",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> confirmPaymentDetails(String orderId, SpotPaymentDto spotPaymentDto,Integer totalUserCount) {
        Optional<PublicData> publicDataOptional = publicRepo.findByOrderId(orderId);
        Optional<InstitutionData> institutionDataOptional = institutionDataRepo.findByOrderId(orderId);
        Optional<ForeignerData> foreignerDataOptional = foreignerDataRepo.findByOrderId(orderId);

        if (publicDataOptional.isPresent()){
            PublicData publicData = publicDataOptional.get();
            publicData.setPaymentStatusId(spotPaymentDto.getPaymentStatusId());

            //check if payment mode is cash and payment status is received
            Optional<PaymentStatus>paymentStatusOptional=paymentStatusRepo.findById(spotPaymentDto.getPaymentStatusId());
            Optional<PaymentMode>paymentModeOptional=paymentModeRepo.findById(publicData.getPaymentMode());
            if (paymentModeOptional.isPresent() && paymentStatusOptional.isPresent()){
                PaymentStatus paymentStatus =paymentStatusOptional.get();
                String name =paymentStatus.getStatusName();
                PaymentMode paymentMode = paymentModeOptional.get();
                String modeName = paymentMode.getPaymentType();
                if ("cash".equalsIgnoreCase(modeName) && "received".equalsIgnoreCase(name)){
                    publicData.setTicketId(alphaNumeric.generateSpotRandomNumber());
                    publicData.setPaymentId(alphaNumeric.generateRandomNumber());
                    publicData.setCreatedTime(LocalTime.now());

                    // if the user booked the tickets then reduce  the present slot capacity by userCount.
                    BookingDetails bookingDetails = amountCalculation.generateBookingDate(spotPaymentDto.getVisitDate(),spotPaymentDto.getSlotId(),totalUserCount);
                    publicData.setSlotId(bookingDetails.getSlotId());
                    publicData.setVisitDate(bookingDetails.getBookDate());
                }else {
                    publicData.setTicketId(null);
                }
            }
            publicData.setCreatedBy(spotPaymentDto.getCreatedBy());
            publicRepo.save(publicData);
            return new ResponseEntity<>(publicData,HttpStatus.OK);

        } else if (institutionDataOptional.isPresent()) {
            InstitutionData institutionData = institutionDataOptional.get();
            institutionData.setPaymentStatusId(spotPaymentDto.getPaymentStatusId());

            //check if payment mode is cash and payment status is received
            Optional<PaymentStatus>paymentStatusOptional=paymentStatusRepo.findById(spotPaymentDto.getPaymentStatusId());
            Optional<PaymentMode>paymentModeOptional=paymentModeRepo.findById(institutionData.getPaymentMode());

            if (paymentModeOptional.isPresent() && paymentStatusOptional.isPresent()){
                PaymentStatus paymentStatus =paymentStatusOptional.get();
                String name =paymentStatus.getStatusName();
                PaymentMode paymentMode = paymentModeOptional.get();
                String modeName = paymentMode.getPaymentType();
                if ("cash".equalsIgnoreCase(modeName) && "received".equalsIgnoreCase(name)){
                    institutionData.setTicketId(alphaNumeric.generateSpotRandomNumber());
                    institutionData.setPaymentId(alphaNumeric.generateRandomNumber());
                    institutionData.setCreatedTime(LocalTime.now());
                }else {
                    institutionData.setTicketId(null);
                }
            }

            // if the user booked the tickets then reduce  the present slot capacity by userCount.
            BookingDetails bookingDetails = amountCalculation.generateBookingDate(spotPaymentDto.getVisitDate(),spotPaymentDto.getSlotId(),totalUserCount);
            institutionData.setVisitDate(bookingDetails.getBookDate());
            institutionData.setSlotId(bookingDetails.getSlotId());
            institutionData.setCreatedBy(spotPaymentDto.getCreatedBy());
            institutionDataRepo.save(institutionData);
            return new ResponseEntity<>(institutionData,HttpStatus.OK);

        } else if (foreignerDataOptional.isPresent()) {
            ForeignerData foreignerData = foreignerDataOptional.get();
            foreignerData.setPaymentStatusId(spotPaymentDto.getPaymentStatusId());

            //check if payment mode is cash and payment status is received
            Optional<PaymentStatus>paymentStatusOptional=paymentStatusRepo.findById(spotPaymentDto.getPaymentStatusId());
            Optional<PaymentMode>paymentModeOptional=paymentModeRepo.findById(foreignerData.getPaymentMode());
            if (paymentModeOptional.isPresent() && paymentStatusOptional.isPresent()){
                PaymentStatus paymentStatus =paymentStatusOptional.get();
                String name =paymentStatus.getStatusName();
                PaymentMode paymentMode = paymentModeOptional.get();
                String modeName = paymentMode.getPaymentType();
                if ("cash".equalsIgnoreCase(modeName) && "received".equalsIgnoreCase(name)){
                    foreignerData.setTicketId(alphaNumeric.generateSpotRandomNumber());
                    foreignerData.setPaymentId(alphaNumeric.generateRandomNumber());
                    foreignerData.setCreatedTime(LocalTime.now());
                }else {
                    foreignerData.setTicketId(null);
                }
            }

            // if the user booked the tickets then reduce  the present slot capacity by userCount.
            BookingDetails bookingDetails = amountCalculation.generateBookingDate(spotPaymentDto.getVisitDate(),spotPaymentDto.getSlotId(),totalUserCount);
            foreignerData.setVisitDate(bookingDetails.getBookDate());
            foreignerData.setSlotId(bookingDetails.getSlotId());
            foreignerData.setCreatedBy(spotPaymentDto.getCreatedBy());
            foreignerDataRepo.save(foreignerData);
            return new ResponseEntity<>(foreignerData,HttpStatus.OK);
        }
        return new ResponseEntity<>("OrderId :"+orderId+" isn't valid",HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> getAllUserDetails() {
        List<AllUserDataDto> allUserDataDtoList = new ArrayList<>();
        List<PublicData> publicDataList = publicRepo.findAll();
        AllUserDataDto allUserDataDto = new AllUserDataDto();
        if (!publicDataList.isEmpty()){
            List<PublicDtoData> publicDtoDataList = new ArrayList<>();
            for (PublicData publicData : publicDataList){
                PublicDtoData publicDtoData = new PublicDtoData();
                publicDtoData.setName(publicData.getName());
                publicDtoData.setPhNumber(publicData.getPhNumber());
                publicDtoData.setAdultCount(publicData.getAdult());
                publicDtoData.setChildCount(publicData.getChild());
                publicDtoData.setSeniorCitizenCount(publicData.getSeniorCitizen());
                publicDtoData.setOrderId(publicData.getOrderId());
                publicDtoData.setPaymentId(publicData.getPaymentId());
                publicDtoData.setTicketId(publicData.getTicketId());

                Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(publicData.getSlotId());
                if (spotSlotOptional.isPresent()){
                    SpotSlot spotSlot = spotSlotOptional.get();
                    publicDtoData.setSlotTime(spotSlot.getSlotEndTime());
                }
                publicDtoData.setGrandTotal(publicData.getGrandTotal());
                Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(publicData.getPaymentMode());
                if (paymentModeOptional.isPresent()){
                    PaymentMode paymentMode = paymentModeOptional.get();
                    publicDtoData.setPaymentModeName(paymentMode.getPaymentType());
                }
                Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(publicData.getPaymentStatusId());
                if (paymentStatusOptional.isPresent()){
                    PaymentStatus paymentStatus = paymentStatusOptional.get();
                    publicDtoData.setPaymentStatusName(paymentStatus.getStatusName());
                }
                publicDtoData.setTicketId(publicData.getTicketId());
                publicDtoData.setOrderId(publicData.getOrderId());
                publicDtoData.setPaymentId(publicData.getPaymentId());
                publicDtoData.setGeneratedTime(publicData.getCreatedTime());
                publicDtoData.setCreatedBy(publicData.getCreatedBy());
                publicDtoDataList.add(publicDtoData);
            }
            allUserDataDto.setPublicDtoDataList(publicDtoDataList);
            allUserDataDtoList.add(allUserDataDto);
        }
        List<InstitutionData> institutionDataList = institutionDataRepo.findAll();
        if (!institutionDataList.isEmpty()){
            List<InstitutionDtoData> institutionDtoDataList = new ArrayList<>();
            for (InstitutionData institutionData : institutionDataList){
                InstitutionDtoData institutionDtoData = new InstitutionDtoData();
                institutionDtoData.setName(institutionData.getName());
                institutionDtoData.setPhNumber(institutionData.getPhNumber());
                institutionDtoData.setDistrict(institutionData.getDistrict());
                institutionDtoData.setTeacherCount(institutionData.getTeacher());
                institutionDtoData.setStudentCount(institutionData.getStudent());
                institutionDtoData.setGrandTotal(institutionData.getGrandTotal());
                Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(institutionData.getPaymentMode());
                if (paymentModeOptional.isPresent()){
                    PaymentMode paymentMode = paymentModeOptional.get();
                    institutionDtoData.setPaymentModeName(paymentMode.getPaymentType());
                }
                Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(institutionData.getPaymentStatusId());
                if (paymentStatusOptional.isPresent()){
                    PaymentStatus paymentStatus = paymentStatusOptional.get();
                    institutionDtoData.setPaymentStatusName(paymentStatus.getStatusName());
                }
                institutionDtoData.setVisitDate(institutionData.getVisitDate());
                Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(institutionData.getSlotId());
                if (spotSlotOptional.isPresent()){
                    SpotSlot spotSlot = spotSlotOptional.get();
                    institutionDtoData.setSlotTime(spotSlot.getSlotEndTime());
                }
                institutionDtoData.setTicketId(institutionData.getTicketId());
                institutionDtoData.setOrderId(institutionData.getOrderId());
                institutionDtoData.setPaymentId(institutionData.getPaymentId());
                institutionDtoData.setCreatedBy(institutionData.getCreatedBy());
                institutionDtoData.setGeneratedTime(institutionData.getCreatedTime());
                institutionDtoDataList.add(institutionDtoData);
            }
            allUserDataDto.setInstitutionDtoDataList(institutionDtoDataList);
            allUserDataDtoList.add(allUserDataDto);
        }
        List<ForeignerData> foreignerDataList = foreignerDataRepo.findAll();
        List<ForeignerDtoData> foreignerDtoDataList = new ArrayList<>();
        if (!foreignerDataList.isEmpty()){
            for (ForeignerData foreignerData : foreignerDataList){
                ForeignerDtoData foreignerDtoData = new ForeignerDtoData();
                foreignerDtoData.setName(foreignerData.getName());
                foreignerDtoData.setPhNumber(foreignerData.getPhNumber());
                foreignerDtoData.setAdultCount(foreignerData.getAdult());
                foreignerDtoData.setChildCount(foreignerData.getChild());

                foreignerDtoData.setVisitDate(foreignerData.getVisitDate());
                Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(foreignerData.getSlotId());
                if (spotSlotOptional.isPresent()){
                    SpotSlot spotSlot = spotSlotOptional.get();
                    foreignerDtoData.setSlotTime(spotSlot.getSlotEndTime());
                }
                foreignerDtoData.setGrandTotal(foreignerData.getGrandTotal());
                Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(foreignerData.getPaymentMode());
                if (paymentModeOptional.isPresent()){
                    PaymentMode paymentMode = paymentModeOptional.get();
                    foreignerDtoData.setPaymentModeName(paymentMode.getPaymentType());
                }
                Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(foreignerData.getPaymentStatusId());
                if (paymentStatusOptional.isPresent()){
                    PaymentStatus paymentStatus = paymentStatusOptional.get();
                    foreignerDtoData.setPaymentStatusName(paymentStatus.getStatusName());
                }
                foreignerDtoData.setTicketId(foreignerData.getTicketId());
                foreignerDtoData.setOrderId(foreignerData.getOrderId());
                foreignerDtoData.setPaymentId(foreignerDtoData.getPaymentId());
                foreignerDtoData.setGeneratedTime(foreignerData.getCreatedTime());
                foreignerDtoData.setCreatedBy(foreignerData.getCreatedBy());
                foreignerDtoDataList.add(foreignerDtoData);
            }
            allUserDataDto.setForeignerDtoDataList(foreignerDtoDataList);
            allUserDataDtoList.add(allUserDataDto);
            return new ResponseEntity<>(allUserDataDtoList,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> getAllPublic() {
        List<PublicData> publicDataList = publicRepo.findAll();
        List<PublicDtoData> publicDtoDataList = new ArrayList<>();
        if (!publicDataList.isEmpty()){
            for (PublicData publicData : publicDataList){
                PublicDtoData publicDtoData = new PublicDtoData();
                publicDtoData.setName(publicData.getName());
                publicDtoData.setPhNumber(publicData.getPhNumber());
                publicDtoData.setAdultCount(publicData.getAdult());
                publicDtoData.setChildCount(publicData.getChild());
                publicDtoData.setSeniorCitizenCount(publicData.getSeniorCitizen());
                publicDtoData.setVisitDate(publicData.getVisitDate());
                Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(publicData.getSlotId());
                if (spotSlotOptional.isPresent()){
                    SpotSlot spotSlot = spotSlotOptional.get();
                    publicDtoData.setSlotTime(spotSlot.getSlotEndTime());
                }
                publicDtoData.setGrandTotal(publicData.getGrandTotal());
                Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(publicData.getPaymentMode());
                if (paymentModeOptional.isPresent()){
                    PaymentMode paymentMode = paymentModeOptional.get();
                    publicDtoData.setPaymentModeName(paymentMode.getPaymentType());
                }
                Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(publicData.getPaymentStatusId());
                if (paymentStatusOptional.isPresent()){
                    PaymentStatus paymentStatus = paymentStatusOptional.get();
                    publicDtoData.setPaymentStatusName(paymentStatus.getStatusName());
                }
                publicDtoData.setTicketId(publicData.getTicketId());
                publicDtoData.setOrderId(publicData.getOrderId());
                publicDtoData.setPaymentId(publicData.getPaymentId());
                publicDtoData.setGeneratedTime(publicData.getCreatedTime());
                publicDtoData.setCreatedBy(publicData.getCreatedBy());
                publicDtoDataList.add(publicDtoData);
            }

        }
        return new ResponseEntity<>(publicDtoDataList,HttpStatus.OK);
    }

    public ResponseEntity<?> getAllInstitution() {
        List<InstitutionData> institutionDataList = institutionDataRepo.findAll();
        List<InstitutionDtoData> institutionDtoDataList = new ArrayList<>();
        if (!institutionDataList.isEmpty()){
            for (InstitutionData institutionData : institutionDataList){
                InstitutionDtoData institutionDtoData = new InstitutionDtoData();
                institutionDtoData.setName(institutionData.getName());
                institutionDtoData.setPhNumber(institutionData.getPhNumber());
                institutionDtoData.setDistrict(institutionData.getDistrict());
                institutionDtoData.setTeacherCount(institutionData.getTeacher());
                institutionDtoData.setStudentCount(institutionData.getStudent());
                institutionDtoData.setGrandTotal(institutionData.getGrandTotal());
                Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(institutionData.getPaymentMode());
                if (paymentModeOptional.isPresent()){
                    PaymentMode paymentMode = paymentModeOptional.get();
                    institutionDtoData.setPaymentModeName(paymentMode.getPaymentType());
                }
                Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(institutionData.getPaymentStatusId());
                if (paymentStatusOptional.isPresent()){
                    PaymentStatus paymentStatus = paymentStatusOptional.get();
                    institutionDtoData.setPaymentStatusName(paymentStatus.getStatusName());
                }
                institutionDtoData.setVisitDate(institutionData.getVisitDate());
                Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(institutionData.getSlotId());
                if (spotSlotOptional.isPresent()){
                    SpotSlot spotSlot = spotSlotOptional.get();
                    institutionDtoData.setSlotTime(spotSlot.getSlotEndTime());
                }
                institutionDtoData.setTicketId(institutionData.getTicketId());
                institutionDtoData.setOrderId(institutionData.getOrderId());
                institutionDtoData.setPaymentId(institutionData.getPaymentId());
                institutionDtoData.setCreatedBy(institutionData.getCreatedBy());
                institutionDtoData.setGeneratedTime(institutionData.getCreatedTime());
                institutionDtoDataList.add(institutionDtoData);
            }
        }
        return new ResponseEntity<>(institutionDtoDataList,HttpStatus.OK);
    }

    public ResponseEntity<?> getAllForeigner() {
        List<ForeignerData> foreignerDataList = foreignerDataRepo.findAll();
        List<ForeignerDtoData> foreignerDtoDataList = new ArrayList<>();
        if (!foreignerDataList.isEmpty()){
            for (ForeignerData foreignerData : foreignerDataList){
                ForeignerDtoData foreignerDtoData = new ForeignerDtoData();
                foreignerDtoData.setName(foreignerData.getName());
                foreignerDtoData.setPhNumber(foreignerData.getPhNumber());
                foreignerDtoData.setAdultCount(foreignerData.getAdult());
                foreignerDtoData.setChildCount(foreignerData.getChild());

                foreignerDtoData.setVisitDate(foreignerData.getVisitDate());
                Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(foreignerData.getSlotId());
                if (spotSlotOptional.isPresent()){
                    SpotSlot spotSlot = spotSlotOptional.get();
                    foreignerDtoData.setSlotTime(spotSlot.getSlotEndTime());
                }
                foreignerDtoData.setGrandTotal(foreignerData.getGrandTotal());
                Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(foreignerData.getPaymentMode());
                if (paymentModeOptional.isPresent()){
                    PaymentMode paymentMode = paymentModeOptional.get();
                    foreignerDtoData.setPaymentModeName(paymentMode.getPaymentType());
                }
                Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(foreignerData.getPaymentStatusId());
                if (paymentStatusOptional.isPresent()){
                    PaymentStatus paymentStatus = paymentStatusOptional.get();
                    foreignerDtoData.setPaymentStatusName(paymentStatus.getStatusName());
                }
                foreignerDtoData.setTicketId(foreignerData.getTicketId());
                foreignerDtoData.setOrderId(foreignerData.getOrderId());
                foreignerDtoData.setPaymentId(foreignerDtoData.getPaymentId());
                foreignerDtoData.setGeneratedTime(foreignerData.getCreatedTime());
                foreignerDtoData.setCreatedBy(foreignerData.getCreatedBy());
                foreignerDtoDataList.add(foreignerDtoData);
            }

        }
        return new ResponseEntity<>(foreignerDtoDataList,HttpStatus.OK);
    }

    public ResponseEntity<List<GetUserData_>> getUserDetailsByDate(LocalDate visitDate, Integer categoryId) {
        Optional<CategoryData> categoryDataOptional = categoryRepo.findById(categoryId);
        if (categoryDataOptional.isPresent()){
            CategoryData categoryData = categoryDataOptional.get();
            String name = categoryData.getCategory();
            List<GetUserData_> getUserDataList = new ArrayList<>();
            if ("Public".equalsIgnoreCase(name)){
                List<PublicData> publicDataList = publicRepo.findByVisitDate(visitDate);
                if (!publicDataList.isEmpty()){
                    for (PublicData publicData : publicDataList){
                        GetUserData_ getUserData = new GetUserData_();
                        getUserData.setName(publicData.getName());
                        getUserData.setPhNumber(publicData.getPhNumber());
                        getUserData.setAdult(publicData.getAdult());
                        getUserData.setChild(publicData.getChild());
                        getUserData.setSeniorCitizen(publicData.getSeniorCitizen());
                        getUserData.setGrandTotal(publicData.getGrandTotal());
                        Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(publicData.getPaymentMode());
                        if (paymentModeOptional.isPresent()){
                            PaymentMode paymentMode = paymentModeOptional.get();
                            getUserData.setPaymentModeName(paymentMode.getPaymentType());
                        }
                        Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(publicData.getPaymentStatusId());
                        if (paymentStatusOptional.isPresent()){
                            PaymentStatus paymentStatus = paymentStatusOptional.get();
                            getUserData.setPaymentStatus(paymentStatus.getStatusName());
                        }
                        getUserData.setTicketId(publicData.getTicketId());
                        getUserData.setGeneratedTime(publicData.getCreatedTime());
                        Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(publicData.getSlotId());
                        if (spotSlotOptional.isPresent()){
                            SpotSlot spotSlot = spotSlotOptional.get();
                            getUserData.setSlotTime(spotSlot.getSlotEndTime());
                        }
                        getUserData.setCreatedBy(publicData.getCreatedBy());//
                        getUserDataList.add(getUserData);
                    }

                    return new ResponseEntity<>(getUserDataList,HttpStatus.OK);
                }
            } else if ("Institution".equalsIgnoreCase(name)) {
                List<InstitutionData> institutionDataList = institutionDataRepo.findByVisitDate(visitDate);
                if (!institutionDataList.isEmpty()){
                    for (InstitutionData institutionData : institutionDataList){
                        GetUserData_ getUserData = new GetUserData_();
                        getUserData.setName(institutionData.getName());
                        getUserData.setPhNumber(institutionData.getPhNumber());
                        getUserData.setDistrict(institutionData.getDistrict());
                        getUserData.setTeacher(institutionData.getTeacher());
                        getUserData.setStudent(institutionData.getStudent());
                        getUserData.setGrandTotal(institutionData.getGrandTotal());
                        Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(institutionData.getPaymentMode());
                        if (paymentModeOptional.isPresent()){
                            PaymentMode paymentMode = paymentModeOptional.get();
                            getUserData.setPaymentModeName(paymentMode.getPaymentType());
                        }
                        Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(institutionData.getPaymentStatusId());
                        if (paymentStatusOptional.isPresent()){
                            PaymentStatus paymentStatus = paymentStatusOptional.get();
                            getUserData.setPaymentStatus(paymentStatus.getStatusName());
                        }
                        getUserData.setTicketId(institutionData.getTicketId());
                        getUserData.setGeneratedTime(institutionData.getCreatedTime());
                        Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(institutionData.getSlotId());
                        if (spotSlotOptional.isPresent()){
                            SpotSlot spotSlot = spotSlotOptional.get();
                            getUserData.setSlotTime(spotSlot.getSlotEndTime());
                        }
                        getUserData.setCreatedBy(institutionData.getCreatedBy());
//                        getUserData.setAdult(0);
//                        getUserData.setChild(0);
//                        getUserData.setSeniorCitizen(0);
                        getUserDataList.add(getUserData);
                    }
                    return new ResponseEntity<>(getUserDataList,HttpStatus.OK);
                }
            } else if ("Foreigner".equalsIgnoreCase(name)) {
                List<ForeignerData> foreignerDataList = foreignerDataRepo.findByVisitDate(visitDate);
                if (!foreignerDataList.isEmpty()){
                    for (ForeignerData foreignerData : foreignerDataList){
                        GetUserData_ getUserData = new GetUserData_();
                        getUserData.setName(foreignerData.getName());
                        getUserData.setPhNumber(foreignerData.getPhNumber());
                        getUserData.setAdult(foreignerData.getAdult());
                        getUserData.setChild(foreignerData.getChild());
                        getUserData.setGrandTotal(foreignerData.getGrandTotal());
                        Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(foreignerData.getPaymentMode());
                        if (paymentModeOptional.isPresent()){
                            PaymentMode paymentMode = paymentModeOptional.get();
                            getUserData.setPaymentModeName(paymentMode.getPaymentType());
                        }
                        Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(foreignerData.getPaymentStatusId());
                        if (paymentStatusOptional.isPresent()){
                            PaymentStatus paymentStatus = paymentStatusOptional.get();
                            getUserData.setPaymentStatus(paymentStatus.getStatusName());
                        }
                        getUserData.setTicketId(foreignerData.getTicketId());
                        getUserData.setGeneratedTime(foreignerData.getCreatedTime());
                        Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(foreignerData.getSlotId());
                        if (spotSlotOptional.isPresent()){
                            SpotSlot spotSlot = spotSlotOptional.get();
                            getUserData.setSlotTime(spotSlot.getSlotEndTime());
                        }
                        getUserData.setCreatedBy(foreignerData.getCreatedBy());
//                        getUserData.setSeniorCitizen(0);
//                        getUserData.setTeacher(0);
//                        getUserData.setStudent(0);
//                        getUserData.setDistrict("NoData");
                        getUserDataList.add(getUserData);
                    }
                    return new ResponseEntity<>(getUserDataList,HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<AllUserDataDto>> getUserDetailsByRangeOfDate(LocalDate startDate, LocalDate endDate, Integer categoryId) {
        List<AllUserDataDto> allUserDataDtoList = new ArrayList<>();
        AllUserDataDto allUserDataDto = new AllUserDataDto();
        Optional<CategoryData> categoryDataOptional = categoryRepo.findById(categoryId);
        if (categoryDataOptional.isPresent()){
            CategoryData categoryData = categoryDataOptional.get();
            String name = categoryData.getCategory();
            if ("Public".equalsIgnoreCase(name)){
                List<PublicData> publicDataList = publicRepo.findByVisitDateBetween(startDate,endDate);
                List<PublicDtoData> publicDtoDataList = new ArrayList<>();
                if (!publicDataList.isEmpty()){
                    for (PublicData publicData : publicDataList){
                        PublicDtoData publicDtoData = new PublicDtoData();
                        publicDtoData.setName(publicData.getName());
                        publicDtoData.setPhNumber(publicData.getPhNumber());
                        publicDtoData.setAdultCount(publicData.getAdult());
                        publicDtoData.setChildCount(publicData.getChild());
                        publicDtoData.setSeniorCitizenCount(publicData.getSeniorCitizen());
                        publicDtoData.setOrderId(publicData.getOrderId());

                        Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(publicData.getPaymentMode());
                        if (paymentModeOptional.isPresent()){
                            PaymentMode paymentMode = paymentModeOptional.get();
                            publicDtoData.setPaymentModeName(paymentMode.getPaymentType());
                        }

                        Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(publicData.getPaymentStatusId());
                        if (paymentStatusOptional.isPresent()){
                            PaymentStatus paymentStatus = paymentStatusOptional.get();
                            publicDtoData.setPaymentStatusName(paymentStatus.getStatusName());
                        }

                        publicDtoData.setVisitDate(publicData.getVisitDate());

                        Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(publicData.getSlotId());
                        if (spotSlotOptional.isPresent()){
                            SpotSlot spotSlot = spotSlotOptional.get();
                            publicDtoData.setSlotTime(spotSlot.getSlotEndTime());
                        }

                        publicDtoData.setGrandTotal(publicData.getGrandTotal());
                        publicDtoData.setTicketId(publicData.getTicketId());
                        publicDtoData.setPaymentId(publicData.getPaymentId());
                        publicDtoData.setGeneratedTime(publicData.getCreatedTime());
                        publicDtoData.setCreatedBy(publicData.getCreatedBy());

                        publicDtoDataList.add(publicDtoData);
                    }
                    allUserDataDto.setPublicDtoDataList(publicDtoDataList);
                    allUserDataDtoList.add(allUserDataDto);
                    return new ResponseEntity<>(allUserDataDtoList,HttpStatus.OK);
                }

            } else if ("Institution".equalsIgnoreCase(name)) {
                List<InstitutionDtoData> institutionDtoDataList = new ArrayList<>();
                List<InstitutionData> institutionDataList = institutionDataRepo.findByVisitDateBetween(startDate,endDate);
                if (!institutionDataList.isEmpty()){
                    for (InstitutionData institutionData : institutionDataList){
                        InstitutionDtoData institutionDtoData = new InstitutionDtoData();
                        institutionDtoData.setName(institutionData.getName());
                        institutionDtoData.setPhNumber(institutionData.getPhNumber());
                        institutionDtoData.setDistrict(institutionData.getDistrict());
                        institutionDtoData.setTeacherCount(institutionData.getTeacher());
                        institutionDtoData.setStudentCount(institutionData.getStudent());
                        institutionDtoData.setOrderId(institutionData.getOrderId());
                        institutionDtoData.setGrandTotal(institutionData.getGrandTotal());
                        Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(institutionData.getPaymentMode());
                        if (paymentModeOptional.isPresent()){
                            PaymentMode paymentMode = paymentModeOptional.get();
                            institutionDtoData.setPaymentModeName(paymentMode.getPaymentType());
                        }
                        Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(institutionData.getPaymentStatusId());
                        if (paymentStatusOptional.isPresent()){
                            PaymentStatus paymentStatus = paymentStatusOptional.get();
                            institutionDtoData.setPaymentStatusName(paymentStatus.getStatusName());
                        }
                        institutionDtoData.setVisitDate(institutionData.getVisitDate());
                        Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(institutionData.getSlotId());
                        if (spotSlotOptional.isPresent()){
                            SpotSlot spotSlot = spotSlotOptional.get();
                            institutionDtoData.setSlotTime(spotSlot.getSlotEndTime());
                        }
                        institutionDtoData.setTicketId(institutionData.getTicketId());
                        institutionDtoData.setOrderId(institutionData.getOrderId());
                        institutionDtoData.setPaymentId(institutionData.getPaymentId());
                        institutionDtoData.setCreatedBy(institutionData.getCreatedBy());
                        institutionDtoData.setGeneratedTime(institutionData.getCreatedTime());
                        institutionDtoDataList.add(institutionDtoData);
                    }
                    allUserDataDto.setInstitutionDtoDataList(institutionDtoDataList);
                    allUserDataDtoList.add(allUserDataDto);
                    return new ResponseEntity<>(allUserDataDtoList,HttpStatus.OK);
                }

            } else if ("Foreigner".equalsIgnoreCase(name)) {
                List<ForeignerDtoData> foreignerDtoDataList = new ArrayList<>();
                List<ForeignerData> foreignerDataList = foreignerDataRepo.findByVisitDateBetween(startDate,endDate);
                if (!foreignerDataList.isEmpty()){
                    for (ForeignerData foreignerData : foreignerDataList){
                        ForeignerDtoData foreignerDtoData = new ForeignerDtoData();
                        foreignerDtoData.setName(foreignerData.getName());
                        foreignerDtoData.setPhNumber(foreignerData.getPhNumber());
                        foreignerDtoData.setAdultCount(foreignerData.getAdult());
                        foreignerDtoData.setChildCount(foreignerData.getChild());

                        foreignerDtoData.setVisitDate(foreignerData.getVisitDate());
                        Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(foreignerData.getSlotId());
                        if (spotSlotOptional.isPresent()){
                            SpotSlot spotSlot = spotSlotOptional.get();
                            foreignerDtoData.setSlotTime(spotSlot.getSlotEndTime());
                        }
                        foreignerDtoData.setGrandTotal(foreignerData.getGrandTotal());
                        Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(foreignerData.getPaymentMode());
                        if (paymentModeOptional.isPresent()){
                            PaymentMode paymentMode = paymentModeOptional.get();
                            foreignerDtoData.setPaymentModeName(paymentMode.getPaymentType());
                        }
                        Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(foreignerData.getPaymentStatusId());
                        if (paymentStatusOptional.isPresent()){
                            PaymentStatus paymentStatus = paymentStatusOptional.get();
                            foreignerDtoData.setPaymentStatusName(paymentStatus.getStatusName());
                        }
                        foreignerDtoData.setTicketId(foreignerData.getTicketId());
                        foreignerDtoData.setOrderId(foreignerData.getOrderId());
                        foreignerDtoData.setPaymentId(foreignerDtoData.getPaymentId());
                        foreignerDtoData.setGeneratedTime(foreignerData.getCreatedTime());
                        foreignerDtoData.setCreatedBy(foreignerData.getCreatedBy());
                        foreignerDtoDataList.add(foreignerDtoData);
                    }
                    allUserDataDto.setForeignerDtoDataList(foreignerDtoDataList);
                    allUserDataDtoList.add(allUserDataDto);
                    return new ResponseEntity<>(allUserDataDtoList,HttpStatus.OK);
                }
            }
        }else {

            List<PublicData> publicDataList = publicRepo.findAll();
            if (!publicDataList.isEmpty()){
                List<PublicDtoData> publicDtoDataList = new ArrayList<>();
                for (PublicData publicData : publicDataList){
                    PublicDtoData publicDtoData = new PublicDtoData();
                    publicDtoData.setName(publicData.getName());
                    publicDtoData.setPhNumber(publicData.getPhNumber());
                    publicDtoData.setAdultCount(publicData.getAdult());
                    publicDtoData.setChildCount(publicData.getChild());
                    publicDtoData.setSeniorCitizenCount(publicData.getSeniorCitizen());
                    publicDtoData.setOrderId(publicData.getOrderId());
                    publicDtoData.setPaymentId(publicData.getPaymentId());
                    publicDtoData.setTicketId(publicData.getTicketId());

                    Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(publicData.getSlotId());
                    if (spotSlotOptional.isPresent()){
                        SpotSlot spotSlot = spotSlotOptional.get();
                        publicDtoData.setSlotTime(spotSlot.getSlotEndTime());
                    }
                    publicDtoData.setGrandTotal(publicData.getGrandTotal());
                    Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(publicData.getPaymentMode());
                    if (paymentModeOptional.isPresent()){
                        PaymentMode paymentMode = paymentModeOptional.get();
                        publicDtoData.setPaymentModeName(paymentMode.getPaymentType());
                    }
                    Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(publicData.getPaymentStatusId());
                    if (paymentStatusOptional.isPresent()){
                        PaymentStatus paymentStatus = paymentStatusOptional.get();
                        publicDtoData.setPaymentStatusName(paymentStatus.getStatusName());
                    }
                    publicDtoData.setTicketId(publicData.getTicketId());
                    publicDtoData.setOrderId(publicData.getOrderId());
                    publicDtoData.setPaymentId(publicData.getPaymentId());
                    publicDtoData.setGeneratedTime(publicData.getCreatedTime());
                    publicDtoData.setCreatedBy(publicData.getCreatedBy());
                    publicDtoDataList.add(publicDtoData);
                }
                allUserDataDto.setPublicDtoDataList(publicDtoDataList);

            }
            List<InstitutionData> institutionDataList = institutionDataRepo.findAll();
            if (!institutionDataList.isEmpty()){
                List<InstitutionDtoData> institutionDtoDataList = new ArrayList<>();
                for (InstitutionData institutionData : institutionDataList){
                    InstitutionDtoData institutionDtoData = new InstitutionDtoData();
                    institutionDtoData.setName(institutionData.getName());
                    institutionDtoData.setPhNumber(institutionData.getPhNumber());
                    institutionDtoData.setDistrict(institutionData.getDistrict());
                    institutionDtoData.setTeacherCount(institutionData.getTeacher());
                    institutionDtoData.setStudentCount(institutionData.getStudent());
                    institutionDtoData.setGrandTotal(institutionData.getGrandTotal());
                    Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(institutionData.getPaymentMode());
                    if (paymentModeOptional.isPresent()){
                        PaymentMode paymentMode = paymentModeOptional.get();
                        institutionDtoData.setPaymentModeName(paymentMode.getPaymentType());
                    }
                    Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(institutionData.getPaymentStatusId());
                    if (paymentStatusOptional.isPresent()){
                        PaymentStatus paymentStatus = paymentStatusOptional.get();
                        institutionDtoData.setPaymentStatusName(paymentStatus.getStatusName());
                    }
                    institutionDtoData.setVisitDate(institutionData.getVisitDate());
                    Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(institutionData.getSlotId());
                    if (spotSlotOptional.isPresent()){
                        SpotSlot spotSlot = spotSlotOptional.get();
                        institutionDtoData.setSlotTime(spotSlot.getSlotEndTime());
                    }
                    institutionDtoData.setTicketId(institutionData.getTicketId());
                    institutionDtoData.setOrderId(institutionData.getOrderId());
                    institutionDtoData.setPaymentId(institutionData.getPaymentId());
                    institutionDtoData.setCreatedBy(institutionData.getCreatedBy());
                    institutionDtoData.setGeneratedTime(institutionData.getCreatedTime());
                    institutionDtoDataList.add(institutionDtoData);
                }
                allUserDataDto.setInstitutionDtoDataList(institutionDtoDataList);

            }
            List<ForeignerData> foreignerDataList = foreignerDataRepo.findAll();
            List<ForeignerDtoData> foreignerDtoDataList = new ArrayList<>();
            if (!foreignerDataList.isEmpty()){
                for (ForeignerData foreignerData : foreignerDataList){
                    ForeignerDtoData foreignerDtoData = new ForeignerDtoData();
                    foreignerDtoData.setName(foreignerData.getName());
                    foreignerDtoData.setPhNumber(foreignerData.getPhNumber());
                    foreignerDtoData.setAdultCount(foreignerData.getAdult());
                    foreignerDtoData.setChildCount(foreignerData.getChild());

                    foreignerDtoData.setVisitDate(foreignerData.getVisitDate());
                    Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(foreignerData.getSlotId());
                    if (spotSlotOptional.isPresent()){
                        SpotSlot spotSlot = spotSlotOptional.get();
                        foreignerDtoData.setSlotTime(spotSlot.getSlotEndTime());
                    }
                    foreignerDtoData.setGrandTotal(foreignerData.getGrandTotal());
                    Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(foreignerData.getPaymentMode());
                    if (paymentModeOptional.isPresent()){
                        PaymentMode paymentMode = paymentModeOptional.get();
                        foreignerDtoData.setPaymentModeName(paymentMode.getPaymentType());
                    }
                    Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(foreignerData.getPaymentStatusId());
                    if (paymentStatusOptional.isPresent()){
                        PaymentStatus paymentStatus = paymentStatusOptional.get();
                        foreignerDtoData.setPaymentStatusName(paymentStatus.getStatusName());
                    }
                    foreignerDtoData.setTicketId(foreignerData.getTicketId());
                    foreignerDtoData.setOrderId(foreignerData.getOrderId());
                    foreignerDtoData.setPaymentId(foreignerDtoData.getPaymentId());
                    foreignerDtoData.setGeneratedTime(foreignerData.getCreatedTime());
                    foreignerDtoData.setCreatedBy(foreignerData.getCreatedBy());
                    foreignerDtoDataList.add(foreignerDtoData);
                }
                allUserDataDto.setForeignerDtoDataList(foreignerDtoDataList);
                allUserDataDtoList.add(allUserDataDto);
                return new ResponseEntity<>(allUserDataDtoList,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> CategoryBasedTotalRevenueByDate(LocalDate visitDate) {
        List<PublicData> publicDataList = publicRepo.findByVisitDate(visitDate);
        List<InstitutionData> institutionDataList = institutionDataRepo.findByVisitDate(visitDate);
        List<ForeignerData> foreignerDataList = foreignerDataRepo.findByVisitDate(visitDate);
        Double revenueAmount = 0.0,totalRevenue=0.0;
        GetRevenueDetails getRevenueDetails = new GetRevenueDetails();
        if (!publicDataList.isEmpty()){
            for (PublicData publicData : publicDataList){
                revenueAmount+=publicData.getGrandTotal();
            }
            totalRevenue+=revenueAmount;
            getRevenueDetails.setPublicRevenue(revenueAmount);
        }
        if (!institutionDataList.isEmpty()){
            for (InstitutionData institutionData : institutionDataList){
                revenueAmount+=institutionData.getGrandTotal();
            }
            totalRevenue+=revenueAmount;
            getRevenueDetails.setInstitutionRevenue(revenueAmount);
        }
        if (!foreignerDataList.isEmpty()){
            for (ForeignerData foreignerData :foreignerDataList){
                revenueAmount+=foreignerData.getGrandTotal();
            }
            totalRevenue+=revenueAmount;
            getRevenueDetails.setForeignerRevenue(revenueAmount);
            getRevenueDetails.setOverAllRevenue(totalRevenue);
        }
        return new ResponseEntity<>(getRevenueDetails,HttpStatus.OK);
    }

    public ResponseEntity<?> totalPublicVisitorsCountByDate(LocalDate vDate) {
        List<PublicData> publicDataList = publicRepo.findByVisitDate(vDate);
        Integer adultCount =0,childCount=0,seniorCitizenCount=0,count=0;
        List<PublicVisitorsDto> publicVisitorsDtoList = new ArrayList<>();
        if (!publicDataList.isEmpty()){

            PublicVisitorsDto publicVisitorsDto = new PublicVisitorsDto();
            for (PublicData publicData : publicDataList){
                adultCount+=publicData.getAdult();
                childCount+=publicData.getChild();
                seniorCitizenCount+=publicData.getSeniorCitizen();
                count++;
            }
            publicVisitorsDto.setAdultCount(adultCount);
            publicVisitorsDto.setChildCount(childCount);
            publicVisitorsDto.setSeniorCitizen(seniorCitizenCount);
            publicVisitorsDto.setPublicCount(count);
            publicVisitorsDtoList.add(publicVisitorsDto);
        }
        return new ResponseEntity<>(publicVisitorsDtoList,HttpStatus.OK);
    }

    public ResponseEntity<?> totalInstitutionVisitorsCountByDate(LocalDate vDate) {
        List<InstitutionData> institutionDataList = institutionDataRepo.findByVisitDate(vDate);
        Integer teacherCount =0,studentCount=0,count=0;
        List<InstitutionVisitorsDto> institutionVisitorsDtoList = new ArrayList<>();
        if (!institutionDataList.isEmpty()){
            InstitutionVisitorsDto institutionVisitorsDto = new InstitutionVisitorsDto();
            for (InstitutionData institutionData : institutionDataList){
                teacherCount+=institutionData.getTeacher();
                studentCount+=institutionData.getStudent();
                count++;
            }
            institutionVisitorsDto.setInstitutionCount(count);
            institutionVisitorsDto.setTeacherCount(teacherCount);
            institutionVisitorsDto.setStudnetCount(studentCount);
            institutionVisitorsDtoList.add(institutionVisitorsDto);
        }
        return new ResponseEntity<>(institutionVisitorsDtoList,HttpStatus.OK);
    }

    public ResponseEntity<?> totalForeignerVisitorsCountByDate(LocalDate vDate) {
        List<ForeignerData> foreignerDataList = foreignerDataRepo.findByVisitDate(vDate);
        Integer adultCount =0,childCount=0,count=0;
        List<ForeignerVisitorsDto> foreignerVisitorsDtoList = new ArrayList<>();
        if (!foreignerDataList.isEmpty()){
            ForeignerVisitorsDto foreignerVisitorsDto = new ForeignerVisitorsDto();
            for (ForeignerData foreignerData : foreignerDataList){
                adultCount+=foreignerData.getAdult();
                childCount+=foreignerData.getChild();
                count++;
            }
            foreignerVisitorsDto.setForeignerCount(count);
            foreignerVisitorsDto.setForeignAdult(adultCount);
            foreignerVisitorsDto.setForeignChild(childCount);
            foreignerVisitorsDtoList.add(foreignerVisitorsDto);
            return new ResponseEntity<>(foreignerVisitorsDto,HttpStatus.OK);
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> totalVisitorSCountByDate(LocalDate vDate) {
        List<PublicData> publicDataList = publicRepo.findByVisitDate(vDate);
        List<InstitutionData> institutionDataList = institutionDataRepo.findByVisitDate(vDate);
        List<ForeignerData> foreignerDataList = foreignerDataRepo.findByVisitDate(vDate);

        List<VisitsCountDto> visitsCountDtoList = new ArrayList<>();
        VisitsCountDto visitsCountDto = new VisitsCountDto();
        Integer adultCount =0,childCount=0,seniorCitizenCount=0,count=0;

        if (!publicDataList.isEmpty()){
            List<PublicVisitorsDto> publicVisitorsDtoList = new ArrayList<>();
            PublicVisitorsDto publicVisitorsDto = new PublicVisitorsDto();
            for (PublicData publicData : publicDataList){
                adultCount+=publicData.getAdult();
                childCount+=publicData.getChild();
                seniorCitizenCount+=publicData.getSeniorCitizen();
                count++;
            }
            publicVisitorsDto.setAdultCount(adultCount);
            publicVisitorsDto.setChildCount(childCount);
            publicVisitorsDto.setSeniorCitizen(seniorCitizenCount);
            publicVisitorsDto.setPublicCount(count);
            publicVisitorsDtoList.add(publicVisitorsDto);
            visitsCountDto.setPublicVisitorsDtoList(publicVisitorsDtoList);
            count=0;adultCount=0;childCount=0;
        }
        if (!institutionDataList.isEmpty()){
            List<InstitutionVisitorsDto> institutionVisitorsDtoList = new ArrayList<>();
            InstitutionVisitorsDto institutionVisitorsDto = new InstitutionVisitorsDto();
            for (InstitutionData institutionData:institutionDataList){
                adultCount+=institutionData.getTeacher();
                childCount+=institutionData.getStudent();
                count++;
            }
            institutionVisitorsDto.setTeacherCount(adultCount);
            institutionVisitorsDto.setStudnetCount(childCount);
            institutionVisitorsDto.setInstitutionCount(count);
            institutionVisitorsDtoList.add(institutionVisitorsDto);
            visitsCountDto.setInstitutionVisitorsDtoList(institutionVisitorsDtoList);
            count=0;adultCount=0;childCount=0;
        }
        if (!foreignerDataList.isEmpty()){
            List<ForeignerVisitorsDto> foreignerVisitorsDtoList = new ArrayList<>();
            ForeignerVisitorsDto foreignerVisitorsDto = new ForeignerVisitorsDto();
            for (ForeignerData foreignerData:foreignerDataList){
                adultCount+=foreignerData.getAdult();
                childCount+=foreignerData.getChild();
                count++;
            }
            foreignerVisitorsDto.setForeignAdult(adultCount);
            foreignerVisitorsDto.setForeignChild(childCount);
            foreignerVisitorsDto.setForeignerCount(count);
            foreignerVisitorsDtoList.add(foreignerVisitorsDto);
            visitsCountDto.setForeignerVisitorsDtoList(foreignerVisitorsDtoList);
            visitsCountDtoList.add(visitsCountDto);
        }
        return new ResponseEntity<>(visitsCountDtoList,HttpStatus.OK);
    }

    public ResponseEntity<List<VisitsCountDto>> visitorsCountByDateRange(LocalDate startDate, LocalDate endDate) {
        List<PublicData> publicDataList = publicRepo.findByVisitDateBetween(startDate,endDate);
        List<InstitutionData> institutionDataList = institutionDataRepo.findByVisitDateBetween(startDate,endDate);
        List<ForeignerData> foreignerDataList = foreignerDataRepo.findByVisitDateBetween(startDate,endDate);

        List<VisitsCountDto> visitsCountDtoList = new ArrayList<>();
        VisitsCountDto visitsCountDto = new VisitsCountDto();
        Integer adultCount =0,childCount=0,seniorCitizenCount=0,count=0;
        //VisitsCountDto visitsCountDto = new VisitsCountDto();
        if (!publicDataList.isEmpty()){
            List<PublicVisitorsDto> publicVisitorsDtoList = new ArrayList<>();
            PublicVisitorsDto publicVisitorsDto = new PublicVisitorsDto();
            for (PublicData publicData : publicDataList){
                adultCount+=publicData.getAdult();
                childCount+=publicData.getChild();
                seniorCitizenCount+=publicData.getSeniorCitizen();
                count++;
            }
            publicVisitorsDto.setPublicCount(count);
            publicVisitorsDto.setAdultCount(adultCount);
            publicVisitorsDto.setChildCount(childCount);
            publicVisitorsDto.setSeniorCitizen(seniorCitizenCount);
            publicVisitorsDtoList.add(publicVisitorsDto);
            visitsCountDto.setPublicVisitorsDtoList(publicVisitorsDtoList);
            adultCount=0;childCount=0;count=0;
        }
        if (!institutionDataList.isEmpty()){
            List<InstitutionVisitorsDto> institutionVisitorsDtoList = new ArrayList<>();
            InstitutionVisitorsDto institutionVisitorsDto = new InstitutionVisitorsDto();
            for (InstitutionData institutionData : institutionDataList){
                adultCount+=institutionData.getTeacher();
                childCount+=institutionData.getStudent();
                count++;
            }
            institutionVisitorsDto.setTeacherCount(adultCount);
            institutionVisitorsDto.setStudnetCount(childCount);
            institutionVisitorsDto.setInstitutionCount(count);
            institutionVisitorsDtoList.add(institutionVisitorsDto);
            visitsCountDto.setInstitutionVisitorsDtoList(institutionVisitorsDtoList);
            adultCount=0;childCount=0;count=0;
        }
        if (!foreignerDataList.isEmpty()){
            List<ForeignerVisitorsDto> foreignerVisitorsDtoList = new ArrayList<>();
            ForeignerVisitorsDto foreignerVisitorsDto = new ForeignerVisitorsDto();
            for (ForeignerData foreignerData : foreignerDataList){
                adultCount+= foreignerData.getAdult();
                childCount+= foreignerData.getChild();
                count++;
            }
            foreignerVisitorsDto.setForeignerCount(count);
            foreignerVisitorsDto.setForeignAdult(adultCount);
            foreignerVisitorsDto.setForeignChild(childCount);
            foreignerVisitorsDtoList.add(foreignerVisitorsDto);
            visitsCountDto.setForeignerVisitorsDtoList(foreignerVisitorsDtoList);
            visitsCountDtoList.add(visitsCountDto);
        }
        return new ResponseEntity<>(visitsCountDtoList,HttpStatus.OK);
    }
}
