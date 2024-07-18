package com.example.MuseumTicketing.Controller;

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

import java.util.List;

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

//    @GetMapping("/loadPrice")
//    public Map<String, Map<String, Integer>> getAllPrice() {
//        List<PriceRequest> priceRequests = priceRequestService.getAllPrice();
//
//        // Creating a map to store the price information
//        Map<String, Map<String, Integer>> priceMap = new HashMap<>();
//
//        // Looping through each PriceRequest and populating the price map
//        for (PriceRequest priceRequest : priceRequests) {
//            String category = priceRequest.getCategory();
//            String type = priceRequest.getType();
//            int price = priceRequest.getPrice();
//
//            // If the category is not already present in the price map, add it
//            if (!priceMap.containsKey(category)) {
//                priceMap.put(category, new HashMap<>());
//            }
//
//            // Add the type and price to the nested map
//            priceMap.get(category).put(type, price);
//        }
//
//        return priceMap;
//    }



    // @CrossOrigin(origins = AppConfig.BASE_URL)
    @PostMapping("/submit")
//    public ResponseEntity<Map<String, Object>> submitDetails(@RequestBody DetailsRequest detailsRequest) {
//
//        try {
//            String sessionId = detailsRequest.getSessionId();
//            String type = detailsRequest.getType();
//            String mobileNumber = detailsRequest.getMobileNumber();
//            Integer bookingId = detailsRequest.getBookingId();
//
//            //detailsService.submitAdditionalDetails(sessionId, type, detailsRequest);
//            Object submittedDetails;
//            if ("institution".equalsIgnoreCase(type)) {
//                submittedDetails= institutionDetailsService.submitAdditionalDetails(sessionId, mobileNumber, detailsRequest, bookingId);
//            } else if("public".equalsIgnoreCase(type)) {
//                submittedDetails = publicDetailsService.submitAdditionalDetails(sessionId, mobileNumber, detailsRequest, bookingId);
//            }else {
//                submittedDetails = foreignerDetailsService.submitAdditionalDetails(sessionId, mobileNumber, detailsRequest, bookingId);
//            }
//
//
//            Map<String,Object> response = new HashMap<>();
//            response.put("status", "success");
//            response.put("message", "Details submitted successfully");
//            response.put("amount", getPriceFromDetails(submittedDetails));
//            response.put("name", getNameFromDetails(submittedDetails));
//            response.put("mobileNumber", getMobileNumberFromDetails(submittedDetails));
//            response.put("sessionId", getSessionIdFromDetails(submittedDetails));
//
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("status", "error");
//            errorResponse.put("message", "Failed to submit details." );
//
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//        }
//
//    }

    public ResponseEntity<?> submitDetails(@RequestBody DetailsRequest detailsRequest){
        try {
            String type = detailsRequest.getType();
            if ("institution".equalsIgnoreCase(type)){
                InstitutionDetails details=institutionDetailsService.submitAdditinalDetails(detailsRequest);
                return new ResponseEntity<>(details,HttpStatus.OK);
            } else if ("public".equalsIgnoreCase(type)) {
                PublicDetails publicDetails = publicDetailsService.submitAdditionalDetails(detailsRequest);
                return new ResponseEntity<>(publicDetails,HttpStatus.OK);
            }else if ("foreigner".equalsIgnoreCase(type)){
                ForeignerDetails foreignerDetails = foreignerDetailsService.submitAdditionalDetails(detailsRequest);
                return new ResponseEntity<>(foreignerDetails,HttpStatus.OK);
            }else {
                return new ResponseEntity<>("Category is required",HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
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

