package com.example.MuseumTicketing.Service.Details;

import com.example.MuseumTicketing.DTO.DetailsRequest;
import com.example.MuseumTicketing.Model.ForeignerDetails;
import com.example.MuseumTicketing.Repo.ForeignerDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class ForeignerDetailsService {

        private final ForeignerDetailsRepo foreignerDetailsRepo;

        @Autowired
        public ForeignerDetailsService(ForeignerDetailsRepo foreignerDetailsRepo) {
            this.foreignerDetailsRepo = foreignerDetailsRepo;
        }

        public Object submitAdditionalDetails(String sessionId, String mobileNumber, DetailsRequest detailsRequest, Integer bookingId) {
           // Optional<ForeignerDetails> optionalDetails = foreignerDetailsRepo.findByMobileNumber(mobileNumber);
            ForeignerDetails foreignerDetails = foreignerDetailsRepo.findByBookingId(bookingId);
            if (foreignerDetails == null) {
                throw new IllegalArgumentException("PublicDetails not found for booking ID: " + bookingId);
            }
           // if (optionalDetails.isPresent()) {
                //foreignerDetails = optionalDetails.get();
            //} else {

            //    foreignerDetails.setMobileNumber(mobileNumber);
            //}

            foreignerDetails.setSessionId(sessionId);
            foreignerDetails.setName(detailsRequest.getName());
            foreignerDetails.setEmail(detailsRequest.getEmail());
            foreignerDetails.setNumberOfAdults(detailsRequest.getNumberOfAdults());
            foreignerDetails.setNumberOfChildren(detailsRequest.getNumberOfChildren());
            //foreignerDetails.setType(detailsRequest.getType());
            foreignerDetails.setTotalPrice(detailsRequest.getTotalPrice());
            //foreignerDetails.setVisitDate(detailsRequest.getVisitDate());
            foreignerDetails.setMobileNumber(detailsRequest.getMobileNumber());
            foreignerDetails.setBookDate(detailsRequest.getBookDate());


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
