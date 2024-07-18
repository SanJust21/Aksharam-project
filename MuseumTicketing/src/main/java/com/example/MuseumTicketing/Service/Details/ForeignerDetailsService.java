package com.example.MuseumTicketing.Service.Details;

import com.example.MuseumTicketing.DTO.DetailsRequest;
import com.example.MuseumTicketing.Guide.util.AlphaNumeric;
import com.example.MuseumTicketing.Model.ForeignerDetails;
import com.example.MuseumTicketing.Repo.ForeignerDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ForeignerDetailsService {

        private final ForeignerDetailsRepo foreignerDetailsRepo;

        @Autowired
        public ForeignerDetailsService(ForeignerDetailsRepo foreignerDetailsRepo) {
            this.foreignerDetailsRepo = foreignerDetailsRepo;
        }

        @Autowired
        private AlphaNumeric alphaNumeric;

//        public Object submitAdditionalDetails(String sessionId, String mobileNumber, DetailsRequest detailsRequest, Integer bookingId) {
//           // Optional<ForeignerDetails> optionalDetails = foreignerDetailsRepo.findByMobileNumber(mobileNumber);
//            ForeignerDetails foreignerDetails = foreignerDetailsRepo.findByBookingId(bookingId);
//            if (foreignerDetails == null) {
//                throw new IllegalArgumentException("PublicDetails not found for booking ID: " + bookingId);
//            }
//           // if (optionalDetails.isPresent()) {
//                //foreignerDetails = optionalDetails.get();
//            //} else {
//
//            //    foreignerDetails.setMobileNumber(mobileNumber);
//            //}
//
//            foreignerDetails.setSessionId(sessionId);
//            foreignerDetails.setName(detailsRequest.getName());
//            foreignerDetails.setEmail(detailsRequest.getEmail());
//            foreignerDetails.setNumberOfAdults(detailsRequest.getNumberOfAdults());
//            foreignerDetails.setNumberOfChildren(detailsRequest.getNumberOfChildren());
//            //foreignerDetails.setType(detailsRequest.getType());
//            foreignerDetails.setTotalPrice(detailsRequest.getTotalPrice());
//            //foreignerDetails.setVisitDate(detailsRequest.getVisitDate());
//            foreignerDetails.setMobileNumber(detailsRequest.getMobileNumber());
//            foreignerDetails.setBookDate(detailsRequest.getBookDate());
//            foreignerDetails.setUniqueId(alphaNumeric.generateRandomNumber());
//
//
//            foreignerDetailsRepo.save(foreignerDetails);
//            return foreignerDetails;
//        }

    public ForeignerDetails submitAdditionalDetails(DetailsRequest detailsRequest) {
        ForeignerDetails foreignerDetails = foreignerDetailsRepo.findByBookingId(detailsRequest.getBookingId());
        if (foreignerDetails==null){
            throw new IllegalArgumentException("PublicDetails not found for booking ID: " + detailsRequest.getBookingId());
        }
        foreignerDetails.setSessionId(detailsRequest.getSessionId());
        foreignerDetails.setName(detailsRequest.getName());
        foreignerDetails.setEmail(detailsRequest.getEmail());
        foreignerDetails.setMobileNumber(detailsRequest.getMobileNumber());
        foreignerDetails.setType(detailsRequest.getType());
        foreignerDetails.setNumberOfAdults(detailsRequest.getNumberOfAdults());
        foreignerDetails.setNumberOfChildren(detailsRequest.getNumberOfChildren());
        foreignerDetails.setBookDate(detailsRequest.getBookDate());
        foreignerDetails.setTotalPrice(detailsRequest.getTotalPrice());
        foreignerDetails.setUniqueId(alphaNumeric.generateRandomNumber());
        foreignerDetailsRepo.save(foreignerDetails);
        return foreignerDetails;
    }

    public List<ForeignerDetails> getAllForeignerDetails() {
        List<ForeignerDetails> allForeignerDetails = foreignerDetailsRepo.findAll();
        List<ForeignerDetails> filteredForeignerDetails = new ArrayList<>();

        for (ForeignerDetails detail : allForeignerDetails) {
            if (detail.getTicketId() != null && !detail.getTicketId().isEmpty()) {
                filteredForeignerDetails.add(detail);
            }
        }

        return filteredForeignerDetails;

    }
}
