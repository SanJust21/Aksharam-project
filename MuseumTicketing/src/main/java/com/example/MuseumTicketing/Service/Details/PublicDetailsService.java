package com.example.MuseumTicketing.Service.Details;

import com.example.MuseumTicketing.DTO.DetailsRequest;
import com.example.MuseumTicketing.Guide.util.AlphaNumeric;
import com.example.MuseumTicketing.Model.PublicDetails;
import com.example.MuseumTicketing.Repo.PublicDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublicDetailsService {

    private final PublicDetailsRepo publicDetailsRepo;

    @Autowired
    public PublicDetailsService(PublicDetailsRepo publicDetailsRepo) {
        this.publicDetailsRepo = publicDetailsRepo;
    }

    @Autowired
    private AlphaNumeric alphaNumeric;

//    public Object submitAdditionalDetails(String sessionId, String mobileNumber, DetailsRequest detailsRequest, Integer bookingId) {
//        //Optional<PublicDetails> optionalDetails = publicDetailsRepo.findByMobileNumber(mobileNumber);
//        //PublicDetails publicDetails;
//
//        //if (optionalDetails.isPresent()) {
//        //    publicDetails = optionalDetails.get();
//        //} else {
//         //   publicDetails = new PublicDetails();
//        //    publicDetails.setMobileNumber(mobileNumber);
//        //}
//        PublicDetails publicDetails = publicDetailsRepo.findByBookingId(bookingId);
//
//        if (publicDetails == null) {
//            throw new IllegalArgumentException("PublicDetails not found for booking ID: " + bookingId);
//        }
//
//        publicDetails.setSessionId(sessionId);
//        publicDetails.setName(detailsRequest.getName());
//        publicDetails.setEmail(detailsRequest.getEmail());
//        publicDetails.setNumberOfAdults(detailsRequest.getNumberOfAdults());
//        publicDetails.setNumberOfChildren(detailsRequest.getNumberOfChildren());
//        publicDetails.setNumberOfSeniors(detailsRequest.getNumberOfSeniors());
//        //publicDetails.setType(detailsRequest.getType());
//        publicDetails.setTotalPrice(detailsRequest.getTotalPrice());
//        //publicDetails.setVisitDate(detailsRequest.getVisitDate());
//        publicDetails.setMobileNumber(detailsRequest.getMobileNumber());
//        publicDetails.setBookDate(detailsRequest.getBookDate());
//        publicDetails.setUniqueId(alphaNumeric.generateRandomNumber());
//
//
//        publicDetailsRepo.save(publicDetails);
//        return publicDetails;
//    }

    public PublicDetails submitAdditionalDetails(DetailsRequest detailsRequest) {
        PublicDetails publicDetails = publicDetailsRepo.findByBookingId(detailsRequest.getBookingId());
        if (publicDetails==null){
            throw new IllegalArgumentException("PublicDetails not found for booking ID: " + detailsRequest.getBookingId());
        }
        publicDetails.setSessionId(detailsRequest.getSessionId());
        publicDetails.setName(detailsRequest.getName());
        publicDetails.setEmail(detailsRequest.getEmail());
        publicDetails.setMobileNumber(detailsRequest.getMobileNumber());
        publicDetails.setType(detailsRequest.getType());
        publicDetails.setNumberOfAdults(detailsRequest.getNumberOfAdults());
        publicDetails.setNumberOfChildren(detailsRequest.getNumberOfChildren());
        publicDetails.setNumberOfSeniors(detailsRequest.getNumberOfSeniors());
        publicDetails.setTotalPrice(detailsRequest.getTotalPrice());
        publicDetails.setBookDate(detailsRequest.getBookDate());
        publicDetails.setUniqueId(alphaNumeric.generateRandomNumber());
        publicDetailsRepo.save(publicDetails);
        return publicDetails;
    }

    public List<PublicDetails> getAllPublicDetails() {
        List<PublicDetails> allPublicDetails = publicDetailsRepo.findAll();
        List<PublicDetails> filteredPublicDetails = new ArrayList<>();

        for (PublicDetails detail : allPublicDetails) {
            if (detail.getTicketId() != null && !detail.getTicketId().isEmpty()) {
                filteredPublicDetails.add(detail);
            }
        }

        return filteredPublicDetails;
    }
}
