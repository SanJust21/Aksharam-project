package com.example.MuseumTicketing.spotReg;

import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingDetails;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingSpotRepo;
import com.example.MuseumTicketing.spotReg.category.additionCharge.AdditionCharge;
import com.example.MuseumTicketing.spotReg.category.additionCharge.AdditionChargeRepo;
import com.example.MuseumTicketing.spotReg.category.gst.GSTData;
import com.example.MuseumTicketing.spotReg.category.gst.GSTRepo;
import com.example.MuseumTicketing.spotReg.category.price.PriceData;
import com.example.MuseumTicketing.spotReg.category.price.PriceDataRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AmountCalculation {
    @Autowired
    private PriceDataRepo priceDataRepo;
    @Autowired
    private GSTRepo gstRepo;
    @Autowired
    private AdditionChargeRepo additionChargeRepo;
    @Autowired
    private BookingSpotRepo bookingSpotRepo;

    //Calculate total ticket charge based on category(public,Institution,foreigner) and type(adult,child,teacher,student,foreignAdult,foreignChild)
    public Double calculateTotalUserCharge(Integer category, Integer typeId, Integer userCount) {
        Double totalUserCharge;
        if (category==1 && typeId==1){
            //public adult
            Optional<PriceData> priceDataOptional=priceDataRepo.findByCategoryIdAndTypeId(category,typeId);
            if (priceDataOptional.isPresent()){
                PriceData priceData = priceDataOptional.get();
                Double perHeadCharge = priceData.getPrice();
                totalUserCharge = userCount*perHeadCharge;
                log.info("totalUserCharge : "+totalUserCharge);
                return totalUserCharge;
            }
        } else if (category==1 && typeId==2) {
            //public child
            Optional<PriceData>priceDataOptional=priceDataRepo.findByCategoryIdAndTypeId(category,typeId);
            if (priceDataOptional.isPresent()){
                PriceData priceData = priceDataOptional.get();
                Double perHeadCharge = priceData.getPrice();
                totalUserCharge = userCount*perHeadCharge;
                return totalUserCharge;
            }
        } else if (category==1 && typeId==3) {
            //public seniorCitizen
            Optional<PriceData>priceDataOptional=priceDataRepo.findByCategoryIdAndTypeId(category,typeId);
            if (priceDataOptional.isPresent()){
                PriceData priceData = priceDataOptional.get();
                Double perHeadCharge = priceData.getPrice();
                totalUserCharge=userCount*perHeadCharge;
                return totalUserCharge;
            }
        } else if (category==2 && typeId==4) {
            //institution teacher
            Optional<PriceData>priceDataOptional=priceDataRepo.findByCategoryIdAndTypeId(category,typeId);
            if (priceDataOptional.isPresent()){
                PriceData priceData = priceDataOptional.get();
                Double perHeadCharge = priceData.getPrice();
                totalUserCharge=userCount*perHeadCharge;
                return totalUserCharge;
            }
        } else if (category==2 && typeId==5) {
            //institution student
            Optional<PriceData>priceDataOptional=priceDataRepo.findByCategoryIdAndTypeId(category,typeId);
            if (priceDataOptional.isPresent()){
                PriceData priceData = priceDataOptional.get();
                Double perHeadCharge = priceData.getPrice();
                totalUserCharge=userCount*perHeadCharge;
                return totalUserCharge;
            }
        } else if (category==3 && typeId ==6) {
            //foreign adult
            Optional<PriceData>priceDataOptional=priceDataRepo.findByCategoryIdAndTypeId(category,typeId);
            if (priceDataOptional.isPresent()){
                PriceData priceData = priceDataOptional.get();
                Double perHeadCharge = priceData.getPrice();
                totalUserCharge = userCount*perHeadCharge;
                return totalUserCharge;
            }
        } else if (category==3 && typeId==7) {
            //foreign child
            Optional<PriceData>priceDataOptional=priceDataRepo.findByCategoryIdAndTypeId(category,typeId);
            if (priceDataOptional.isPresent()){
                PriceData priceData = priceDataOptional.get();
                Double perHeadCharge = priceData.getPrice();
                totalUserCharge = userCount*perHeadCharge;
                return totalUserCharge;
            }
        }
        return -1.0;
    }

    //calculate total GST components
    public Double CalculateGST() {
        List<GSTData> gstDataList =gstRepo.findAll();
        Double totalGstRate = 0.0;
        if (!gstDataList.isEmpty()){
            for (GSTData gstData : gstDataList){
                totalGstRate +=gstData.getAmount();
                log.info("Total GST rate : "+totalGstRate);
            }
            log.info("Total gst rate : "+totalGstRate);
            return totalGstRate;
        }
        return totalGstRate;
    }

    //calculate total additional charges
    public Integer calculateAdditionalCharges() {
        Integer extraCharge =0;
        List<AdditionCharge>additionChargeList=additionChargeRepo.findAll();
        if (!additionChargeList.isEmpty()){
            for (AdditionCharge additionCharge:additionChargeList){
                extraCharge+=additionCharge.getCharge();
                log.info("Additional charge rate : "+extraCharge);
            }
            log.info("AdditionalCharge : "+extraCharge);
            return extraCharge;
        }
        return extraCharge;
    }

    //grandTotal calculation
    public Double calculateGrandTotal(Double totalUserGst, Integer extraCharge, Double totalCharges) {
        Double grandTotal = totalCharges+totalUserGst+extraCharge;
        log.info("Grand total : "+grandTotal);
        return grandTotal;
    }

    //decline slot by no.of userCount
    public BookingDetails generateBookingDate(LocalDate visitDate, Integer slotId, Integer totalUserCount) {
        Optional<BookingDetails> bookingDetailsOptional = bookingSpotRepo.findByBookDateAndSlotId(visitDate,slotId);
        if (bookingDetailsOptional.isPresent()){
            BookingDetails bookingDetails = bookingDetailsOptional.get();
            Integer capacity = bookingDetails.getPresentCapacity();
            Integer calcCapacity = capacity-totalUserCount;
            bookingDetails.setPresentCapacity(calcCapacity);
            bookingSpotRepo.save(bookingDetails);
            return bookingDetails;
        }
        return null;
    }
}
