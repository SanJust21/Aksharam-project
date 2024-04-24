package com.example.MuseumTicketing.Service.Details;

import com.example.MuseumTicketing.DTO.DetailsRequest;
import com.example.MuseumTicketing.Model.InstitutionDetails;
import com.example.MuseumTicketing.Model.PublicDetails;
import com.example.MuseumTicketing.Repo.InstitutionDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InstitutionDetailsService {

    private final InstitutionDetailsRepo institutionDetailsRepo;

    @Autowired
    public InstitutionDetailsService(InstitutionDetailsRepo institutionDetailsRepo) {
        this.institutionDetailsRepo = institutionDetailsRepo;
    }

    public Object submitAdditionalDetails(String sessionId, String mobileNumber, DetailsRequest detailsRequest, Integer bookingId) {
        //Optional<InstitutionDetails> optionalDetails = institutionDetailsRepo.findByMobileNumber(mobileNumber);


        //if (optionalDetails.isPresent()) {
            //institutionDetails = optionalDetails.get();
        //} else {

        //    institutionDetails.setMobileNumber(mobileNumber);
       // }
        InstitutionDetails institutionDetails= institutionDetailsRepo.findByBookingId(bookingId);

        if (institutionDetails == null) {
            throw new IllegalArgumentException("PublicDetails not found for booking ID: " + bookingId);
        }
        institutionDetails.setSessionId(sessionId);
        //institutionDetails.setType(detailsRequest.getType());
        institutionDetails.setEmail(detailsRequest.getEmail());
        institutionDetails.setInstitutionName(detailsRequest.getInstitutionName());
        institutionDetails.setDistrict(detailsRequest.getDistrict());
        institutionDetails.setNumberOfStudents(detailsRequest.getNumberOfStudents());
        institutionDetails.setNumberOfTeachers(detailsRequest.getNumberOfTeachers());
        institutionDetails.setTotalPrice(detailsRequest.getTotalPrice());
        //institutionDetails.setVisitDate(detailsRequest.getVisitDate());
        institutionDetails.setMobileNumber(detailsRequest.getMobileNumber());
        institutionDetails.setBookDate(detailsRequest.getBookDate());


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

