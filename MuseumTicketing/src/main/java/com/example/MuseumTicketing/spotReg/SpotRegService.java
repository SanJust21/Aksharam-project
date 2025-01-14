package com.example.MuseumTicketing.spotReg;

import com.example.MuseumTicketing.Guide.util.AlphaNumeric;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingDetails;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingSpotRepo;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SpotSlot;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SpotSlotRepo;
import com.example.MuseumTicketing.spotReg.category.additionCharge.AdditionChargeRepo;
import com.example.MuseumTicketing.spotReg.category.category.CategoryData;
import com.example.MuseumTicketing.spotReg.category.category.CategoryRepo;
import com.example.MuseumTicketing.spotReg.category.discount.DiscountCount;
import com.example.MuseumTicketing.spotReg.category.discount.DiscountCountRepo;
import com.example.MuseumTicketing.spotReg.category.gst.GSTRepo;
import com.example.MuseumTicketing.spotReg.category.paymentMode.PaymentMode;
import com.example.MuseumTicketing.spotReg.category.paymentMode.PaymentModeRepo;
import com.example.MuseumTicketing.spotReg.category.paymentStatus.PaymentStatus;
import com.example.MuseumTicketing.spotReg.category.paymentStatus.PaymentStatusRepo;
import com.example.MuseumTicketing.spotReg.category.price.PriceData;
import com.example.MuseumTicketing.spotReg.category.price.PriceDataRepo;
import com.example.MuseumTicketing.spotReg.category.type.TypeData;
import com.example.MuseumTicketing.spotReg.category.type.TypeRepo;
import com.example.MuseumTicketing.spotReg.userData.SpotPaymentDto;
import com.example.MuseumTicketing.spotReg.userData.SpotUpdateDto;
import com.example.MuseumTicketing.spotReg.userData.SpotUserDto;
import com.example.MuseumTicketing.spotReg.userData.dashboardDTO.*;
import com.example.MuseumTicketing.spotReg.userData.Institution.InstitutionData;
import com.example.MuseumTicketing.spotReg.userData.Institution.InstitutionDataRepo;
import com.example.MuseumTicketing.spotReg.userData.dashboardDTO.count.*;
import com.example.MuseumTicketing.spotReg.userData.dashboardDTO.usesrDetails.AllUserDataDto;
import com.example.MuseumTicketing.spotReg.userData.dashboardDTO.usesrDetails.ForeignerDtoData;
import com.example.MuseumTicketing.spotReg.userData.dashboardDTO.usesrDetails.InstitutionDtoData;
import com.example.MuseumTicketing.spotReg.userData.dashboardDTO.usesrDetails.PublicDtoData;
import com.example.MuseumTicketing.spotReg.userData.foreigner.ForeignerData;
import com.example.MuseumTicketing.spotReg.userData.foreigner.ForeignerDataRepo;
import com.example.MuseumTicketing.spotReg.userData.publicUser.PublicData;
import com.example.MuseumTicketing.spotReg.userData.publicUser.PublicRepo;
import com.example.MuseumTicketing.spotReg.userData.TypeGrandTotalDto;
import com.google.zxing.WriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

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
    @Autowired
    private SpotQRcodeService spotQRcodeService;
    @Autowired
    private DiscountCountRepo discountCountRepo;

    //public static final Integer studentDiscountId=1;


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
        publicDetails.setAdultGrandTotal(totalAdultCharge);
        if (spotUserDto.getChild()>0){ //calculating total child ticket charge
            typeId=spotUserDto.getChildTypeId();
            userCount = spotUserDto.getChild();
            totalChildCharge = amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        publicDetails.setChildGrandTotal(totalChildCharge);
        if (spotUserDto.getSeniorCitizen()>0){ // calculating total senior citizen ticket charge.
            typeId = spotUserDto.getSeniorCitizenTypeId();
            userCount = spotUserDto.getSeniorCitizen();
            totalSeniorCitizenCharge = amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        publicDetails.setSeniorCitizenGrandTotal(totalSeniorCitizenCharge);
        //total ticket charge by total no.of adult + total no.of child + total no.of senior citizen
        Double totalCharges = totalAdultCharge+totalChildCharge+totalSeniorCitizenCharge;
        Double totalGstRate =0.0; Double totalUserGst;
        totalGstRate = amountCalculation.CalculateGST(); // calculating GST charge
        totalUserGst = totalGstRate*totalCharges;   // total user GST charge
        Integer extraCharge= amountCalculation.calculateAdditionalCharges(); // additional charges
        // calculating grandTotal = totalUserGST charge + additionalCharge + totalTicket charge
        grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);
        BigDecimal roundedGrandTotal = BigDecimal.valueOf(grandTotal).setScale(0, RoundingMode.HALF_UP);
        grandTotal=roundedGrandTotal.doubleValue();
        publicDetails.setTotalAmount(totalCharges);
        publicDetails.setTotalGstCharge(totalUserGst);
        publicDetails.setTotalAdditionalCharges(extraCharge);
        publicDetails.setGrandTotal(grandTotal);
        publicDetails.setPaymentMode(spotUserDto.getPaymentMode());
        publicDetails.setOrderId(alphaNumeric.generateRandomNumber());
        Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(spotUserDto.getPaymentStatusId());
        if (paymentStatusOptional.isPresent()){
            PaymentStatus paymentStatus = paymentStatusOptional.get();
            if ("Pending".equalsIgnoreCase(paymentStatus.getStatusName())){
                publicDetails.setPaymentStatusId(paymentStatus.getId());
            }
        }
        log.info("log PublicDetails : "+publicDetails);
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

        //calculateStudent DiscountRate.
        Double discountAmount=0.0,payableStudentCharge=0.0;
//        Optional<DiscountCount> discountCountOptional = discountCountRepo.findById(studentDiscountId);

        //discount calculation for student
        Optional<DiscountCount> discountCountOptional = discountCountRepo.findByCategoryIdAndTypeId(category,spotUserDto.getStudentTypeId());
        if (discountCountOptional.isPresent()){
            DiscountCount discountCount = discountCountOptional.get();

                //check studentCount > discountCount of student
                if (spotUserDto.getStudent()>discountCount.getDisCount()){
                    Double disAmountNew=((double)spotUserDto.getDiscountRate()/100);
                    log.info("discountAmount: "+disAmountNew);
                    institutionData.setDiscountAmount(disAmountNew);
                    discountAmount= totalStudentCharge*disAmountNew;
                    log.info("discount less Result : "+discountAmount);
                    institutionData.setStudentDiscount(discountAmount);
                    payableStudentCharge = totalStudentCharge-discountAmount;
                    institutionData.setStudentTicketCharge(totalStudentCharge);
                    institutionData.setTeacherTicketCharge(totalTeacherCharge);
                    institutionData.setPayableStudentCharge(payableStudentCharge);

                    // total ticket charge = total student ticket charge + total teachers ticket charge
                    Double totalCharges = payableStudentCharge+totalTeacherCharge;

                    //calculate total user GST charge
                    Double totalGstRate =amountCalculation.CalculateGST(),totalUserGst;
                    totalUserGst = totalGstRate*totalCharges;

                    //calculating additional chagres
                    Integer extraCharge= amountCalculation.calculateAdditionalCharges();
                    //Grand total = total GST charge + total additional charge + total ticket charge
                    grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);

                    BigDecimal roundedGrandTotal = BigDecimal.valueOf(grandTotal).setScale(0,RoundingMode.HALF_UP);
                    grandTotal= roundedGrandTotal.doubleValue();
                    institutionData.setTotalAmount(totalCharges);
                    institutionData.setPaymentMode(spotUserDto.getPaymentMode());
                    institutionData.setTotalGstCharge(totalUserGst);
                    institutionData.setTotalAdditionalCharges(extraCharge);
                    institutionData.setGrandTotal(grandTotal);
                    institutionData.setOrderId(alphaNumeric.generateRandomNumber());
                    Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(spotUserDto.getPaymentStatusId());
                    if (paymentStatusOptional.isPresent()){
                        PaymentStatus paymentStatus = paymentStatusOptional.get();
                        if ("Pending".equalsIgnoreCase(paymentStatus.getStatusName())){
                            institutionData.setPaymentStatusId(paymentStatus.getId());
                        }
                    }
                    log.info("Info institutionData : "+institutionData);
                    institutionDataRepo.save(institutionData);
                    return new ResponseEntity<>(institutionData,HttpStatus.OK);
                }else {
                    // total ticket charge = total student ticket charge + total teachers ticket charge
                    Double totalCharges = totalStudentCharge+totalTeacherCharge;
                    institutionData.setStudentTicketCharge(totalStudentCharge);
                    institutionData.setTeacherTicketCharge(totalTeacherCharge);
                    institutionData.setPayableStudentCharge(totalStudentCharge);
                    institutionData.setDiscountAmount((double) spotUserDto.getDiscountRate());

                    //calculate total user GST charge
                    Double totalGstRate =amountCalculation.CalculateGST(),totalUserGst;
                    totalUserGst = totalGstRate*totalCharges;

                    //calculating additional chagres
                    Integer extraCharge= amountCalculation.calculateAdditionalCharges();

                    //Grand total = total GST charge + total additional charge + total ticket charge
                    grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);
                    BigDecimal roundedGrandTotal = BigDecimal.valueOf(grandTotal).setScale(0,RoundingMode.HALF_UP);
                    grandTotal= roundedGrandTotal.doubleValue();
                    institutionData.setTotalAmount(totalCharges);
                    institutionData.setPaymentMode(spotUserDto.getPaymentMode());
                    institutionData.setTotalGstCharge(totalUserGst);
                    institutionData.setTotalAdditionalCharges(extraCharge);
                    institutionData.setGrandTotal(grandTotal);
                    institutionData.setOrderId(alphaNumeric.generateRandomNumber());
                    Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(spotUserDto.getPaymentStatusId());
                    if (paymentStatusOptional.isPresent()){
                        PaymentStatus paymentStatus = paymentStatusOptional.get();
                        if ("Pending".equalsIgnoreCase(paymentStatus.getStatusName())){
                            institutionData.setPaymentStatusId(paymentStatus.getId());
                        }
                    }
                    log.info("Info institutionData : "+institutionData);
                    institutionDataRepo.save(institutionData);
                    return new ResponseEntity<>(institutionData,HttpStatus.OK);
                }

        }else {
            return new ResponseEntity<>("studentDiscountId is not present in discount Table ",HttpStatus.NOT_FOUND);
        }
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
        foreignerData.setAdultGrandTotal(totalAdultCharge);
        if (spotUserDto.getChild()>0){      // calculating ticket charge of foreign child ticket charge
            typeId=spotUserDto.getChildTypeId();
            userCount = spotUserDto.getChild();

            totalChildCharge=amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        foreignerData.setChildGrandTotal(totalChildCharge);
        Double totalCharges=totalAdultCharge+totalChildCharge;      // total ticket charge = adult charge + child charge

        //calculating GST charge
        Double totalGstRate =amountCalculation.CalculateGST(),totalUserGst;
        totalUserGst = totalGstRate*totalCharges;

        //Calculating additional charge
        Integer extraCharge= amountCalculation.calculateAdditionalCharges();

        //grand total = GST + additionalCharge + ticketCharge
        grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);
        BigDecimal roundedGrandTotal = BigDecimal.valueOf(grandTotal).setScale(0,RoundingMode.HALF_UP);
        grandTotal= roundedGrandTotal.doubleValue();
        foreignerData.setTotalAmount(totalCharges);
        foreignerData.setTotalGstCharge(totalUserGst);
        foreignerData.setTotalAdditionalCharges(extraCharge);
        foreignerData.setGrandTotal(grandTotal);
        foreignerData.setPaymentMode(spotUserDto.getPaymentMode());
        foreignerData.setOrderId(alphaNumeric.generateRandomNumber());
        Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(spotUserDto.getPaymentStatusId());
        if (paymentStatusOptional.isPresent()){
            PaymentStatus paymentStatus = paymentStatusOptional.get();
            if ("Pending".equalsIgnoreCase(paymentStatus.getStatusName())){
                foreignerData.setPaymentStatusId(paymentStatus.getId());
            }
        }
        log.info("info foreigner Data : "+foreignerData);
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
            publicData.setPaymentMode(spotUpdateDto.getPaymentModeId());
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
            publicData.setAdultGrandTotal(totalAdultCharge);
            publicData.setChildGrandTotal(totalChildCharge);
            publicData.setSeniorCitizenGrandTotal(totalSeniorCitizenCharge);
            Double totalGstRate =0.0; Double totalUserGst;
            totalGstRate = amountCalculation.CalculateGST(); // calculating GST charge
            totalUserGst = totalGstRate*totalCharges;   // total user GST charge
            Integer extraCharge= amountCalculation.calculateAdditionalCharges(); // additional charges
            // calculating grandTotal = totalUserGST charge + additionalCharge + totalTicket charge
            grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);
            BigDecimal roundedGrandTotal = BigDecimal.valueOf(grandTotal).setScale(0,RoundingMode.HALF_UP);
            grandTotal= roundedGrandTotal.doubleValue();
            publicData.setTotalAmount(totalCharges);
            publicData.setTotalGstCharge(totalUserGst);
            publicData.setTotalAdditionalCharges(extraCharge);
            publicData.setGrandTotal(grandTotal);
            publicRepo.save(publicData);
            return new ResponseEntity<>(publicData,HttpStatus.OK);
        } else if (institutionDataOptional.isPresent()) {
            InstitutionData institutionData = institutionDataOptional.get();
            institutionData.setTeacher(spotUpdateDto.getTeacher());
            institutionData.setStudent(spotUpdateDto.getStudent());
            institutionData.setPaymentMode(spotUpdateDto.getPaymentModeId());
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

            //calculateStudent DiscountRate.
            Double discountAmount=0.0,payableStudentCharge=0.0;
            //discount calculation for student
            Optional<DiscountCount> discountCountOptional = discountCountRepo.findByCategoryIdAndTypeId(categoryId,spotUpdateDto.getStudentTypeId());
            if (discountCountOptional.isPresent()){
                DiscountCount discountCount = discountCountOptional.get();

                //check studentCount > discountCount of student
                if (spotUpdateDto.getStudent()>discountCount.getDisCount()){
                    Double disAmountNew=((double)spotUpdateDto.getDiscountRate()/100);
                    log.info("discountAmount: "+disAmountNew);
                    institutionData.setDiscountAmount(disAmountNew);
                    discountAmount= totalStudentCharge*disAmountNew;
                    log.info("discount less Result : "+discountAmount);
                    institutionData.setStudentDiscount(discountAmount);
                    payableStudentCharge = totalStudentCharge-discountAmount;
                    institutionData.setStudentTicketCharge(totalStudentCharge);
                    institutionData.setTeacherTicketCharge(totalTeacherCharge);
                    institutionData.setPayableStudentCharge(payableStudentCharge);

                    // total ticket charge = total student ticket charge + total teachers ticket charge
                    Double totalCharges = payableStudentCharge+totalTeacherCharge;

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

                    log.info("Info institutionData : "+institutionData);
                    institutionDataRepo.save(institutionData);
                    return new ResponseEntity<>(institutionData,HttpStatus.OK);
                }else {

                    // total ticket charge = total student ticket charge + total teachers ticket charge
                    Double totalCharges = totalStudentCharge+totalTeacherCharge;
                    institutionData.setStudentTicketCharge(totalStudentCharge);
                    institutionData.setDiscountAmount((double)spotUpdateDto.getDiscountRate());
                    institutionData.setPayableStudentCharge(totalStudentCharge);
                    institutionData.setTeacherTicketCharge(totalTeacherCharge);

                    //calculate total user GST charge
                    Double totalGstRate =amountCalculation.CalculateGST(),totalUserGst;
                    totalUserGst = totalGstRate*totalCharges;

                    //calculating additional chagres
                    Integer extraCharge= amountCalculation.calculateAdditionalCharges();

                    //Grand total = total GST charge + total additional charge + total ticket charge
                    grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);
                    BigDecimal roundedGrandTotal = BigDecimal.valueOf(grandTotal).setScale(0,RoundingMode.HALF_UP);
                    grandTotal= roundedGrandTotal.doubleValue();
                    institutionData.setTotalAmount(totalCharges);
                    institutionData.setTotalGstCharge(totalUserGst);
                    institutionData.setTotalAdditionalCharges(extraCharge);
                    institutionData.setGrandTotal(grandTotal);
                    institutionDataRepo.save(institutionData);
                    return new ResponseEntity<>(institutionData,HttpStatus.OK);
                }

            }else {
                return new ResponseEntity<>("No discount Details ",HttpStatus.NO_CONTENT);
            }

        } else if (foreignerDataOptional.isPresent()) {
            ForeignerData foreignerData = foreignerDataOptional.get();
            foreignerData.setAdult(spotUpdateDto.getAdult());
            foreignerData.setChild(spotUpdateDto.getChild());
            foreignerData.setPaymentMode(spotUpdateDto.getPaymentModeId());
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

            foreignerData.setAdultGrandTotal(totalAdultCharge);
            foreignerData.setChildGrandTotal(totalChildCharge);
            //calculating GST charge
            Double totalGstRate =amountCalculation.CalculateGST(),totalUserGst;
            totalUserGst = totalGstRate*totalCharges;

            //Calculating additional charge
            Integer extraCharge= amountCalculation.calculateAdditionalCharges();

            //grand total = GST + additionalCharge + ticketCharge
            grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);
            BigDecimal roundedGrandTotal = BigDecimal.valueOf(grandTotal).setScale(0,RoundingMode.HALF_UP);
            grandTotal= roundedGrandTotal.doubleValue();
            foreignerData.setTotalAmount(totalCharges);
            foreignerData.setTotalGstCharge(totalUserGst);
            foreignerData.setTotalAdditionalCharges(extraCharge);
            foreignerData.setGrandTotal(grandTotal);
            foreignerDataRepo.save(foreignerData);
            return new ResponseEntity<>(foreignerData,HttpStatus.OK);
        }
        return new ResponseEntity<>("OrderId : "+orderId+" isn't valid",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> getAllRegistrationDetails(Integer categoryId) {
        Optional<CategoryData> categoryDataOptional = categoryRepo.findById(categoryId);
        if (categoryDataOptional.isPresent()){
            CategoryData categoryData = categoryDataOptional.get();
            if ("Public".equalsIgnoreCase(categoryData.getCategory())){
                return new ResponseEntity<>(publicRepo.findAll(),HttpStatus.OK);
            } else if ("Institution".equalsIgnoreCase(categoryData.getCategory())) {
                return new ResponseEntity<>(institutionDataRepo.findAll(),HttpStatus.OK);
            } else if ("Foreigner".equalsIgnoreCase(categoryData.getCategory())) {
                return new ResponseEntity<>(foreignerDataRepo.findAll(),HttpStatus.OK);
            }
        }return new ResponseEntity<>("CategoryId isn't valid.",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> deletePartialRegistration(Integer categoryId, Long deleteId) {
        Optional<CategoryData> categoryDataOptional = categoryRepo.findById(categoryId);
        if (categoryDataOptional.isPresent()){
            CategoryData categoryData = categoryDataOptional.get();
            if ("Public".equalsIgnoreCase(categoryData.getCategory())){
                Optional<PublicData> publicDataOptional = publicRepo.findById(deleteId);
                if (publicDataOptional.isPresent()){
                    PublicData publicData = publicDataOptional.get();
                    String name = publicData.getName();
                    publicRepo.delete(publicData);
                    return new ResponseEntity<>(name+"'s details are deleted",HttpStatus.OK);
                }return new ResponseEntity<>("id : "+deleteId+" isn't present",HttpStatus.NOT_FOUND);
            } else if ("Institution".equalsIgnoreCase(categoryData.getCategory())) {
                Optional<InstitutionData> institutionDataOptional = institutionDataRepo.findById(deleteId);
                if (institutionDataOptional.isPresent()){
                    InstitutionData institutionData = institutionDataOptional.get();
                    String name = institutionData.getName();
                    institutionDataRepo.delete(institutionData);
                    return new ResponseEntity<>(name+"'s details are deleted",HttpStatus.OK);
                }return new ResponseEntity<>("id : "+deleteId+" isn't present",HttpStatus.NOT_FOUND);
            } else if ("Foreigner".equalsIgnoreCase(categoryData.getCategory())) {
                Optional<ForeignerData> foreignerDataOptional = foreignerDataRepo.findById(deleteId);
                if (foreignerDataOptional.isPresent()){
                    ForeignerData foreignerData = foreignerDataOptional.get();
                    String name = foreignerData.getName();
                    foreignerDataRepo.delete(foreignerData);
                    return new ResponseEntity<>(name+"'s details are deleted",HttpStatus.OK);
                }return new ResponseEntity<>("id : "+deleteId+" isn't present",HttpStatus.NOT_FOUND);
            }
        }return new ResponseEntity<>("CategoryId isn't valid.",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<SpotBookingDto> confirmPaymentDetails(String orderId, SpotPaymentDto spotPaymentDto,Integer totalUserCount)throws WriterException, IOException {
        SpotBookingDto spotBookingDto = new SpotBookingDto();
        Optional<PublicData> publicDataOptional = publicRepo.findByOrderId(orderId);
        Optional<InstitutionData> institutionDataOptional = institutionDataRepo.findByOrderId(orderId);
        Optional<ForeignerData> foreignerDataOptional = foreignerDataRepo.findByOrderId(orderId);

        if (publicDataOptional.isPresent()){
           Optional<BookingDetails> bookingDetailsOptional = bookingSpotRepo.findByBookDateAndSlotId(spotPaymentDto.getVisitDate(),spotPaymentDto.getSlotId());
           if (bookingDetailsOptional.isPresent()){
               BookingDetails bookingDetails = bookingDetailsOptional.get();
               if (bookingDetails.getPresentCapacity()>0){
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
                       if ("cash".equalsIgnoreCase(modeName) || "QRCode".equalsIgnoreCase(modeName) && "received".equalsIgnoreCase(name)){
                           publicData.setTicketId(alphaNumeric.generateSpotRandomNumber());
                           publicData.setPaymentId(alphaNumeric.generateRandomNumber());
                           publicData.setCreatedTime(LocalTime.now());

                           // if the user booked the tickets then reduce  the present slot capacity by userCount.
                           BookingDetails bookingDetail = amountCalculation.generateBookingDate(spotPaymentDto.getVisitDate(),spotPaymentDto.getSlotId(),totalUserCount);
                           publicData.setSlotId(bookingDetail.getSlotId());
                           publicData.setVisitDate(bookingDetail.getBookDate());
                       }else {
                           publicData.setTicketId(null);
                       }
                   }
                   publicData.setCreatedBy(spotPaymentDto.getCreatedBy());
                   publicData.setCountOfPeople(totalUserCount);
                   publicRepo.save(publicData);
                   spotBookingDto.setName(publicData.getName());
                   spotBookingDto.setPhNumber(publicData.getPhNumber());
                   spotBookingDto.setAdultCount(publicData.getAdult());
                   spotBookingDto.setChildCount(publicData.getChild());
                   spotBookingDto.setSeniorCitizenCount(publicData.getSeniorCitizen());
                   spotBookingDto.setVisitDate(publicData.getVisitDate());
                   Optional<BookingDetails> bookingDetails1 = bookingSpotRepo.findByBookDateAndSlotId(publicData.getVisitDate(), publicData.getSlotId());
                   if (bookingDetails1.isPresent()){
                       BookingDetails bookingDetails2 = bookingDetails1.get();
                       spotBookingDto.setSlotStartTime(bookingDetails2.getSlotStartTime());
                   }
                   spotBookingDto.setTotalAmount(publicData.getTotalAmount());
                   spotBookingDto.setTotalGstCharge(publicData.getTotalGstCharge());
                   spotBookingDto.setTotalAdditionalCharges(publicData.getTotalAdditionalCharges());
                   spotBookingDto.setGrandTotal(publicData.getGrandTotal());
                   spotBookingDto.setOrderId(publicData.getOrderId());
                   spotBookingDto.setPaymentId(publicData.getPaymentId());
                   spotBookingDto.setTicketId(publicData.getTicketId());
                   spotBookingDto.setCreatedTime(publicData.getCreatedTime());
                   spotBookingDto.setQrCodeImage(spotQRcodeService.generateQRCode(publicData.getTicketId()));
                   return new ResponseEntity<>(spotBookingDto,HttpStatus.OK);

               }return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
           }
        } else if (institutionDataOptional.isPresent()) {
            Optional<BookingDetails> bookingDetailsOptional = bookingSpotRepo.findByBookDateAndSlotId(spotPaymentDto.getVisitDate(),spotPaymentDto.getSlotId());
            if (bookingDetailsOptional.isPresent()){
                BookingDetails bookingDetails = bookingDetailsOptional.get();
                if (bookingDetails.getPresentCapacity()>0){
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
                        if ("cash".equalsIgnoreCase(modeName) || "QRCode".equalsIgnoreCase(modeName) && "received".equalsIgnoreCase(name)){
                            institutionData.setTicketId(alphaNumeric.generateSpotRandomNumber());
                            institutionData.setPaymentId(alphaNumeric.generateRandomNumber());
                            institutionData.setCreatedTime(LocalTime.now());
                        }else {
                            institutionData.setTicketId(null);
                        }
                    }

                    // if the user booked the tickets then reduce  the present slot capacity by userCount.
                    BookingDetails booking = amountCalculation.generateBookingDate(spotPaymentDto.getVisitDate(),spotPaymentDto.getSlotId(),totalUserCount);
                    institutionData.setVisitDate(booking.getBookDate());
                    institutionData.setSlotId(booking.getSlotId());
                    institutionData.setCreatedBy(spotPaymentDto.getCreatedBy());
                    institutionData.setCountOfPeople(totalUserCount);
                    institutionDataRepo.save(institutionData);
                    spotBookingDto.setName(institutionData.getName());
                    spotBookingDto.setPhNumber(institutionData.getPhNumber());
                    spotBookingDto.setDistrict(institutionData.getDistrict());
                    spotBookingDto.setTeacherCount(institutionData.getTeacher());
                    spotBookingDto.setTeacherTicketCharge(institutionData.getTeacherTicketCharge());
                    spotBookingDto.setStudentTicketCharge(institutionData.getStudentTicketCharge());
                    spotBookingDto.setPayableStudentCharge(institutionData.getPayableStudentCharge());
                    spotBookingDto.setDiscountAmount(institutionData.getDiscountAmount());
                    spotBookingDto.setStudentCount(institutionData.getStudent());
                    spotBookingDto.setVisitDate(institutionData.getVisitDate());
                    Optional<BookingDetails> bookingDetails1 = bookingSpotRepo.findByBookDateAndSlotId(institutionData.getVisitDate(), institutionData.getSlotId());
                    if (bookingDetails1.isPresent()){
                        BookingDetails bookingDetails2 = bookingDetails1.get();
                        spotBookingDto.setSlotStartTime(bookingDetails2.getSlotStartTime());
                    }
                    spotBookingDto.setTotalAmount(institutionData.getTotalAmount());
                    spotBookingDto.setTotalGstCharge(institutionData.getTotalGstCharge());
                    spotBookingDto.setTotalAdditionalCharges(institutionData.getTotalAdditionalCharges());
                    spotBookingDto.setGrandTotal(institutionData.getGrandTotal());
                    spotBookingDto.setOrderId(institutionData.getOrderId());
                    spotBookingDto.setPaymentId(institutionData.getPaymentId());
                    spotBookingDto.setTicketId(institutionData.getTicketId());
                    spotBookingDto.setCreatedTime(institutionData.getCreatedTime());
                    spotBookingDto.setQrCodeImage(spotQRcodeService.generateQRCode(institutionData.getTicketId()));
                    return new ResponseEntity<>(spotBookingDto,HttpStatus.OK);
                }
            }



        } else if (foreignerDataOptional.isPresent()) {
            Optional<BookingDetails> bookingDetailsOptional = bookingSpotRepo.findByBookDateAndSlotId(spotPaymentDto.getVisitDate(),spotPaymentDto.getSlotId());

            if (bookingDetailsOptional.isPresent()){
                BookingDetails bookingDetails = bookingDetailsOptional.get();
                if (bookingDetails.getPresentCapacity()>0){
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
                        if ("cash".equalsIgnoreCase(modeName) || "QRCode".equalsIgnoreCase(modeName) && "received".equalsIgnoreCase(name)){
                            foreignerData.setTicketId(alphaNumeric.generateSpotRandomNumber());
                            foreignerData.setPaymentId(alphaNumeric.generateRandomNumber());
                            foreignerData.setCreatedTime(LocalTime.now());
                        }else {
                            foreignerData.setTicketId(null);
                        }
                    }

                    // if the user booked the tickets then reduce  the present slot capacity by userCount.
                    BookingDetails bookingDetail = amountCalculation.generateBookingDate(spotPaymentDto.getVisitDate(),spotPaymentDto.getSlotId(),totalUserCount);
                    foreignerData.setVisitDate(bookingDetail.getBookDate());
                    foreignerData.setSlotId(bookingDetail.getSlotId());
                    foreignerData.setCreatedBy(spotPaymentDto.getCreatedBy());
                    foreignerData.setCountOfPeople(totalUserCount);
                    foreignerDataRepo.save(foreignerData);
                    spotBookingDto.setName(foreignerData.getName());
                    spotBookingDto.setPhNumber(foreignerData.getPhNumber());
                    spotBookingDto.setAdultCount(foreignerData.getAdult());
                    spotBookingDto.setChildCount(foreignerData.getChild());
                    spotBookingDto.setVisitDate(foreignerData.getVisitDate());
                    Optional<BookingDetails> bookingDetails1 = bookingSpotRepo.findByBookDateAndSlotId(foreignerData.getVisitDate(),foreignerData.getSlotId());
                    if (bookingDetails1.isPresent()){
                        BookingDetails bookingDetails2 = bookingDetails1.get();
                        spotBookingDto.setSlotStartTime(bookingDetails2.getSlotStartTime());
                    }
                    spotBookingDto.setTotalAmount(foreignerData.getTotalAmount());
                    spotBookingDto.setTotalGstCharge(foreignerData.getTotalGstCharge());
                    spotBookingDto.setTotalAdditionalCharges(foreignerData.getTotalAdditionalCharges());
                    spotBookingDto.setGrandTotal(foreignerData.getGrandTotal());
                    spotBookingDto.setOrderId(foreignerData.getOrderId());
                    spotBookingDto.setPaymentId(foreignerData.getPaymentId());
                    spotBookingDto.setTicketId(foreignerData.getTicketId());
                    spotBookingDto.setCreatedTime(foreignerData.getCreatedTime());
                    spotBookingDto.setQrCodeImage(spotQRcodeService.generateQRCode(foreignerData.getTicketId()));
                    return new ResponseEntity<>(spotBookingDto,HttpStatus.OK);
                }return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
            }return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> getAllUserDetails() {
        List<AllUserDataDto> allUserDataDtoList = new ArrayList<>();
        List<PublicData> publicDataList = publicRepo.findAll();

        if (!publicDataList.isEmpty()){
//            List<PublicDtoData> publicDtoDataList = new ArrayList<>();
            for (PublicData publicData : publicDataList){
                AllUserDataDto allUserDataDto = new AllUserDataDto();
                allUserDataDto.setName(publicData.getName());
                allUserDataDto.setPhNumber(publicData.getPhNumber());
                allUserDataDto.setAdultCount(publicData.getAdult());
                allUserDataDto.setAdultCharge(publicData.getAdultGrandTotal());
                allUserDataDto.setChildCount(publicData.getChild());
                allUserDataDto.setChildCharge(publicData.getChildGrandTotal());
                allUserDataDto.setSeniorCitizenCount(publicData.getSeniorCitizen());
                allUserDataDto.setOrderId(publicData.getOrderId());
                allUserDataDto.setPaymentId(publicData.getPaymentId());
                allUserDataDto.setTicketId(publicData.getTicketId());

                Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(publicData.getSlotId());
                if (spotSlotOptional.isPresent()){
                    SpotSlot spotSlot = spotSlotOptional.get();
                    allUserDataDto.setVisitDate(publicData.getVisitDate());
                    allUserDataDto.setSlotTime(spotSlot.getSlotStartTime());
                }
                allUserDataDto.setGrandTotal(publicData.getGrandTotal());
                Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(publicData.getPaymentMode());
                if (paymentModeOptional.isPresent()){
                    PaymentMode paymentMode = paymentModeOptional.get();
                    allUserDataDto.setPaymentModeName(paymentMode.getPaymentType());
                }
                Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(publicData.getPaymentStatusId());
                if (paymentStatusOptional.isPresent()){
                    PaymentStatus paymentStatus = paymentStatusOptional.get();
                    allUserDataDto.setPaymentStatusName(paymentStatus.getStatusName());
                }
                allUserDataDto.setTicketId(publicData.getTicketId());
                allUserDataDto.setOrderId(publicData.getOrderId());
                allUserDataDto.setPaymentId(publicData.getPaymentId());
                allUserDataDto.setGeneratedTime(publicData.getCreatedTime());
                allUserDataDto.setCreatedBy(publicData.getCreatedBy());
                allUserDataDto.setVisitStatus(publicData.isVisitStatus());
                allUserDataDto.setCategoryName("Public");
                allUserDataDtoList.add(allUserDataDto);
            }
//            allUserDataDto.setPublicDtoDataList(publicDtoDataList);
            //allUserDataDtoList.add(allUserDataDto);
        }
        List<InstitutionData> institutionDataList = institutionDataRepo.findAll();
        if (!institutionDataList.isEmpty()){

//            List<InstitutionDtoData> institutionDtoDataList = new ArrayList<>();
            for (InstitutionData institutionData : institutionDataList){
                AllUserDataDto allUserDataDto = new AllUserDataDto();
                allUserDataDto.setName(institutionData.getName());
                allUserDataDto.setPhNumber(institutionData.getPhNumber());
                allUserDataDto.setDistrict(institutionData.getDistrict());
                allUserDataDto.setTeacherCount(institutionData.getTeacher());
                allUserDataDto.setTeacherCharge(institutionData.getTeacherTicketCharge());
                allUserDataDto.setStudentCount(institutionData.getStudent());
                allUserDataDto.setStudentCharge(institutionData.getStudentTicketCharge());
                allUserDataDto.setStudentDiscount(institutionData.getStudentDiscount());
                allUserDataDto.setPayableStudentCharge(institutionData.getPayableStudentCharge());
                allUserDataDto.setDiscountAmount(institutionData.getDiscountAmount());
                allUserDataDto.setGrandTotal(institutionData.getGrandTotal());
                Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(institutionData.getPaymentMode());
                if (paymentModeOptional.isPresent()){
                    PaymentMode paymentMode = paymentModeOptional.get();
                    allUserDataDto.setPaymentModeName(paymentMode.getPaymentType());
                }
                Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(institutionData.getPaymentStatusId());
                if (paymentStatusOptional.isPresent()){
                    PaymentStatus paymentStatus = paymentStatusOptional.get();
                    allUserDataDto.setPaymentStatusName(paymentStatus.getStatusName());
                }
                allUserDataDto.setVisitDate(institutionData.getVisitDate());
                Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(institutionData.getSlotId());
                if (spotSlotOptional.isPresent()){
                    SpotSlot spotSlot = spotSlotOptional.get();
                    allUserDataDto.setVisitDate(institutionData.getVisitDate());
                    allUserDataDto.setSlotTime(spotSlot.getSlotStartTime());
                }
                allUserDataDto.setTicketId(institutionData.getTicketId());
                allUserDataDto.setOrderId(institutionData.getOrderId());
                allUserDataDto.setPaymentId(institutionData.getPaymentId());
                allUserDataDto.setCreatedBy(institutionData.getCreatedBy());
                allUserDataDto.setGeneratedTime(institutionData.getCreatedTime());
                allUserDataDto.setVisitStatus(institutionData.isVisitStatus());
                allUserDataDto.setCategoryName("Institution");
                allUserDataDtoList.add(allUserDataDto);
            }
            //allUserDataDto.setInstitutionDtoDataList(institutionDtoDataList);
            //allUserDataDtoList.add(allUserDataDto);
        }
        List<ForeignerData> foreignerDataList = foreignerDataRepo.findAll();
//        List<ForeignerDtoData> foreignerDtoDataList = new ArrayList<>();
        if (!foreignerDataList.isEmpty()){
            for (ForeignerData foreignerData : foreignerDataList){
                AllUserDataDto allUserDataDto = new AllUserDataDto();
                allUserDataDto.setName(foreignerData.getName());
                allUserDataDto.setPhNumber(foreignerData.getPhNumber());
                allUserDataDto.setAdultCount(foreignerData.getAdult());
                allUserDataDto.setAdultCharge(foreignerData.getAdultGrandTotal());
                allUserDataDto.setChildCount(foreignerData.getChild());
                allUserDataDto.setChildCharge(foreignerData.getChildGrandTotal());
                allUserDataDto.setVisitDate(foreignerData.getVisitDate());
                Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(foreignerData.getSlotId());
                if (spotSlotOptional.isPresent()){
                    SpotSlot spotSlot = spotSlotOptional.get();
                    allUserDataDto.setVisitDate(foreignerData.getVisitDate());
                    allUserDataDto.setSlotTime(spotSlot.getSlotStartTime());
                }
                allUserDataDto.setGrandTotal(foreignerData.getGrandTotal());
                Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(foreignerData.getPaymentMode());
                if (paymentModeOptional.isPresent()){
                    PaymentMode paymentMode = paymentModeOptional.get();
                    allUserDataDto.setPaymentModeName(paymentMode.getPaymentType());
                }
                Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(foreignerData.getPaymentStatusId());
                if (paymentStatusOptional.isPresent()){
                    PaymentStatus paymentStatus = paymentStatusOptional.get();
                    allUserDataDto.setPaymentStatusName(paymentStatus.getStatusName());
                }
                allUserDataDto.setTicketId(foreignerData.getTicketId());
                allUserDataDto.setOrderId(foreignerData.getOrderId());
                allUserDataDto.setPaymentId(foreignerData.getPaymentId());
                allUserDataDto.setGeneratedTime(foreignerData.getCreatedTime());
                allUserDataDto.setCreatedBy(foreignerData.getCreatedBy());
                allUserDataDto.setVisitStatus(foreignerData.isVisitStatus());
                allUserDataDto.setCategoryName("Foreigner");
                allUserDataDtoList.add(allUserDataDto);
            }
//            allUserDataDto.setForeignerDtoDataList(foreignerDtoDataList);
//            allUserDataDtoList.add(allUserDataDto);
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
                publicDtoData.setAdultCharge(publicData.getAdultGrandTotal());
                publicDtoData.setChildCharge(publicData.getChildGrandTotal());
                publicDtoData.setChildCount(publicData.getChild());
                publicDtoData.setSeniorCitizenCount(publicData.getSeniorCitizen());
                publicDtoData.setVisitDate(publicData.getVisitDate());
                Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(publicData.getSlotId());
                if (spotSlotOptional.isPresent()){
                    SpotSlot spotSlot = spotSlotOptional.get();
                    publicDtoData.setSlotTime(spotSlot.getSlotStartTime());
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
                publicDtoData.setVisitStatus(publicData.isVisitStatus());
                publicDtoData.setCategoryName("Public");
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
                institutionDtoData.setTeacherCharge(institutionData.getTeacherTicketCharge());
                institutionDtoData.setStudentCount(institutionData.getStudent());
                institutionDtoData.setStudentCharge(institutionData.getStudentTicketCharge());
                institutionDtoData.setStudentDiscount(institutionData.getStudentDiscount());
                institutionDtoData.setPayableStudentCharge(institutionData.getPayableStudentCharge());
                institutionData.setDiscountAmount(institutionData.getDiscountAmount());
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
                    institutionDtoData.setSlotTime(spotSlot.getSlotStartTime());
                }
                institutionDtoData.setTicketId(institutionData.getTicketId());
                institutionDtoData.setOrderId(institutionData.getOrderId());
                institutionDtoData.setPaymentId(institutionData.getPaymentId());
                institutionDtoData.setCreatedBy(institutionData.getCreatedBy());
                institutionDtoData.setGeneratedTime(institutionData.getCreatedTime());
                institutionDtoData.setVisitorsStatus(institutionData.isVisitStatus());
                institutionDtoData.setCategoryName("Institution");
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
                foreignerDtoData.setAdultCharge(foreignerData.getAdultGrandTotal());
                foreignerDtoData.setChildCharge(foreignerData.getChildGrandTotal());
                foreignerDtoData.setChildCount(foreignerData.getChild());

                foreignerDtoData.setVisitDate(foreignerData.getVisitDate());
                Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(foreignerData.getSlotId());
                if (spotSlotOptional.isPresent()){
                    SpotSlot spotSlot = spotSlotOptional.get();
                    foreignerDtoData.setSlotTime(spotSlot.getSlotStartTime());
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
                foreignerDtoData.setPaymentId(foreignerData.getPaymentId());
                foreignerDtoData.setGeneratedTime(foreignerData.getCreatedTime());
                foreignerDtoData.setCreatedBy(foreignerData.getCreatedBy());
                foreignerDtoData.setVisitorsStatus(foreignerData.isVisitStatus());
                foreignerDtoData.setCategoryName("Foreigner");
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
                        getUserData.setAdultCharge(publicData.getAdultGrandTotal());
                        getUserData.setChild(publicData.getChild());
                        getUserData.setChildCharge(publicData.getChildGrandTotal());
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
                            getUserData.setSlotTime(spotSlot.getSlotStartTime());
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
                        getUserData.setTeacherCharge(institutionData.getTeacherTicketCharge());
                        getUserData.setStudent(institutionData.getStudent());
                        getUserData.setStudentCharge(institutionData.getStudentTicketCharge());
                        getUserData.setStudentDiscount(institutionData.getStudentDiscount());
                        getUserData.setPayableStudentCharge(institutionData.getPayableStudentCharge());
                        getUserData.setDiscountAmount(institutionData.getDiscountAmount());
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
                            getUserData.setSlotTime(spotSlot.getSlotStartTime());
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
                        getUserData.setAdultCharge(foreignerData.getAdultGrandTotal());
                        getUserData.setChild(foreignerData.getChild());
                        getUserData.setChildCharge(foreignerData.getChildGrandTotal());
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
                            getUserData.setSlotTime(spotSlot.getSlotStartTime());
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

        Optional<CategoryData> categoryDataOptional = categoryRepo.findById(categoryId);
        if (categoryDataOptional.isPresent()){
            CategoryData categoryData = categoryDataOptional.get();
            String name = categoryData.getCategory();
            if ("Public".equalsIgnoreCase(name)){
                List<PublicData> publicDataList = publicRepo.findByVisitDateBetween(startDate,endDate);
//                List<PublicDtoData> publicDtoDataList = new ArrayList<>();
                if (!publicDataList.isEmpty()){
                    for (PublicData publicData : publicDataList){
                        AllUserDataDto allUserDataDto = new AllUserDataDto();
                        allUserDataDto.setName(publicData.getName());
                        allUserDataDto.setPhNumber(publicData.getPhNumber());
                        allUserDataDto.setAdultCount(publicData.getAdult());
                        allUserDataDto.setAdultCharge(publicData.getAdultGrandTotal());
                        allUserDataDto.setChildCount(publicData.getChild());
                        allUserDataDto.setChildCharge(publicData.getChildGrandTotal());
                        allUserDataDto.setSeniorCitizenCount(publicData.getSeniorCitizen());
                        allUserDataDto.setOrderId(publicData.getOrderId());

                        Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(publicData.getPaymentMode());
                        if (paymentModeOptional.isPresent()){
                            PaymentMode paymentMode = paymentModeOptional.get();
                            allUserDataDto.setPaymentModeName(paymentMode.getPaymentType());
                        }

                        Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(publicData.getPaymentStatusId());
                        if (paymentStatusOptional.isPresent()){
                            PaymentStatus paymentStatus = paymentStatusOptional.get();
                            allUserDataDto.setPaymentStatusName(paymentStatus.getStatusName());
                        }

                        allUserDataDto.setVisitDate(publicData.getVisitDate());

                        Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(publicData.getSlotId());
                        if (spotSlotOptional.isPresent()){
                            SpotSlot spotSlot = spotSlotOptional.get();
                            allUserDataDto.setSlotTime(spotSlot.getSlotStartTime());
                        }

                        allUserDataDto.setGrandTotal(publicData.getGrandTotal());
                        allUserDataDto.setTicketId(publicData.getTicketId());
                        allUserDataDto.setPaymentId(publicData.getPaymentId());
                        allUserDataDto.setGeneratedTime(publicData.getCreatedTime());
                        allUserDataDto.setCreatedBy(publicData.getCreatedBy());

                        allUserDataDtoList.add(allUserDataDto);
                    }
//                    allUserDataDto.setPublicDtoDataList(publicDtoDataList);
//                    allUserDataDtoList.add(allUserDataDto);
                    return new ResponseEntity<>(allUserDataDtoList,HttpStatus.OK);
                }

            } else if ("Institution".equalsIgnoreCase(name)) {
//                List<InstitutionDtoData> institutionDtoDataList = new ArrayList<>();
                List<InstitutionData> institutionDataList = institutionDataRepo.findByVisitDateBetween(startDate,endDate);
                if (!institutionDataList.isEmpty()){
                    for (InstitutionData institutionData : institutionDataList){
                        AllUserDataDto allUserDataDto = new AllUserDataDto();
                        allUserDataDto.setName(institutionData.getName());
                        allUserDataDto.setPhNumber(institutionData.getPhNumber());
                        allUserDataDto.setDistrict(institutionData.getDistrict());
                        allUserDataDto.setTeacherCount(institutionData.getTeacher());
                        allUserDataDto.setTeacherCharge(institutionData.getTeacherTicketCharge());
                        allUserDataDto.setStudentCount(institutionData.getStudent());
                        allUserDataDto.setStudentCharge(institutionData.getStudentTicketCharge());
                        allUserDataDto.setStudentDiscount(institutionData.getStudentDiscount());
                        allUserDataDto.setPayableStudentCharge(institutionData.getPayableStudentCharge());
                        allUserDataDto.setDiscountAmount(institutionData.getDiscountAmount());
                        allUserDataDto.setOrderId(institutionData.getOrderId());
                        allUserDataDto.setGrandTotal(institutionData.getGrandTotal());
                        Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(institutionData.getPaymentMode());
                        if (paymentModeOptional.isPresent()){
                            PaymentMode paymentMode = paymentModeOptional.get();
                            allUserDataDto.setPaymentModeName(paymentMode.getPaymentType());
                        }
                        Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(institutionData.getPaymentStatusId());
                        if (paymentStatusOptional.isPresent()){
                            PaymentStatus paymentStatus = paymentStatusOptional.get();
                            allUserDataDto.setPaymentStatusName(paymentStatus.getStatusName());
                        }
                        allUserDataDto.setVisitDate(institutionData.getVisitDate());
                        Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(institutionData.getSlotId());
                        if (spotSlotOptional.isPresent()){
                            SpotSlot spotSlot = spotSlotOptional.get();
                            allUserDataDto.setSlotTime(spotSlot.getSlotStartTime());
                        }
                        allUserDataDto.setTicketId(institutionData.getTicketId());
                        allUserDataDto.setOrderId(institutionData.getOrderId());
                        allUserDataDto.setPaymentId(institutionData.getPaymentId());
                        allUserDataDto.setCreatedBy(institutionData.getCreatedBy());
                        allUserDataDto.setGeneratedTime(institutionData.getCreatedTime());
//                        institutionDtoDataList.add(institutionDtoData);
                        allUserDataDtoList.add(allUserDataDto);
                    }
//                    allUserDataDto.setInstitutionDtoDataList(institutionDtoDataList);
//                    allUserDataDtoList.add(allUserDataDto);
                    return new ResponseEntity<>(allUserDataDtoList,HttpStatus.OK);
                }

            } else if ("Foreigner".equalsIgnoreCase(name)) {
//                List<ForeignerDtoData> foreignerDtoDataList = new ArrayList<>();
                List<ForeignerData> foreignerDataList = foreignerDataRepo.findByVisitDateBetween(startDate,endDate);
                if (!foreignerDataList.isEmpty()){
                    for (ForeignerData foreignerData : foreignerDataList){
                        AllUserDataDto allUserDataDto = new AllUserDataDto();
                        allUserDataDto.setName(foreignerData.getName());
                        allUserDataDto.setPhNumber(foreignerData.getPhNumber());
                        allUserDataDto.setAdultCount(foreignerData.getAdult());
                        allUserDataDto.setAdultCharge(foreignerData.getAdultGrandTotal());
                        allUserDataDto.setChildCount(foreignerData.getChild());
                        allUserDataDto.setChildCharge(foreignerData.getChildGrandTotal());
                        allUserDataDto.setVisitDate(foreignerData.getVisitDate());
                        Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(foreignerData.getSlotId());
                        if (spotSlotOptional.isPresent()){
                            SpotSlot spotSlot = spotSlotOptional.get();
                            allUserDataDto.setSlotTime(spotSlot.getSlotStartTime());
                        }
                        allUserDataDto.setGrandTotal(foreignerData.getGrandTotal());
                        Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(foreignerData.getPaymentMode());
                        if (paymentModeOptional.isPresent()){
                            PaymentMode paymentMode = paymentModeOptional.get();
                            allUserDataDto.setPaymentModeName(paymentMode.getPaymentType());
                        }
                        Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(foreignerData.getPaymentStatusId());
                        if (paymentStatusOptional.isPresent()){
                            PaymentStatus paymentStatus = paymentStatusOptional.get();
                            allUserDataDto.setPaymentStatusName(paymentStatus.getStatusName());
                        }
                        allUserDataDto.setTicketId(foreignerData.getTicketId());
                        allUserDataDto.setOrderId(foreignerData.getOrderId());
                        allUserDataDto.setPaymentId(foreignerData.getPaymentId());
                        allUserDataDto.setGeneratedTime(foreignerData.getCreatedTime());
                        allUserDataDto.setCreatedBy(foreignerData.getCreatedBy());
                        allUserDataDtoList.add(allUserDataDto);
                    }
//                    allUserDataDto.setForeignerDtoDataList(foreignerDtoDataList);
//                    allUserDataDtoList.add(allUserDataDto);
                    return new ResponseEntity<>(allUserDataDtoList,HttpStatus.OK);
                }
            }else {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NO_CONTENT);
            }
        }else {

            List<PublicData> publicDataList = publicRepo.findAll();
            if (!publicDataList.isEmpty()){
//                List<PublicDtoData> publicDtoDataList = new ArrayList<>();
                for (PublicData publicData : publicDataList){
                    AllUserDataDto allUserDataDto = new AllUserDataDto();
                    allUserDataDto.setName(publicData.getName());
                    allUserDataDto.setPhNumber(publicData.getPhNumber());
                    allUserDataDto.setAdultCount(publicData.getAdult());
                    allUserDataDto.setAdultCharge(publicData.getAdultGrandTotal());
                    allUserDataDto.setChildCount(publicData.getChild());
                    allUserDataDto.setChildCharge(publicData.getChildGrandTotal());
                    allUserDataDto.setSeniorCitizenCount(publicData.getSeniorCitizen());
                    allUserDataDto.setOrderId(publicData.getOrderId());
                    allUserDataDto.setPaymentId(publicData.getPaymentId());
                    allUserDataDto.setTicketId(publicData.getTicketId());

                    Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(publicData.getSlotId());
                    if (spotSlotOptional.isPresent()){
                        SpotSlot spotSlot = spotSlotOptional.get();
                        allUserDataDto.setSlotTime(spotSlot.getSlotStartTime());
                    }
                    allUserDataDto.setGrandTotal(publicData.getGrandTotal());
                    Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(publicData.getPaymentMode());
                    if (paymentModeOptional.isPresent()){
                        PaymentMode paymentMode = paymentModeOptional.get();
                        allUserDataDto.setPaymentModeName(paymentMode.getPaymentType());
                    }
                    Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(publicData.getPaymentStatusId());
                    if (paymentStatusOptional.isPresent()){
                        PaymentStatus paymentStatus = paymentStatusOptional.get();
                        allUserDataDto.setPaymentStatusName(paymentStatus.getStatusName());
                    }
                    allUserDataDto.setTicketId(publicData.getTicketId());
                    allUserDataDto.setOrderId(publicData.getOrderId());
                    allUserDataDto.setPaymentId(publicData.getPaymentId());
                    allUserDataDto.setGeneratedTime(publicData.getCreatedTime());
                    allUserDataDto.setCreatedBy(publicData.getCreatedBy());
                    allUserDataDtoList.add(allUserDataDto);
                }
//                allUserDataDto.setPublicDtoDataList(publicDtoDataList);

            }
            List<InstitutionData> institutionDataList = institutionDataRepo.findAll();
            if (!institutionDataList.isEmpty()){
//                List<InstitutionDtoData> institutionDtoDataList = new ArrayList<>();
                for (InstitutionData institutionData : institutionDataList){
                    AllUserDataDto allUserDataDto = new AllUserDataDto();
                    allUserDataDto.setName(institutionData.getName());
                    allUserDataDto.setPhNumber(institutionData.getPhNumber());
                    allUserDataDto.setDistrict(institutionData.getDistrict());
                    allUserDataDto.setTeacherCount(institutionData.getTeacher());
                    allUserDataDto.setTeacherCharge(institutionData.getTeacherTicketCharge());
                    allUserDataDto.setStudentCount(institutionData.getStudent());
                    allUserDataDto.setStudentCharge(institutionData.getStudentTicketCharge());
                    allUserDataDto.setStudentDiscount(institutionData.getStudentDiscount());
                    allUserDataDto.setPayableStudentCharge(institutionData.getPayableStudentCharge());
                    allUserDataDto.setDiscountAmount(institutionData.getDiscountAmount());
                    allUserDataDto.setGrandTotal(institutionData.getGrandTotal());
                    Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(institutionData.getPaymentMode());
                    if (paymentModeOptional.isPresent()){
                        PaymentMode paymentMode = paymentModeOptional.get();
                        allUserDataDto.setPaymentModeName(paymentMode.getPaymentType());
                    }
                    Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(institutionData.getPaymentStatusId());
                    if (paymentStatusOptional.isPresent()){
                        PaymentStatus paymentStatus = paymentStatusOptional.get();
                        allUserDataDto.setPaymentStatusName(paymentStatus.getStatusName());
                    }
                    allUserDataDto.setVisitDate(institutionData.getVisitDate());
                    Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(institutionData.getSlotId());
                    if (spotSlotOptional.isPresent()){
                        SpotSlot spotSlot = spotSlotOptional.get();
                        allUserDataDto.setSlotTime(spotSlot.getSlotStartTime());
                    }
                    allUserDataDto.setTicketId(institutionData.getTicketId());
                    allUserDataDto.setOrderId(institutionData.getOrderId());
                    allUserDataDto.setPaymentId(institutionData.getPaymentId());
                    allUserDataDto.setCreatedBy(institutionData.getCreatedBy());
                    allUserDataDto.setGeneratedTime(institutionData.getCreatedTime());
                    allUserDataDtoList.add(allUserDataDto);
                }
            }
            List<ForeignerData> foreignerDataList = foreignerDataRepo.findAll();
//            List<ForeignerDtoData> foreignerDtoDataList = new ArrayList<>();
            if (!foreignerDataList.isEmpty()){
                for (ForeignerData foreignerData : foreignerDataList){
                    AllUserDataDto allUserDataDto = new AllUserDataDto();
                    allUserDataDto.setName(foreignerData.getName());
                    allUserDataDto.setPhNumber(foreignerData.getPhNumber());
                    allUserDataDto.setAdultCount(foreignerData.getAdult());
                    allUserDataDto.setAdultCharge(foreignerData.getAdultGrandTotal());
                    allUserDataDto.setChildCount(foreignerData.getChild());
                    allUserDataDto.setChildCharge(foreignerData.getChildGrandTotal());
                    allUserDataDto.setVisitDate(foreignerData.getVisitDate());
                    Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(foreignerData.getSlotId());
                    if (spotSlotOptional.isPresent()){
                        SpotSlot spotSlot = spotSlotOptional.get();
                        allUserDataDto.setSlotTime(spotSlot.getSlotStartTime());
                    }
                    allUserDataDto.setGrandTotal(foreignerData.getGrandTotal());
                    Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(foreignerData.getPaymentMode());
                    if (paymentModeOptional.isPresent()){
                        PaymentMode paymentMode = paymentModeOptional.get();
                        allUserDataDto.setPaymentModeName(paymentMode.getPaymentType());
                    }
                    Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(foreignerData.getPaymentStatusId());
                    if (paymentStatusOptional.isPresent()){
                        PaymentStatus paymentStatus = paymentStatusOptional.get();
                        allUserDataDto.setPaymentStatusName(paymentStatus.getStatusName());
                    }
                    allUserDataDto.setTicketId(foreignerData.getTicketId());
                    allUserDataDto.setOrderId(foreignerData.getOrderId());
                    allUserDataDto.setPaymentId(allUserDataDto.getPaymentId());
                    allUserDataDto.setGeneratedTime(foreignerData.getCreatedTime());
                    allUserDataDto.setCreatedBy(foreignerData.getCreatedBy());
                    allUserDataDtoList.add(allUserDataDto);
                }
//                allUserDataDto.setForeignerDtoDataList(foreignerDtoDataList);
//                allUserDataDtoList.add(allUserDataDto);
                return new ResponseEntity<>(allUserDataDtoList,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<GetRevenueDetails>> CategoryBasedTotalRevenueByDate(LocalDate visitDate) {
        List<PublicData> publicDataList = publicRepo.findByVisitDate(visitDate);
        List<InstitutionData> institutionDataList = institutionDataRepo.findByVisitDate(visitDate);
        List<ForeignerData> foreignerDataList = foreignerDataRepo.findByVisitDate(visitDate);
        Double revenueAmount = 0.0,totalRevenue=0.0;
        List<GetRevenueDetails> getRevenueDetailsList = new ArrayList<>();
        GetRevenueDetails getRevenueDetails = new GetRevenueDetails();
        if (publicDataList.isEmpty() && institutionDataList.isEmpty() && foreignerDataList.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NO_CONTENT);
        }else {
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
            }
            getRevenueDetails.setOverAllIncome(totalRevenue);
            getRevenueDetailsList.add(getRevenueDetails);
            return new ResponseEntity<>(getRevenueDetailsList,HttpStatus.OK);
        }
    }

//    public ResponseEntity<?> CategoryBasedTotalRevenueByDate(LocalDate visitDate) {
//        List<PublicData> publicDataList = publicRepo.findByVisitDate(visitDate);
//        List<InstitutionData> institutionDataList = institutionDataRepo.findByVisitDate(visitDate);
//        List<ForeignerData> foreignerDataList = foreignerDataRepo.findByVisitDate(visitDate);
//        Double revenueAmount = 0.0,totalRevenue=0.0;
//        GetRevenueDetails getRevenueDetails = new GetRevenueDetails();
//        if (publicDataList.isEmpty() && institutionDataList.isEmpty() && foreignerDataList.isEmpty()){
//            return new ResponseEntity<>("No data",HttpStatus.NO_CONTENT);
//        }else {
//            if (!publicDataList.isEmpty()){
//                for (PublicData publicData : publicDataList){
//                    revenueAmount+=publicData.getGrandTotal();
//                }
//                totalRevenue+=revenueAmount;
//                getRevenueDetails.setPublicRevenue(revenueAmount);
//            }
//            if (!institutionDataList.isEmpty()){
//                for (InstitutionData institutionData : institutionDataList){
//                    revenueAmount+=institutionData.getGrandTotal();
//                }
//                totalRevenue+=revenueAmount;
//                getRevenueDetails.setInstitutionRevenue(revenueAmount);
//            }
//            if (!foreignerDataList.isEmpty()){
//                for (ForeignerData foreignerData :foreignerDataList){
//                    revenueAmount+=foreignerData.getGrandTotal();
//                }
//                totalRevenue+=revenueAmount;
//                getRevenueDetails.setForeignerRevenue(revenueAmount);
//            }
//            getRevenueDetails.setOverAllIncome(totalRevenue);
//            return new ResponseEntity<>(getRevenueDetails,HttpStatus.OK);
//        }
//
//    }

    public ResponseEntity<?> totalPublicVisitorsCountByDate(LocalDate vDate) {
        List<PublicData> publicDataList = publicRepo.findByVisitDate(vDate);
        Integer adultCount =0,childCount=0,seniorCitizenCount=0,count=0;
        List<PublicVisitorsDto> publicVisitorsDtoList = new ArrayList<>();
        if (publicDataList.isEmpty()){
            return new ResponseEntity<>("No Data",HttpStatus.NO_CONTENT);
        }else {
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
                publicVisitorsDto.setPublicTicketCount(count);
                publicVisitorsDtoList.add(publicVisitorsDto);
            }
            return new ResponseEntity<>(publicVisitorsDtoList,HttpStatus.OK);
        }
    }

    public ResponseEntity<?> totalInstitutionVisitorsCountByDate(LocalDate vDate) {
        List<InstitutionData> institutionDataList = institutionDataRepo.findByVisitDate(vDate);
        Integer teacherCount =0,studentCount=0,count=0;
        List<InstitutionVisitorsDto> institutionVisitorsDtoList = new ArrayList<>();
        if (institutionDataList.isEmpty()){
            return new ResponseEntity<>("No Data",HttpStatus.NO_CONTENT);
        }else {
            if (!institutionDataList.isEmpty()){
                InstitutionVisitorsDto institutionVisitorsDto = new InstitutionVisitorsDto();
                for (InstitutionData institutionData : institutionDataList){
                    teacherCount+=institutionData.getTeacher();
                    studentCount+=institutionData.getStudent();
                    count++;
                }
                institutionVisitorsDto.setInstitutionTicketCount(count);
                institutionVisitorsDto.setTeacherCount(teacherCount);
                institutionVisitorsDto.setStudnetCount(studentCount);
                institutionVisitorsDtoList.add(institutionVisitorsDto);
            }
            return new ResponseEntity<>(institutionVisitorsDtoList,HttpStatus.OK);
        }

    }

    public ResponseEntity<?> totalForeignerVisitorsCountByDate(LocalDate vDate) {
        List<ForeignerData> foreignerDataList = foreignerDataRepo.findByVisitDate(vDate);
        Integer adultCount =0,childCount=0,count=0;
        List<ForeignerVisitorsDto> foreignerVisitorsDtoList = new ArrayList<>();
        if (foreignerDataList.isEmpty()){
            return new ResponseEntity<>("No data",HttpStatus.NO_CONTENT);
        }else {
            if (!foreignerDataList.isEmpty()){
                ForeignerVisitorsDto foreignerVisitorsDto = new ForeignerVisitorsDto();
                for (ForeignerData foreignerData : foreignerDataList){
                    adultCount+=foreignerData.getAdult();
                    childCount+=foreignerData.getChild();
                    count++;
                }
                foreignerVisitorsDto.setForeignerTicketCount(count);
                foreignerVisitorsDto.setForeignAdult(adultCount);
                foreignerVisitorsDto.setForeignChild(childCount);
                foreignerVisitorsDtoList.add(foreignerVisitorsDto);
                return new ResponseEntity<>(foreignerVisitorsDto,HttpStatus.OK);
            }
            return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> totalVisitorSCountByDate(LocalDate vDate) {
        List<PublicData> publicDataList = publicRepo.findByVisitDate(vDate);
        List<InstitutionData> institutionDataList = institutionDataRepo.findByVisitDate(vDate);
        List<ForeignerData> foreignerDataList = foreignerDataRepo.findByVisitDate(vDate);

        List<VisitsCountDto> visitsCountDtoList = new ArrayList<>();
        VisitsCountDto visitsCountDto = new VisitsCountDto();

        Integer adultCount =0,childCount=0,seniorCitizenCount=0,count=0,totalCount=0;
        if (publicDataList.isEmpty()&&institutionDataList.isEmpty()&& foreignerDataList.isEmpty()){
            return new ResponseEntity<>("No data",HttpStatus.NO_CONTENT);
        }else {
            if (!publicDataList.isEmpty()){
//            List<PublicVisitorsDto> publicVisitorsDtoList = new ArrayList<>();
//            PublicVisitorsDto publicVisitorsDto = new PublicVisitorsDto();

                for (PublicData publicData : publicDataList){
                    adultCount+=publicData.getAdult();
                    childCount+=publicData.getChild();
                    seniorCitizenCount+=publicData.getSeniorCitizen();
                    count++;
                }
                totalCount+=count;
                visitsCountDto.setAdultCount(adultCount);
                visitsCountDto.setChildCount(childCount);
                visitsCountDto.setSeniorCitizen(seniorCitizenCount);
                visitsCountDto.setPublicTicketCount(count);
                //visitsCountDtoList.add(visitsCountDto);
//            visitsCountDto.setPublicVisitorsDtoList(publicVisitorsDtoList);
                count=0;adultCount=0;childCount=0;
            }
            if (!institutionDataList.isEmpty()){
                List<InstitutionVisitorsDto> institutionVisitorsDtoList = new ArrayList<>();
//            InstitutionVisitorsDto institutionVisitorsDto = new InstitutionVisitorsDto();
//            VisitsCountDto visitsCountDto = new VisitsCountDto();
                for (InstitutionData institutionData:institutionDataList){
                    adultCount+=institutionData.getTeacher();
                    childCount+=institutionData.getStudent();
                    count++;
                }
                totalCount+=count;
                visitsCountDto.setTeacherCount(adultCount);
                visitsCountDto.setStudentCount(childCount);
                visitsCountDto.setInstitutionTicketCount(count);
                //visitsCountDtoList.add(visitsCountDto);
//            visitsCountDto.setInstitutionVisitorsDtoList(institutionVisitorsDtoList);
                count=0;adultCount=0;childCount=0;
            }
            if (!foreignerDataList.isEmpty()){
                List<ForeignerVisitorsDto> foreignerVisitorsDtoList = new ArrayList<>();
//            ForeignerVisitorsDto foreignerVisitorsDto = new ForeignerVisitorsDto();
//            VisitsCountDto visitsCountDto = new VisitsCountDto();
                for (ForeignerData foreignerData:foreignerDataList){
                    adultCount+=foreignerData.getAdult();
                    childCount+=foreignerData.getChild();
                    count++;
                }
                totalCount+=count;
                visitsCountDto.setForeignAdult(adultCount);
                visitsCountDto.setForeignChild(childCount);
                visitsCountDto.setForeignerTicketCount(count);

//                visitsCountDtoList.add(visitsCountDto);
//                return new ResponseEntity<>(visitsCountDtoList,HttpStatus.OK);
//            visitsCountDto.setForeignerVisitorsDtoList(foreignerVisitorsDtoList);
//            visitsCountDtoList.add(visitsCountDto);
            }
            visitsCountDto.setTotalVisitsCount(totalCount);
            visitsCountDtoList.add(visitsCountDto);
            return new ResponseEntity<>(visitsCountDtoList,HttpStatus.OK);
        }
//        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<VisitsCountDto>> visitorsCountByDateRange(LocalDate startDate, LocalDate endDate) {
        List<PublicData> publicDataList = publicRepo.findByVisitDateBetween(startDate,endDate);
        List<InstitutionData> institutionDataList = institutionDataRepo.findByVisitDateBetween(startDate,endDate);
        List<ForeignerData> foreignerDataList = foreignerDataRepo.findByVisitDateBetween(startDate,endDate);

        List<VisitsCountDto> visitsCountDtoList = new ArrayList<>();
        VisitsCountDto visitsCountDto = new VisitsCountDto();
        Integer adultCount =0,childCount=0,seniorCitizenCount=0,count=0,totalCount=0,flag=0,discountNumber=0;
        //VisitsCountDto visitsCountDto = new VisitsCountDto();
        Double adultGrandTotals=0.0,childGrandTotals=0.0,seniorCitizenGrandTotal=0.0,overAllGrandTotal=0.0,discountPercentage=0.0;
        Double overAllIncome=0.0;

        if (publicDataList.isEmpty() && institutionDataList.isEmpty() && foreignerDataList.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NO_CONTENT);
        }else {
            if (!publicDataList.isEmpty()){
//            List<PublicVisitorsDto> publicVisitorsDtoList = new ArrayList<>();

//            PublicVisitorsDto publicVisitorsDto = new PublicVisitorsDto();
                for (PublicData publicData : publicDataList){
                    adultCount+=publicData.getAdult();
                    childCount+=publicData.getChild();
                    seniorCitizenCount+=publicData.getSeniorCitizen();
                    count++;
                    adultGrandTotals += publicData.getAdultGrandTotal();
                    childGrandTotals += publicData.getChildGrandTotal();
                    seniorCitizenGrandTotal +=publicData.getSeniorCitizenGrandTotal();

                }
                overAllGrandTotal+=adultGrandTotals+childGrandTotals+seniorCitizenGrandTotal;
                totalCount+=count;
                visitsCountDto.setPublicTicketCount(count);
                visitsCountDto.setAdultCount(adultCount);
                visitsCountDto.setChildCount(childCount);
                visitsCountDto.setSeniorCitizen(seniorCitizenCount);
                visitsCountDto.setPublicGrandTotal(overAllGrandTotal);
                visitsCountDto.setAdultGrandTotal(adultGrandTotals);
                visitsCountDto.setChildGrandTotal(childGrandTotals);
                visitsCountDto.setSeniorCitizenGrandTotal(seniorCitizenGrandTotal);
                overAllIncome+=overAllGrandTotal;
                //visitsCountDtoList.add(visitsCountDto);
//            visitsCountDto.setPublicVisitorsDtoList(publicVisitorsDtoList);
                adultCount=0;childCount=0;count=0;adultGrandTotals=0.0;childGrandTotals=0.0;overAllGrandTotal=0.0;
            }
            if (!institutionDataList.isEmpty()){
                List<InstitutionVisitorsDto> institutionVisitorsDtoList = new ArrayList<>();
//            InstitutionVisitorsDto institutionVisitorsDto = new InstitutionVisitorsDto();
                for (InstitutionData institutionData : institutionDataList){
                    adultCount+=institutionData.getTeacher();
                    childCount+=institutionData.getStudent();
                    count++;


                    Double disAmount = institutionData.getDiscountAmount()*100;
                    if (flag<disAmount){
                        discountNumber++;
                        discountPercentage+=disAmount;
                    }
                    adultGrandTotals+=institutionData.getTeacherTicketCharge();
                    childGrandTotals+=institutionData.getPayableStudentCharge();


                }
                totalCount+=count;
                overAllGrandTotal=adultGrandTotals+childGrandTotals;
                visitsCountDto.setTeacherCount(adultCount);
                visitsCountDto.setStudentCount(childCount);
                visitsCountDto.setInstitutionTicketCount(count);
                visitsCountDto.setInstitutionGrandTotal(overAllGrandTotal);
                visitsCountDto.setTeacherGrandTotal(adultGrandTotals);
                visitsCountDto.setStudentGrandTotal(childGrandTotals);
                visitsCountDto.setNo_Of_Discount(discountNumber);
                visitsCountDto.setOverAllDiscountPercentage(discountPercentage);
                overAllIncome+=overAllGrandTotal;
                //visitsCountDtoList.add(visitsCountDto);
                adultCount=0;childCount=0;count=0;adultGrandTotals=0.0;childGrandTotals=0.0;overAllGrandTotal=0.0;
            }
            if (!foreignerDataList.isEmpty()){

//            ForeignerVisitorsDto foreignerVisitorsDto = new ForeignerVisitorsDto();
                for (ForeignerData foreignerData : foreignerDataList){
                    adultCount+= foreignerData.getAdult();
                    childCount+= foreignerData.getChild();
                    count++;
                    adultGrandTotals+=foreignerData.getAdultGrandTotal();
                    childGrandTotals+=foreignerData.getChildGrandTotal();

                }
                totalCount+=count;
                overAllGrandTotal+=adultGrandTotals+childGrandTotals;
                visitsCountDto.setForeignerTicketCount(count);
                visitsCountDto.setForeignAdult(adultCount);
                visitsCountDto.setForeignChild(childCount);
                visitsCountDto.setTotalVisitsCount(totalCount);
                visitsCountDto.setForeignerAdultGrandTotal(adultGrandTotals);
                visitsCountDto.setForeignerChildGrandTotal(childGrandTotals);
                visitsCountDto.setForeignerGrandTotal(overAllGrandTotal);
                overAllIncome+=overAllGrandTotal;

            }
            visitsCountDto.setOverAllIncome(overAllIncome);
            visitsCountDtoList.add(visitsCountDto);
            return new ResponseEntity<>(visitsCountDtoList,HttpStatus.OK);
        }

    }

    public ResponseEntity<List<VisitorsAmountDto>> visitsIncomeAndTotalCountUpToNow(Integer categoryId) {
        Optional<CategoryData> categoryDataOptional = categoryRepo.findById(categoryId);
        List<VisitorsAmountDto> visitorsAmountDtoList = new ArrayList<>();
        VisitorsAmountDto visitorsAmountDto = new VisitorsAmountDto();
        Double incomeData=0.0;Integer ticketCount=0;
        if (categoryDataOptional.isPresent()){
            CategoryData categoryData = categoryDataOptional.get();
            if ("Public".equalsIgnoreCase(categoryData.getCategory())){
                List<PublicData> publicDataList = publicRepo.findAll();
                if (!publicDataList.isEmpty()){
                    for (PublicData publicData:publicDataList){
                        Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(publicData.getPaymentStatusId());
                        if (paymentStatusOptional.isPresent()){
                            PaymentStatus paymentStatus = paymentStatusOptional.get();
                            if ("Received".equalsIgnoreCase(paymentStatus.getStatusName())){
                                incomeData+=publicData.getGrandTotal();
                                ticketCount+=(publicData.getAdult()+publicData.getChild()+publicData.getSeniorCitizen());
                            }
                        }
                    }
                    visitorsAmountDto.setPublicIncome(incomeData);
                    visitorsAmountDto.setPublicTicketCount(ticketCount);
                    visitorsAmountDtoList.add(visitorsAmountDto);
                    return new ResponseEntity<>(visitorsAmountDtoList,HttpStatus.OK);
                }
            } else if ("Institution".equalsIgnoreCase(categoryData.getCategory())) {
                List<InstitutionData> institutionDataList = institutionDataRepo.findAll();
                if (!institutionDataList.isEmpty()){
                    for (InstitutionData institutionData : institutionDataList){
                        Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(institutionData.getPaymentStatusId());
                        if (paymentStatusOptional.isPresent()){
                            PaymentStatus paymentStatus = paymentStatusOptional.get();
                            if ("Received".equalsIgnoreCase(paymentStatus.getStatusName())){
                                incomeData+=institutionData.getGrandTotal();
                                ticketCount+=(institutionData.getTeacher()+institutionData.getStudent());
                            }
                        }
                    }
                    visitorsAmountDto.setInstitutionIncome(incomeData);
                    visitorsAmountDto.setInstitutionTicketCount(ticketCount);
                    visitorsAmountDtoList.add(visitorsAmountDto);
                    return new ResponseEntity<>(visitorsAmountDtoList,HttpStatus.OK);
                }
            } else if ("Foreigner".equalsIgnoreCase(categoryData.getCategory())) {
                List<ForeignerData> foreignerDataList = foreignerDataRepo.findAll();
                if (!foreignerDataList.isEmpty()){
                    for (ForeignerData foreignerData:foreignerDataList){
                        Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(foreignerData.getPaymentStatusId());
                        if (paymentStatusOptional.isPresent()){
                            PaymentStatus paymentStatus = paymentStatusOptional.get();
                            if ("Received".equalsIgnoreCase(paymentStatus.getStatusName())){
                                incomeData+=foreignerData.getGrandTotal();
                                ticketCount+=(foreignerData.getAdult()+foreignerData.getChild());
                            }
                        }
                    }
                    visitorsAmountDto.setForeignerIncome(incomeData);
                    visitorsAmountDto.setForeignerTicketCount(ticketCount);
                    visitorsAmountDtoList.add(visitorsAmountDto);
                    return new ResponseEntity<>(visitorsAmountDtoList,HttpStatus.OK);
                }
            }
        }else {
            Double incomeAll=0.0;Integer countAll=0;
            List<PublicData> publicDataList = publicRepo.findAll();
            if (!publicDataList.isEmpty()){
                for (PublicData publicData:publicDataList){
                    Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(publicData.getPaymentStatusId());
                    if (paymentStatusOptional.isPresent()){
                        PaymentStatus paymentStatus = paymentStatusOptional.get();
                        if ("Received".equalsIgnoreCase(paymentStatus.getStatusName())){
                            incomeData+=publicData.getGrandTotal();
                            ticketCount+=(publicData.getAdult()+publicData.getChild()+publicData.getSeniorCitizen());
                        }
                    }
                }
                visitorsAmountDto.setPublicIncome(incomeData);
                visitorsAmountDto.setPublicTicketCount(ticketCount);
                incomeAll+=incomeData;countAll+=ticketCount;
            }
            incomeData=0.0;ticketCount=0;
            List<InstitutionData> institutionDataList = institutionDataRepo.findAll();
            if (!institutionDataList.isEmpty()){
                for (InstitutionData institutionData : institutionDataList){
                    Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(institutionData.getPaymentStatusId());
                    if (paymentStatusOptional.isPresent()){
                        PaymentStatus paymentStatus = paymentStatusOptional.get();
                        if ("Received".equalsIgnoreCase(paymentStatus.getStatusName())){
                            incomeData+=institutionData.getGrandTotal();
                            ticketCount+=(institutionData.getTeacher()+institutionData.getStudent());
                        }
                    }
                }
                visitorsAmountDto.setInstitutionIncome(incomeData);
                visitorsAmountDto.setInstitutionTicketCount(ticketCount);
                incomeAll+=incomeData;countAll+=ticketCount;
            }
            incomeData=0.0;ticketCount=0;
            List<ForeignerData> foreignerDataList = foreignerDataRepo.findAll();
            if (!foreignerDataList.isEmpty()){
                for (ForeignerData foreignerData:foreignerDataList){
                    Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(foreignerData.getPaymentStatusId());
                    if (paymentStatusOptional.isPresent()){
                        PaymentStatus paymentStatus = paymentStatusOptional.get();
                        if ("Received".equalsIgnoreCase(paymentStatus.getStatusName())){
                            incomeData+=foreignerData.getGrandTotal();
                            ticketCount+=(foreignerData.getAdult()+foreignerData.getChild());
                        }
                    }
                }
                visitorsAmountDto.setForeignerIncome(incomeData);
                visitorsAmountDto.setForeignerTicketCount(ticketCount);
                incomeAll+=incomeData;countAll+=ticketCount;
                visitorsAmountDto.setTotalIncome(incomeAll);
                visitorsAmountDto.setTotalTicketCount(countAll);
                visitorsAmountDtoList.add(visitorsAmountDto);
                return new ResponseEntity<>(visitorsAmountDtoList,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    public Map<String, Map<String, Object>> getMonthlyDataByYear(int year,Integer categoryId) {
        Optional<CategoryData> categoryDataOptional = categoryRepo.findById(categoryId);
        if (categoryDataOptional.isPresent()){
            CategoryData categoryData = categoryDataOptional.get();
            if ("Public".equalsIgnoreCase(categoryData.getCategory())){
                List<Object[]> results = publicRepo.findMonthlyDataByYear(year);

                Map<String, Map<String, Object>> monthlyData = new LinkedHashMap<>();
                for (int month=1;month<=12;month++){
                    monthlyData.put(getMonthName(month),new HashMap<String,Object>() {{
                        put("grandTotal",0.0);
                        put("countOfPeople",0);
                    }});
                }
                for (Object[] result : results){
                    Integer monthInteger = (Integer) result[0];
                    int month = monthInteger;
                    double grandTotal = ((Number) result[1]).doubleValue();
                    int countOfPeople = ((Number) result[2]).intValue();

                    Map<String, Object> monthData =monthlyData.get(getMonthName(month));
                    if (monthData!=null){
                        monthData.put("grandTotal",grandTotal);
                        monthData.put("countOfPeople",countOfPeople);
                    }
                }
                return monthlyData;
            } else if ("Institution".equalsIgnoreCase(categoryData.getCategory())) {
                List<Object[]> results = institutionDataRepo.findMonthlyDataByYear(year);
                Map<String,Map<String,Object>> monthlyData = new LinkedHashMap<>();
                for (int month=1;month<=12;month++){
                    monthlyData.put(getMonthName(month),new HashMap<String,Object>(){{
                        put("grandTotal",0.0);
                        put("countOfPeople",0);
                    }});
                }
                for (Object[] result:results){
                    Integer monthInteger=(Integer) result[0];
                    int month = monthInteger;
                    double grandTotal= ((Number) result[1]).doubleValue();
                    int countOfPeople = ((Number) result[2]).intValue();
                    Map<String, Object> monthData = monthlyData.get(getMonthName(month));
                    if (monthData!=null){
                        monthData.put("grandTotal",grandTotal);
                        monthData.put("countOfPeople",countOfPeople);
                    }
                }
                return monthlyData;

            } else if ("Foreigner".equalsIgnoreCase(categoryData.getCategory())) {
                List<Object[]> results = foreignerDataRepo.findMonthlyDataByYear(year);
                Map<String,Map<String,Object>> monthlyData = new LinkedHashMap<>();
                for (int month=1;month<=12;month++){
                    monthlyData.put(getMonthName(month),new HashMap<String,Object>(){{
                        put("grandTotal",0.0);
                        put("countOfPeople",0);
                    }});
                }
                for (Object[] result:results){
                    Integer monthInteger =(Integer) result[0];
                    int month = monthInteger;
                    double grandTotal = ((Number) result[1]).doubleValue();
                    int countOfPeople = ((Number) result[2]).intValue();
                    Map<String,Object> monthData = monthlyData.get(getMonthName(month));
                    if (monthData!=null){
                        monthData.put("grandTotal",grandTotal);
                        monthData.put("countOfPeople",countOfPeople);
                    }
                }return monthlyData;
            }
        }else {
            List<Object[]> publicResults = publicRepo.findMonthlyDataByYear(year);
            List<Object[]> institutionResult = institutionDataRepo.findMonthlyDataByYear(year);
            List<Object[]> foreignerResult = foreignerDataRepo.findMonthlyDataByYear(year);

            Map<String,Map<String,Object>> monthlyData = new LinkedHashMap<>();

            for (int month =1; month<=12;month++){
                String monthName = getMonthName(month);
                Map<String,Object> monthData = new HashMap<>();

                monthData.put("PublicGrandTotal",0.0);
                monthData.put("PublicCountOfPeople",0);
                monthData.put("InstitutionGrandTotal",0.0);
                monthData.put("InstitutionCountOfPeople",0);
                monthData.put("ForeignerGrandTotal",0.0);
                monthData.put("ForeignerCountOfPeople",0);

                monthlyData.put(monthName,monthData);
            }

            for (Object[] result : publicResults){
                Integer monthInteger = (Integer) result[0];
                double publicGrandTotal = ((Number) result[1]).doubleValue();
                int publicCountOfPeople =((Number) result[2]).intValue();

                String monthName = getMonthName(monthInteger);
                Map<String,Object> monthData = monthlyData.get(monthName);
                if (monthData!=null){
                    monthData.put("PublicGrandTotal",publicGrandTotal);
                    monthData.put("PublicCountOfPeople",publicCountOfPeople);
                }
            }
            for (Object[] result:institutionResult){
                Integer monthInteger = (Integer) result[0];
                double institutionGrandTotal = ((Number) result[1]).doubleValue();
                int institutionCountOfPeople = ((Number) result[2]).intValue();
                String monthName = getMonthName(monthInteger);
                Map<String,Object> monthData = monthlyData.get(monthName);
                if (monthData!=null){
                    monthData.put("InstitutionGrandTotal",institutionGrandTotal);
                    monthData.put("InstitutionCountOfPeople",institutionCountOfPeople);
                }
            }
            for (Object[] result:foreignerResult){
                Integer monthInteger =(Integer) result[0];
                double foreignerGrandTotal =((Number) result[1]).doubleValue();
                int foreignerCountOfPeople =((Number) result[2]).intValue();

                String monthName = getMonthName(monthInteger);
                Map<String,Object> monthData = monthlyData.get(monthName);
                if (monthData!=null){
                    monthData.put("ForeignerGrandTotal",foreignerGrandTotal);
                    monthData.put("ForeignerCountOfPeople",foreignerCountOfPeople);
                }
            }
            return monthlyData;
        }return null;
    }

    private String getMonthName(int month) {
        return java.time.Month.of(month).name();
    }

    public ResponseEntity<?> updateTypeGrandTotal(Integer categoryId, TypeGrandTotalDto totalDto) {
        Optional<CategoryData> categoryDataOptional = categoryRepo.findById(categoryId);
        if (categoryDataOptional.isPresent()){
            CategoryData categoryData = categoryDataOptional.get();
            if ("Public".equalsIgnoreCase(categoryData.getCategory())){
                Optional<PublicData> publicDataOptional = publicRepo.findById(totalDto.getTableId());
                if (publicDataOptional.isPresent()){
                    PublicData publicData = publicDataOptional.get();
                    List<TypeData> typeDataList = typeRepo.findByCategoryId(categoryId);
                    if (!typeDataList.isEmpty()){
                        for (TypeData typeData:typeDataList){
                            if ("Adult".equalsIgnoreCase(typeData.getType())){
                                Optional<PriceData> priceDataOptional = priceDataRepo.findByCategoryIdAndTypeId(categoryId,typeData.getId());
                                if (priceDataOptional.isPresent()){
                                    PriceData priceData = priceDataOptional.get();
                                    Double adultCharge = priceData.getPrice() * publicData.getAdult();

                                    Double defaultCharge =0.0;
                                    if (adultCharge.equals(totalDto.getAdultGrandTotal())) {
                                        publicData.setAdultGrandTotal(adultCharge);
                                        publicData.setSeniorCitizenGrandTotal(0.0);

                                    }else {
                                        publicData.setAdultGrandTotal(adultCharge);
                                        publicData.setSeniorCitizenGrandTotal(defaultCharge);
                                    }
                                }

                            }
                            if ("Child".equalsIgnoreCase(typeData.getType())){
                                Optional<PriceData> priceDataOptional1 = priceDataRepo.findByCategoryIdAndTypeId(categoryId,typeData.getId());
                                if (priceDataOptional1.isPresent()){
                                    PriceData priceData = priceDataOptional1.get();
                                    Double childCharge = priceData.getPrice() * publicData.getChild();
                                    if (childCharge.equals(totalDto.getChildGrandTotal())){
                                        publicData.setChildGrandTotal(childCharge);
                                        publicRepo.save(publicData);
                                        return new ResponseEntity<>(publicData,HttpStatus.OK);
                                    }else {
                                        publicData.setChildGrandTotal(childCharge);
                                        publicRepo.save(publicData);
                                        return new ResponseEntity<>(publicData,HttpStatus.ACCEPTED);
                                    }
                                }
                            }
                        }
                    }

                }else {
                    return new ResponseEntity<>("Table Id : "+totalDto.getTableId()+"  is not valid",HttpStatus.BAD_REQUEST);
                }
            } else if ("institution".equalsIgnoreCase(categoryData.getCategory())) {
                Optional<InstitutionData> institutionDataOptional = institutionDataRepo.findById(totalDto.getTableId());
                if (institutionDataOptional.isPresent()){
                    InstitutionData institutionData = institutionDataOptional.get();
                    List<TypeData> typeDataList = typeRepo.findByCategoryId(categoryId);
                    if (!typeDataList.isEmpty()){
                        for (TypeData typeData:typeDataList){
                            Double defaultCharge =0.0;
                            if ("Teacher".equalsIgnoreCase(typeData.getType())){
                                Optional<PriceData> priceDataOptional = priceDataRepo.findByCategoryIdAndTypeId(categoryId,typeData.getId());
                                if (priceDataOptional.isPresent()){
                                    PriceData priceData = priceDataOptional.get();
                                    Double teacherTicketCharge = priceData.getPrice() * institutionData.getTeacher();
                                    if (teacherTicketCharge .equals(totalDto.getTeacherGrandTotal())){
                                        institutionData.setTeacherTicketCharge(teacherTicketCharge);
                                    }else {
                                        institutionData.setTeacherTicketCharge(teacherTicketCharge);
                                    }
                                }
                            }
                            if ("Student".equalsIgnoreCase(typeData.getType())){
                                Optional<PriceData> priceDataOptional1 = priceDataRepo.findByCategoryIdAndTypeId(categoryId,typeData.getId());
                                if (priceDataOptional1.isPresent()){
                                    PriceData priceData1 = priceDataOptional1.get();
                                    Double studentTicketCharge = priceData1.getPrice() * institutionData.getStudent();
                                    if (studentTicketCharge .equals(totalDto.getStudentGrandTotal())){
                                        institutionData.setStudentTicketCharge(studentTicketCharge);
                                        institutionData.setPayableStudentCharge(defaultCharge);
                                        institutionData.setDiscountAmount(defaultCharge);
                                        institutionData.setStudentDiscount(defaultCharge);
                                        institutionDataRepo.save(institutionData);
                                        return new ResponseEntity<>(institutionData,HttpStatus.OK);
                                    }else {
                                        institutionData.setStudentTicketCharge(studentTicketCharge);
                                        institutionData.setPayableStudentCharge(defaultCharge);
                                        institutionData.setDiscountAmount(defaultCharge);
                                        institutionData.setStudentDiscount(defaultCharge);
                                        institutionDataRepo.save(institutionData);
                                        return new ResponseEntity<>(institutionData,HttpStatus.ACCEPTED);
                                    }
                                }
                            }
                        }
                    }
                }else {
                    return new ResponseEntity<>("Table Id : "+totalDto.getTableId()+"  is not valid",HttpStatus.BAD_REQUEST);
                }
            } else if ("foreigner".equalsIgnoreCase(categoryData.getCategory())) {
                    Optional<ForeignerData> foreignerDataOptional = foreignerDataRepo.findById(totalDto.getTableId());
                    if (foreignerDataOptional.isPresent()){
                        ForeignerData foreignerData = foreignerDataOptional.get();
                        List<TypeData> typeDataList = typeRepo.findByCategoryId(categoryId);
                        if (!typeDataList.isEmpty()){
                            for (TypeData typeData:typeDataList){
                                if ("Adult".equalsIgnoreCase(typeData.getType())){
                                    Optional<PriceData> priceDataOptional = priceDataRepo.findByCategoryIdAndTypeId(categoryId,typeData.getId());
                                    if (priceDataOptional.isPresent()){
                                        PriceData priceData = priceDataOptional.get();
                                        Double adultCharge = priceData.getPrice() * foreignerData.getAdult();
                                        if (adultCharge.equals(totalDto.getAdultGrandTotal())) {
                                            foreignerData.setAdultGrandTotal(adultCharge);
                                        }else {
                                            foreignerData.setAdultGrandTotal(adultCharge);
                                        }
                                    }
                                }
                                if ("Child".equalsIgnoreCase(typeData.getType())){
                                    Optional<PriceData> priceDataOptional = priceDataRepo.findByCategoryIdAndTypeId(categoryId,typeData.getId());
                                    if (priceDataOptional.isPresent()){
                                        PriceData priceData = priceDataOptional.get();
                                        Double childCharge = priceData.getPrice() * foreignerData.getChild();
                                        if (childCharge.equals(totalDto.getChildGrandTotal())){
                                            foreignerData.setChildGrandTotal(childCharge);
                                            foreignerDataRepo.save(foreignerData);
                                            return new ResponseEntity<>(foreignerData,HttpStatus.OK);
                                        }else {
                                            foreignerData.setChildGrandTotal(childCharge);
                                            foreignerDataRepo.save(foreignerData);
                                            return new ResponseEntity<>(foreignerData,HttpStatus.ACCEPTED);
                                        }

                                    }
                                }
                            }
                        }

                    }else {
                        return new ResponseEntity<>("Table Id : "+totalDto.getTableId()+"  is not valid",HttpStatus.BAD_REQUEST);
                    }
            }else {
                return new ResponseEntity<>("Category  : "+categoryId+" is not valid",HttpStatus.NO_CONTENT);
            }
        }else {
            return new ResponseEntity<>("Category is not valid",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<VisitsCountDto>> getCountAndGrandTotalByPaymentMode(Integer paymentModeId,LocalDate dateDetails) {
        List<PublicData>publicDataList = publicRepo.findByPaymentModeAndVisitDate(paymentModeId,dateDetails);
        List<InstitutionData>institutionDataList=institutionDataRepo.findByPaymentModeAndVisitDate(paymentModeId,dateDetails);
        List<ForeignerData>foreignerDataList=foreignerDataRepo.findByPaymentModeAndVisitDate(paymentModeId,dateDetails);
        if (publicDataList.isEmpty() && institutionDataList.isEmpty()&& foreignerDataList.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NO_CONTENT);
        }else {
            List<VisitsCountDto> visitsCountDtoList = new ArrayList<>();
            VisitsCountDto visitsCountDto = new VisitsCountDto();

            Integer adultCount =0,childCount=0,seniorCitizenCount=0,count=0,totalCount=0,flag=0;
            Double adultGrandTotals=0.0,childGrandTotals=0.0,seniorCitizenGrandTotal=0.0,overAllGrandTotal=0.0;
            Double overAllIncome=0.0;

            Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(paymentModeId);
            if (paymentModeOptional.isPresent()){
                PaymentMode paymentMode = paymentModeOptional.get();
                String modeName= paymentMode.getPaymentType();
                if ("cash".equalsIgnoreCase(modeName)){
                    if (!publicDataList.isEmpty()){
                        for (PublicData publicData:publicDataList){
                            adultCount+=publicData.getAdult();
                            childCount+=publicData.getChild();
                            seniorCitizenCount+= publicData.getSeniorCitizen();
                            adultGrandTotals+=publicData.getAdultGrandTotal();
                            childGrandTotals+=publicData.getChildGrandTotal();
                            seniorCitizenGrandTotal+=publicData.getSeniorCitizenGrandTotal();
                            count++;
                        }
                        overAllGrandTotal+=adultGrandTotals+childGrandTotals+seniorCitizenGrandTotal;
                        overAllIncome+=overAllGrandTotal;
                        totalCount+=count;
                        visitsCountDto.setAdultCount(adultCount);
                        visitsCountDto.setChildCount(childCount);
                        visitsCountDto.setSeniorCitizen(seniorCitizenCount);
                        visitsCountDto.setPublicTicketCount(count);
                        visitsCountDto.setAdultGrandTotal(adultGrandTotals);
                        visitsCountDto.setChildGrandTotal(childGrandTotals);
                        visitsCountDto.setSeniorCitizenGrandTotal(seniorCitizenGrandTotal);
                        visitsCountDto.setPublicGrandTotal(overAllGrandTotal);
                        adultCount=0;childCount=0;adultGrandTotals=0.0;childGrandTotals=0.0;count=0;overAllGrandTotal=0.0;
                    }
                    if (!institutionDataList.isEmpty()){
                        for (InstitutionData institutionData:institutionDataList){
                            adultCount+=institutionData.getTeacher();
                            childCount+=institutionData.getStudent();
                            adultGrandTotals+=institutionData.getTeacherTicketCharge();
                            childGrandTotals+=institutionData.getPayableStudentCharge();
                            count++;
                        }
                        overAllGrandTotal+=adultGrandTotals+childGrandTotals;
                        overAllIncome+=overAllGrandTotal;
                        totalCount+=count;
                        visitsCountDto.setTeacherCount(adultCount);
                        visitsCountDto.setStudentCount(count);
                        visitsCountDto.setTeacherGrandTotal(adultGrandTotals);
                        visitsCountDto.setStudentGrandTotal(childGrandTotals);
                        visitsCountDto.setInstitutionTicketCount(count);
                        visitsCountDto.setInstitutionGrandTotal(overAllGrandTotal);
                        adultCount=0;childCount=0;adultGrandTotals=0.0;childGrandTotals=0.0;count=0;overAllGrandTotal=0.0;
                    }
                    if (!foreignerDataList.isEmpty()){
                        for (ForeignerData foreignerData:foreignerDataList){
                            adultCount+=foreignerData.getAdult();
                            childCount+=foreignerData.getChild();
                            adultGrandTotals+=foreignerData.getAdultGrandTotal();
                            childGrandTotals+=foreignerData.getChildGrandTotal();
                            count++;
                        }
                        overAllGrandTotal+=adultGrandTotals+childGrandTotals;
                        overAllIncome+=overAllGrandTotal;
                        totalCount+=count;
                        visitsCountDto.setForeignAdult(adultCount);
                        visitsCountDto.setForeignChild(childCount);
                        visitsCountDto.setForeignerAdultGrandTotal(adultGrandTotals);
                        visitsCountDto.setForeignerChildGrandTotal(childGrandTotals);
                        visitsCountDto.setForeignerGrandTotal(overAllGrandTotal);
                        visitsCountDto.setForeignerTicketCount(count);
                    }
                    visitsCountDto.setOverAllIncome(overAllIncome);
                    visitsCountDto.setTotalVisitsCount(totalCount);
                    visitsCountDtoList.add(visitsCountDto);
                    return new ResponseEntity<>(visitsCountDtoList,HttpStatus.OK);
                } else if ("QRCode".equalsIgnoreCase(modeName)) {
                    if (!publicDataList.isEmpty()){
                        for (PublicData publicData:publicDataList){
                            adultCount+=publicData.getAdult();
                            childCount+=publicData.getChild();
                            seniorCitizenCount+= publicData.getSeniorCitizen();
                            adultGrandTotals+=publicData.getAdultGrandTotal();
                            childGrandTotals+=publicData.getChildGrandTotal();
                            seniorCitizenGrandTotal+=publicData.getSeniorCitizenGrandTotal();
                            count++;
                        }
                        overAllGrandTotal+=adultGrandTotals+childGrandTotals+seniorCitizenGrandTotal;
                        overAllIncome+=overAllGrandTotal;
                        totalCount+=count;
                        visitsCountDto.setAdultCount(adultCount);
                        visitsCountDto.setChildCount(childCount);
                        visitsCountDto.setSeniorCitizen(seniorCitizenCount);
                        visitsCountDto.setPublicTicketCount(count);
                        visitsCountDto.setAdultGrandTotal(adultGrandTotals);
                        visitsCountDto.setChildGrandTotal(childGrandTotals);
                        visitsCountDto.setSeniorCitizenGrandTotal(seniorCitizenGrandTotal);
                        visitsCountDto.setPublicGrandTotal(overAllGrandTotal);
                        adultCount=0;childCount=0;adultGrandTotals=0.0;childGrandTotals=0.0;count=0;overAllGrandTotal=0.0;
                    }
                    if (!institutionDataList.isEmpty()){
                        for (InstitutionData institutionData:institutionDataList){
                            adultCount+=institutionData.getTeacher();
                            childCount+=institutionData.getStudent();
                            adultGrandTotals+=institutionData.getTeacherTicketCharge();
                            childGrandTotals+=institutionData.getPayableStudentCharge();
                            count++;
                        }
                        overAllGrandTotal+=adultGrandTotals+childGrandTotals;
                        overAllIncome+=overAllGrandTotal;
                        totalCount+=count;
                        visitsCountDto.setTeacherCount(adultCount);
                        visitsCountDto.setStudentCount(count);
                        visitsCountDto.setTeacherGrandTotal(adultGrandTotals);
                        visitsCountDto.setStudentGrandTotal(childGrandTotals);
                        visitsCountDto.setInstitutionTicketCount(count);
                        visitsCountDto.setInstitutionGrandTotal(overAllGrandTotal);
                        adultCount=0;childCount=0;adultGrandTotals=0.0;childGrandTotals=0.0;count=0;overAllGrandTotal=0.0;
                    }
                    if (!foreignerDataList.isEmpty()){
                        for (ForeignerData foreignerData:foreignerDataList){
                            adultCount+=foreignerData.getAdult();
                            childCount+=foreignerData.getChild();
                            adultGrandTotals+=foreignerData.getAdultGrandTotal();
                            childGrandTotals+=foreignerData.getChildGrandTotal();
                            count++;
                        }
                        overAllGrandTotal+=adultGrandTotals+childGrandTotals;
                        overAllIncome+=overAllGrandTotal;
                        totalCount+=count;
                        visitsCountDto.setForeignAdult(adultCount);
                        visitsCountDto.setForeignChild(childCount);
                        visitsCountDto.setForeignerAdultGrandTotal(adultGrandTotals);
                        visitsCountDto.setForeignerChildGrandTotal(childGrandTotals);
                        visitsCountDto.setForeignerGrandTotal(overAllGrandTotal);
                        visitsCountDto.setForeignerTicketCount(count);
                    }
                    visitsCountDto.setOverAllIncome(overAllIncome);
                    visitsCountDto.setTotalVisitsCount(totalCount);
                    visitsCountDtoList.add(visitsCountDto);
                    return new ResponseEntity<>(visitsCountDtoList,HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NOT_FOUND);
                }
            }
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
