package com.example.MuseumTicketing.Controller;

import com.example.MuseumTicketing.Config.AppConfig;
import com.example.MuseumTicketing.DTO.DetailsRequest;
import com.example.MuseumTicketing.DTO.PriceRequest;
import com.example.MuseumTicketing.Model.ForeignerDetails;
import com.example.MuseumTicketing.Model.InstitutionDetails;
import com.example.MuseumTicketing.Model.PublicDetails;
import com.example.MuseumTicketing.Service.Details.ForeignerDetailsService;
import com.example.MuseumTicketing.Service.Details.InstitutionDetailsService;
import com.example.MuseumTicketing.Service.BasePrice.PriceRequestService;
import com.example.MuseumTicketing.Service.Details.PublicDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/details")
@CrossOrigin

public class DetailsController {


    private final PriceRequestService priceRequestService;

    private final InstitutionDetailsService institutionDetailsService;
    private final PublicDetailsService publicDetailsService;

    private final ForeignerDetailsService foreignerDetailsService;


    @Autowired
    public DetailsController( PriceRequestService priceRequestService, InstitutionDetailsService institutionDetailsService, PublicDetailsService publicDetailsService, ForeignerDetailsService foreignerDetailsService) {
        this.priceRequestService = priceRequestService;
        this.institutionDetailsService = institutionDetailsService;
        this.publicDetailsService = publicDetailsService;
        this.foreignerDetailsService = foreignerDetailsService;
    }

    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @GetMapping("/loadPrice")
    public List<PriceRequest> getAllPrice() {
        return priceRequestService.getAllPrice();
    }


   // @CrossOrigin(origins = AppConfig.BASE_URL)
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitDetails(@RequestBody DetailsRequest detailsRequest) {

        try {
            String sessionId = detailsRequest.getSessionId();
            String type = detailsRequest.getType();
            String mobileNumber = detailsRequest.getMobileNumber();

            //detailsService.submitAdditionalDetails(sessionId, type, detailsRequest);
            Object submittedDetails;
            if ("institution".equalsIgnoreCase(type)) {
                submittedDetails= institutionDetailsService.submitAdditionalDetails(sessionId, mobileNumber, detailsRequest);
            } else if("public".equalsIgnoreCase(type)) {
                submittedDetails = publicDetailsService.submitAdditionalDetails(sessionId, mobileNumber, detailsRequest);
            }else {
                submittedDetails = foreignerDetailsService.submitAdditionalDetails(sessionId, mobileNumber, detailsRequest);
            }


            Map<String,Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Details submitted successfully");
            response.put("amount", getPriceFromDetails(submittedDetails));
            response.put("name", getNameFromDetails(submittedDetails));
            response.put("mobileNumber", getMobileNumberFromDetails(submittedDetails));
            response.put("sessionId", getSessionIdFromDetails(submittedDetails));


            return ResponseEntity.ok(response);
        } catch (Exception e) {

            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Failed to submit details." );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

//    @CrossOrigin(origins = AppConfig.BASE_URL)
//    @PostMapping("/update")
//    public ResponseEntity<Map<String, String>> updateDetails(@RequestBody DetailsRequest detailsRequest) {
//        try {
//            String sessionId = detailsRequest.getSessionId();
//            String type = detailsRequest.getType();
//
//            detailsService.updateDetails(sessionId, type, detailsRequest);
//
//            Map<String, String> response = new HashMap<>();
//            response.put("status", "success");
//            response.put("message", "Details updated successfully");
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Map<String, String> errorResponse = new HashMap<>();
//            errorResponse.put("status", "error");
//            errorResponse.put("message", "Failed to update details. " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//        }
//    }
    private Double getPriceFromDetails(Object details) {
        if (details instanceof InstitutionDetails) {
            return ((InstitutionDetails) details).getTotalPrice();
        } else if (details instanceof PublicDetails) {
            return ((PublicDetails) details).getTotalPrice();
        } else if (details instanceof ForeignerDetails){
            return ((ForeignerDetails) details).getTotalPrice();
        }
        else return null;
    }
    private String getNameFromDetails(Object details) {
        if (details instanceof InstitutionDetails) {
            return ((InstitutionDetails) details).getInstitutionName();
        } else if (details instanceof PublicDetails) {
            return ((PublicDetails) details).getName();
        } else if (details instanceof ForeignerDetails){
            return ((ForeignerDetails) details).getName();
        }
        else return null;
    }
    private String getMobileNumberFromDetails(Object details) {
        if (details instanceof InstitutionDetails) {
            return ((InstitutionDetails) details).getMobileNumber();
        } else if (details instanceof PublicDetails) {
            return ((PublicDetails) details).getMobileNumber();
        } else if (details instanceof ForeignerDetails){
            return ((ForeignerDetails) details).getMobileNumber();
        }
        else return null;
    }

    private String getSessionIdFromDetails(Object details) {
        if (details instanceof InstitutionDetails) {
            return ((InstitutionDetails) details).getSessionId();
        } else if (details instanceof PublicDetails) {
            return ((PublicDetails) details).getSessionId();
        } else if (details instanceof ForeignerDetails){
            return ((ForeignerDetails) details).getSessionId();
        }
        else return null;
    }
}

