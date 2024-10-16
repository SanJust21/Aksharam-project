package com.example.MuseumTicketing.spotReg;

import com.example.MuseumTicketing.Guide.util.AlphaNumeric;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingDetails;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingSpotRepo;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SpotSlot;
import com.example.MuseumTicketing.spotReg.bookingDetails.slotData.SpotSlotRepo;
import com.example.MuseumTicketing.spotReg.category.additionCharge.AdditionChargeRepo;
import com.example.MuseumTicketing.spotReg.category.gst.GSTRepo;
import com.example.MuseumTicketing.spotReg.category.paymentMode.PaymentMode;
import com.example.MuseumTicketing.spotReg.category.paymentMode.PaymentModeRepo;
import com.example.MuseumTicketing.spotReg.category.paymentStatus.PaymentStatus;
import com.example.MuseumTicketing.spotReg.category.paymentStatus.PaymentStatusRepo;
import com.example.MuseumTicketing.spotReg.category.price.PriceDataRepo;
import com.example.MuseumTicketing.spotReg.category.type.TypeRepo;
import com.example.MuseumTicketing.spotReg.userData.Institution.InstitutionData;
import com.example.MuseumTicketing.spotReg.userData.Institution.InstitutionDataRepo;
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


    public ResponseEntity<?> publicUserReg(SpotUserDto spotUserDto, Integer category) {
        PublicData publicDetails = new PublicData();
        publicDetails.setName(spotUserDto.getName());
        publicDetails.setPhNumber(spotUserDto.getPhNumber());
        publicDetails.setAdult(spotUserDto.getAdult());
        publicDetails.setChild(spotUserDto.getChild());
        publicDetails.setSeniorCitizen(spotUserDto.getSeniorCitizen());
        Double totalAdultCharge=0.0;    Double totalChildCharge=0.0;    Double totalSeniorCitizenCharge=0.0;
        Double grandTotal;
        Integer userCount,typeId,totalUserCount=0;
        if (spotUserDto.getAdult()>0){
            typeId = spotUserDto.getAdultTypeId();
            userCount = spotUserDto.getAdult();
            totalUserCount+=userCount;
            totalAdultCharge =amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        if (spotUserDto.getChild()>0){
            typeId=spotUserDto.getChildTypeId();
            userCount = spotUserDto.getChild();
            totalUserCount+=userCount;
            totalChildCharge = amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        if (spotUserDto.getSeniorCitizen()>0){
            typeId = spotUserDto.getSeniorCitizenTypeId();
            userCount = spotUserDto.getSeniorCitizen();
            totalUserCount+=userCount;
            totalSeniorCitizenCharge = amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        Double totalCharges = totalAdultCharge+totalChildCharge+totalSeniorCitizenCharge;
        Double totalGstRate =0.0; Double totalUserGst;
        totalGstRate = amountCalculation.CalculateGST();
        totalUserGst = totalGstRate*totalCharges;
        Integer extraCharge= amountCalculation.calculateAdditionalCharges();
        grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);
        publicDetails.setTotalAmount(totalCharges);
        publicDetails.setTotalGstCharge(totalUserGst);
        publicDetails.setTotalAdditionalCharges(extraCharge);
        publicDetails.setGrandTotal(grandTotal);
        publicDetails.setPaymentMode(spotUserDto.getPaymentMode());
        publicDetails.setPaymentStatusId(spotUserDto.getPaymentStatusId());
        Optional<PaymentStatus>paymentStatusOptional=paymentStatusRepo.findById(spotUserDto.getPaymentStatusId());
        Optional<PaymentMode>paymentModeOptional=paymentModeRepo.findById(spotUserDto.getPaymentMode());
        if (paymentModeOptional.isPresent() && paymentStatusOptional.isPresent()){
            PaymentStatus paymentStatus =paymentStatusOptional.get();
            String name =paymentStatus.getStatusName();
            PaymentMode paymentMode = paymentModeOptional.get();
            String modeName = paymentMode.getPaymentType();
            if ("cash".equalsIgnoreCase(modeName) && "received".equalsIgnoreCase(name)){
                publicDetails.setTicketId(alphaNumeric.generateSpotRandomNumber());
                publicDetails.setCreatedTime(LocalTime.now());
                BookingDetails bookingDetails = amountCalculation.generateBookingDate(spotUserDto.getVisitDate(),spotUserDto.getSlotId(),totalUserCount);
                publicDetails.setSlotId(bookingDetails.getSlotId());
                publicDetails.setVisitDate(bookingDetails.getBookDate());
            }else {
                publicDetails.setTicketId(null);
            }
        }
        publicDetails.setCreatedBy(spotUserDto.getCreatedBy());
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
        Double grandTotal;Integer userCount,typeId,totalUserCount=0;
        if (spotUserDto.getTeacher()>0){
            typeId = spotUserDto.getTeacherTypeId();
            userCount = spotUserDto.getTeacher();
            totalUserCount+=userCount;
            totalTeacherCharge = amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        if (spotUserDto.getStudent()>0){
            typeId=spotUserDto.getStudentTypeId();
            userCount=spotUserDto.getStudent();
            totalUserCount+=userCount;
            totalStudentCharge = amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        Double totalCharges = totalStudentCharge+totalTeacherCharge;
        Double totalGstRate =amountCalculation.CalculateGST(),totalUserGst;
        totalUserGst = totalGstRate*totalCharges;
        Integer extraCharge= amountCalculation.calculateAdditionalCharges();
        grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);
        institutionData.setTotalAmount(totalCharges);
        institutionData.setPaymentMode(spotUserDto.getPaymentMode());
        institutionData.setTotalGstCharge(totalUserGst);
        institutionData.setTotalAdditionalCharges(extraCharge);
        institutionData.setGrandTotal(grandTotal);
        institutionData.setPaymentStatusId(spotUserDto.getPaymentStatusId());
        Optional<PaymentStatus>paymentStatusOptional=paymentStatusRepo.findById(spotUserDto.getPaymentStatusId());
        Optional<PaymentMode>paymentModeOptional=paymentModeRepo.findById(spotUserDto.getPaymentMode());
        if (paymentModeOptional.isPresent() && paymentStatusOptional.isPresent()){
            PaymentStatus paymentStatus =paymentStatusOptional.get();
            String name =paymentStatus.getStatusName();
            PaymentMode paymentMode = paymentModeOptional.get();
            String modeName = paymentMode.getPaymentType();
            if ("cash".equalsIgnoreCase(modeName) && "received".equalsIgnoreCase(name)){
                institutionData.setTicketId(alphaNumeric.generateSpotRandomNumber());
                institutionData.setCreatedTime(LocalTime.now());
            }else {
                institutionData.setTicketId(null);
            }
        }
        BookingDetails bookingDetails = amountCalculation.generateBookingDate(spotUserDto.getVisitDate(),spotUserDto.getSlotId(),totalUserCount);
        institutionData.setVisitDate(bookingDetails.getBookDate());
        institutionData.setSlotId(bookingDetails.getSlotId());
        institutionData.setCreatedBy(spotUserDto.getCreatedBy());
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
        Integer userCount,typeId,totalUserCount=0;
        if (spotUserDto.getAdult()>0){
            typeId=spotUserDto.getAdultTypeId();
            userCount = spotUserDto.getAdult();
            totalUserCount+=userCount;
            totalAdultCharge=amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        if (spotUserDto.getChild()>0){
            typeId=spotUserDto.getChildTypeId();
            userCount = spotUserDto.getChild();
            totalUserCount+=userCount;
            totalChildCharge=amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        Double totalCharges=totalAdultCharge+totalChildCharge;
        Double totalGstRate =amountCalculation.CalculateGST(),totalUserGst;
        totalUserGst = totalGstRate*totalCharges;
        Integer extraCharge= amountCalculation.calculateAdditionalCharges();
        grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);
        foreignerData.setTotalAmount(totalCharges);
        foreignerData.setTotalGstCharge(totalUserGst);
        foreignerData.setTotalAdditionalCharges(extraCharge);
        foreignerData.setGrandTotal(grandTotal);
        foreignerData.setPaymentMode(spotUserDto.getPaymentMode());
        foreignerData.setPaymentStatusId(spotUserDto.getPaymentStatusId());
        Optional<PaymentStatus>paymentStatusOptional=paymentStatusRepo.findById(spotUserDto.getPaymentStatusId());
        Optional<PaymentMode>paymentModeOptional=paymentModeRepo.findById(spotUserDto.getPaymentMode());
        if (paymentModeOptional.isPresent() && paymentStatusOptional.isPresent()){
            PaymentStatus paymentStatus =paymentStatusOptional.get();
            String name =paymentStatus.getStatusName();
            PaymentMode paymentMode = paymentModeOptional.get();
            String modeName = paymentMode.getPaymentType();
            if ("cash".equalsIgnoreCase(modeName) && "received".equalsIgnoreCase(name)){
                foreignerData.setTicketId(alphaNumeric.generateSpotRandomNumber());
                foreignerData.setCreatedTime(LocalTime.now());
            }else {
                foreignerData.setTicketId(null);
            }
        }
        BookingDetails bookingDetails = amountCalculation.generateBookingDate(spotUserDto.getVisitDate(),spotUserDto.getSlotId(),totalUserCount);
        foreignerData.setVisitDate(bookingDetails.getBookDate());
        foreignerData.setSlotId(bookingDetails.getSlotId());
        foreignerData.setCreatedBy(spotUserDto.getCreatedBy());
        foreignerDataRepo.save(foreignerData);
        return new ResponseEntity<>(foreignerData,HttpStatus.OK);
    }

    public ResponseEntity<List<SpotUserDto>> getAllUserDetails() {
        List<SpotUserDto> spotUserDtoList = new ArrayList<>();
        List<PublicData> publicDataList = publicRepo.findAll();
        Double totalUserRevenue=0.0;
        for (PublicData publicData : publicDataList){
            SpotUserDto spotUserDto = new SpotUserDto();
            spotUserDto.setName(publicData.getName());
            spotUserDto.setPhNumber(publicData.getPhNumber());
            spotUserDto.setAdult(publicData.getAdult());
            spotUserDto.setChild(publicData.getChild());
            spotUserDto.setSeniorCitizen(publicData.getSeniorCitizen());
            spotUserDto.setVisitDate(publicData.getVisitDate());
            Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(publicData.getSlotId());
            if (spotSlotOptional.isPresent()){
                SpotSlot spotSlot = spotSlotOptional.get();
                spotUserDto.setSlotTime(spotSlot.getSlotEndTime());
            }
            spotUserDto.setGrandTotal(publicData.getTotalAmount());
            totalUserRevenue+=publicData.getGrandTotal();
            Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(publicData.getPaymentMode());
            if (paymentModeOptional.isPresent()){
                PaymentMode paymentMode = paymentModeOptional.get();
                spotUserDto.setPaymentModeName(paymentMode.getPaymentType());
            }else {
                spotUserDto.setPaymentModeName(null);
            }
            Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(publicData.getPaymentStatusId());
            if (paymentStatusOptional.isPresent()){
                PaymentStatus paymentStatus = paymentStatusOptional.get();
                spotUserDto.setPaymentStatus(paymentStatus.getStatusName());
            }else {
                spotUserDto.setPaymentStatus(null);
            }
            spotUserDto.setCreatedBy(publicData.getCreatedBy());
            spotUserDto.setGeneratedTime(publicData.getCreatedTime());
            spotUserDtoList.add(spotUserDto);
        }

        List<InstitutionData>institutionDataList = institutionDataRepo.findAll();
        for (InstitutionData institutionData:institutionDataList){
            SpotUserDto spotUserDtoInstitution = new SpotUserDto();
            spotUserDtoInstitution.setName(institutionData.getName());
            spotUserDtoInstitution.setPhNumber(institutionData.getPhNumber());
            spotUserDtoInstitution.setDistrict(institutionData.getDistrict());
            spotUserDtoInstitution.setTeacher(institutionData.getTeacher());
            spotUserDtoInstitution.setStudent(institutionData.getStudent());
            spotUserDtoInstitution.setVisitDate(institutionData.getVisitDate());
            Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(institutionData.getSlotId());
            if (spotSlotOptional.isPresent()){
                SpotSlot spotSlot = spotSlotOptional.get();
                spotUserDtoInstitution.setSlotTime(spotSlot.getSlotEndTime());
            }
            spotUserDtoInstitution.setGrandTotal(institutionData.getGrandTotal());
            totalUserRevenue+=institutionData.getGrandTotal();
            Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(institutionData.getPaymentMode());
            if (paymentModeOptional.isPresent()){
                PaymentMode paymentMode = paymentModeOptional.get();
                spotUserDtoInstitution.setPaymentModeName(paymentMode.getPaymentType());
            }else {
                spotUserDtoInstitution.setPaymentModeName(null);
            }
            Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(institutionData.getPaymentStatusId());
            if (paymentStatusOptional.isPresent()){
                PaymentStatus paymentStatus = paymentStatusOptional.get();
                spotUserDtoInstitution.setPaymentStatus(paymentStatus.getStatusName());
            }else {
                spotUserDtoInstitution.setPaymentStatus(null);
            }
            spotUserDtoInstitution.setCreatedBy(institutionData.getCreatedBy());

            spotUserDtoList.add(spotUserDtoInstitution);
        }

        List<ForeignerData>foreignerDataList=foreignerDataRepo.findAll();
        for (ForeignerData foreignerData:foreignerDataList){
            SpotUserDto spotUserDtoForeigner = new SpotUserDto();
            spotUserDtoForeigner.setName(foreignerData.getName());
            spotUserDtoForeigner.setPhNumber(foreignerData.getPhNumber());
            spotUserDtoForeigner.setAdult(foreignerData.getAdult());
            spotUserDtoForeigner.setChild(foreignerData.getChild());
            spotUserDtoForeigner.setVisitDate(foreignerData.getVisitDate());
            Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(foreignerData.getSlotId());
            if (spotSlotOptional.isPresent()){
                SpotSlot spotSlot = spotSlotOptional.get();
                spotUserDtoForeigner.setSlotTime(spotSlot.getSlotEndTime());
            }
            spotUserDtoForeigner.setGrandTotal(foreignerData.getTotalAmount());
            totalUserRevenue+=foreignerData.getGrandTotal();
            spotUserDtoForeigner.setTotalRevenue(totalUserRevenue);
            Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(foreignerData.getPaymentMode());
            if (paymentModeOptional.isPresent()){
                PaymentMode paymentMode = paymentModeOptional.get();
                spotUserDtoForeigner.setPaymentModeName(paymentMode.getPaymentType());
            }else {
                spotUserDtoForeigner.setPaymentModeName(null);
            }
            Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(foreignerData.getPaymentStatusId());
            if (paymentStatusOptional.isPresent()){
                PaymentStatus paymentStatus = paymentStatusOptional.get();
                spotUserDtoForeigner.setPaymentStatus(paymentStatus.getStatusName());
            }else {
                spotUserDtoForeigner.setPaymentStatus(null);
            }
            spotUserDtoForeigner.setCreatedBy(foreignerData.getCreatedBy());
            spotUserDtoForeigner.setGeneratedTime(foreignerData.getCreatedTime());
            spotUserDtoList.add(spotUserDtoForeigner);
        }
        return new ResponseEntity<>(spotUserDtoList,HttpStatus.OK);
    }

    public ResponseEntity<List<SpotUserDto>> getAllPublic() {
        List<SpotUserDto> spotUserDtoList = new ArrayList<>();
        List<PublicData> publicDataList = publicRepo.findAll();
        Double totalRevenue =0.0;
        for (PublicData publicData : publicDataList){
            SpotUserDto spotUserDto = new SpotUserDto();
            spotUserDto.setName(publicData.getName());
            spotUserDto.setPhNumber(publicData.getPhNumber());
            spotUserDto.setAdult(publicData.getAdult());
            spotUserDto.setChild(publicData.getChild());
            spotUserDto.setSeniorCitizen(publicData.getSeniorCitizen());
            spotUserDto.setVisitDate(publicData.getVisitDate());
            Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(publicData.getSlotId());
            if (spotSlotOptional.isPresent()){
                SpotSlot spotSlot = spotSlotOptional.get();
                spotUserDto.setSlotTime(spotSlot.getSlotEndTime());
            }
            spotUserDto.setGrandTotal(publicData.getTotalAmount());
            totalRevenue+=publicData.getGrandTotal();
            spotUserDto.setTotalRevenue(totalRevenue);
            Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(publicData.getPaymentMode());
            if (paymentModeOptional.isPresent()){
                PaymentMode paymentMode = paymentModeOptional.get();
                spotUserDto.setPaymentModeName(paymentMode.getPaymentType());
            }else {
                spotUserDto.setPaymentModeName(null);
            }
            Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(publicData.getPaymentStatusId());
            if (paymentStatusOptional.isPresent()){
                PaymentStatus paymentStatus = paymentStatusOptional.get();
                spotUserDto.setPaymentStatus(paymentStatus.getStatusName());
            }else {
                spotUserDto.setPaymentStatus(null);
            }
            spotUserDto.setCreatedBy(publicData.getCreatedBy());
            spotUserDto.setGeneratedTime(publicData.getCreatedTime());
            spotUserDtoList.add(spotUserDto);
        }
        return new ResponseEntity<>(spotUserDtoList,HttpStatus.OK);
    }

    public ResponseEntity<List<SpotUserDto>> getAllInstitution() {
        List<SpotUserDto> spotUserDtoList = new ArrayList<>();
        List<InstitutionData>institutionDataList = institutionDataRepo.findAll();
        Double totalRevenue =0.0;
        for (InstitutionData institutionData:institutionDataList){
            SpotUserDto spotUserDtoInstitution = new SpotUserDto();
            spotUserDtoInstitution.setName(institutionData.getName());
            spotUserDtoInstitution.setPhNumber(institutionData.getPhNumber());
            spotUserDtoInstitution.setDistrict(institutionData.getDistrict());
            spotUserDtoInstitution.setTeacher(institutionData.getTeacher());
            spotUserDtoInstitution.setStudent(institutionData.getStudent());
            spotUserDtoInstitution.setVisitDate(institutionData.getVisitDate());
            Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(institutionData.getSlotId());
            if (spotSlotOptional.isPresent()){
                SpotSlot spotSlot = spotSlotOptional.get();
                spotUserDtoInstitution.setSlotTime(spotSlot.getSlotEndTime());
            }
            spotUserDtoInstitution.setGrandTotal(institutionData.getTotalAmount());
            totalRevenue+=institutionData.getGrandTotal();
            spotUserDtoInstitution.setTotalRevenue(totalRevenue);
            Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(institutionData.getPaymentMode());
            if (paymentModeOptional.isPresent()){
                PaymentMode paymentMode = paymentModeOptional.get();
                spotUserDtoInstitution.setPaymentModeName(paymentMode.getPaymentType());
            }else {
                spotUserDtoInstitution.setPaymentModeName(null);
            }
            Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(institutionData.getPaymentStatusId());
            if (paymentStatusOptional.isPresent()){
                PaymentStatus paymentStatus = paymentStatusOptional.get();
                spotUserDtoInstitution.setPaymentStatus(paymentStatus.getStatusName());
            }else {
                spotUserDtoInstitution.setPaymentStatus(null);
            }
            spotUserDtoInstitution.setCreatedBy(institutionData.getCreatedBy());
            spotUserDtoInstitution.setGeneratedTime(institutionData.getCreatedTime());
            spotUserDtoList.add(spotUserDtoInstitution);
        }
        return new ResponseEntity<>(spotUserDtoList,HttpStatus.OK);
    }

    public ResponseEntity<List<SpotUserDto>> getAllForeigner() {
        List<SpotUserDto> spotUserDtoList = new ArrayList<>();
        List<ForeignerData>foreignerDataList=foreignerDataRepo.findAll();
        Double totalRevenue=0.0;
        for (ForeignerData foreignerData:foreignerDataList){
            SpotUserDto spotUserDtoForeigner = new SpotUserDto();
            spotUserDtoForeigner.setName(foreignerData.getName());
            spotUserDtoForeigner.setPhNumber(foreignerData.getPhNumber());
            spotUserDtoForeigner.setAdult(foreignerData.getAdult());
            spotUserDtoForeigner.setChild(foreignerData.getChild());
            spotUserDtoForeigner.setVisitDate(foreignerData.getVisitDate());
            Optional<SpotSlot> spotSlotOptional = spotSlotRepo.findById(foreignerData.getSlotId());
            if (spotSlotOptional.isPresent()){
                SpotSlot spotSlot = spotSlotOptional.get();
                spotUserDtoForeigner.setSlotTime(spotSlot.getSlotEndTime());
            }
            spotUserDtoForeigner.setGrandTotal(foreignerData.getTotalAmount());
            totalRevenue+= spotUserDtoForeigner.getGrandTotal();
            spotUserDtoForeigner.setTotalRevenue(totalRevenue);
            Optional<PaymentMode> paymentModeOptional = paymentModeRepo.findById(foreignerData.getPaymentMode());
            if (paymentModeOptional.isPresent()){
                PaymentMode paymentMode = paymentModeOptional.get();
                spotUserDtoForeigner.setPaymentModeName(paymentMode.getPaymentType());
            }else {
                spotUserDtoForeigner.setPaymentModeName(null);
            }
            Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepo.findById(foreignerData.getPaymentStatusId());
            if (paymentStatusOptional.isPresent()){
                PaymentStatus paymentStatus = paymentStatusOptional.get();
                spotUserDtoForeigner.setPaymentStatus(paymentStatus.getStatusName());
            }else {
                spotUserDtoForeigner.setPaymentStatus(null);
            }
            spotUserDtoForeigner.setCreatedBy(foreignerData.getCreatedBy());
            spotUserDtoForeigner.setGeneratedTime(foreignerData.getCreatedTime());
            spotUserDtoList.add(spotUserDtoForeigner);
        }
        return new ResponseEntity<>(spotUserDtoList,HttpStatus.OK);
    }
}
