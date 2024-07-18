package com.example.MuseumTicketing.Service.Details;

import com.example.MuseumTicketing.DTO.DetailsRequest;
import com.example.MuseumTicketing.Guide.util.AlphaNumeric;
import com.example.MuseumTicketing.Model.InstitutionDetails;
import com.example.MuseumTicketing.Repo.InstitutionDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InstitutionDetailsService {

    private final InstitutionDetailsRepo institutionDetailsRepo;

    @Autowired
    public InstitutionDetailsService(InstitutionDetailsRepo institutionDetailsRepo) {
        this.institutionDetailsRepo = institutionDetailsRepo;
    }

    @Autowired
    private AlphaNumeric alphaNumeric;

//    public Object submitAdditionalDetails(String sessionId, String mobileNumber, DetailsRequest detailsRequest, Integer bookingId) {
//        //Optional<InstitutionDetails> optionalDetails = institutionDetailsRepo.findByMobileNumber(mobileNumber);
//
//
//        //if (optionalDetails.isPresent()) {
//            //institutionDetails = optionalDetails.get();
//        //} else {
//
//        //    institutionDetails.setMobileNumber(mobileNumber);
//       // }
//        InstitutionDetails institutionDetails= institutionDetailsRepo.findByBookingId(bookingId);
//
//        if (institutionDetails == null) {
//            throw new IllegalArgumentException("PublicDetails not found for booking ID: " + bookingId);
//        }
//        institutionDetails.setSessionId(sessionId);
//        //institutionDetails.setType(detailsRequest.getType());
//        institutionDetails.setEmail(detailsRequest.getEmail());
//        institutionDetails.setInstitutionName(detailsRequest.getInstitutionName());
//        institutionDetails.setDistrict(detailsRequest.getDistrict());
//        institutionDetails.setNumberOfStudents(detailsRequest.getNumberOfStudents());
//        institutionDetails.setNumberOfTeachers(detailsRequest.getNumberOfTeachers());
//        institutionDetails.setTotalPrice(detailsRequest.getTotalPrice());
//        //institutionDetails.setVisitDate(detailsRequest.getVisitDate());
//        institutionDetails.setMobileNumber(detailsRequest.getMobileNumber());
//        institutionDetails.setBookDate(detailsRequest.getBookDate());
//        institutionDetails.setUniqueId(alphaNumeric.generateRandomNumber());
//
//
//        institutionDetailsRepo.save(institutionDetails);
//        return institutionDetails;
//    }

    public InstitutionDetails submitAdditinalDetails(DetailsRequest detailsRequest) {
        Integer bookingId = detailsRequest.getBookingId();
        InstitutionDetails institutionDetails= institutionDetailsRepo.findByBookingId(bookingId);
        if (institutionDetails==null){
            throw new IllegalArgumentException("PublicDetails not found for booking ID: " + bookingId);
        }
        institutionDetails.setInstitutionName(detailsRequest.getInstitutionName());
        institutionDetails.setEmail(detailsRequest.getEmail());
        institutionDetails.setDistrict(detailsRequest.getDistrict());
        institutionDetails.setUniqueId(alphaNumeric.generateRandomNumber());
        institutionDetails.setBookDate(detailsRequest.getBookDate());
        institutionDetails.setMobileNumber(detailsRequest.getMobileNumber());
        institutionDetails.setNumberOfTeachers(detailsRequest.getNumberOfTeachers());
        institutionDetails.setNumberOfStudents(detailsRequest.getNumberOfStudents());
        institutionDetails.setTotalPrice(detailsRequest.getTotalPrice());
        institutionDetails.setSessionId(detailsRequest.getSessionId());
        institutionDetailsRepo.save(institutionDetails);
        return institutionDetails;
    }

    public List<InstitutionDetails> getAllInstitutionDetails() {
        List<InstitutionDetails> allInstitutionDetails = institutionDetailsRepo.findAll();
        List<InstitutionDetails> filteredInstitutionDetails = new ArrayList<>();

        for (InstitutionDetails detail : allInstitutionDetails) {
            if (detail.getTicketId() != null && !detail.getTicketId().isEmpty()) {
                filteredInstitutionDetails.add(detail);
            }
        }

        return filteredInstitutionDetails;

    }
}

