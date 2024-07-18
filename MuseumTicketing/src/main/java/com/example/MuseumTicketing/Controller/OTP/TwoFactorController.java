package com.example.MuseumTicketing.Controller.OTP;
import com.example.MuseumTicketing.Service.OTP.TwoFactorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/2factor")
@CrossOrigin
public class TwoFactorController {

    private final TwoFactorService twoFactorService;

    @Autowired
    public TwoFactorController(TwoFactorService twoFactorService) {
        this.twoFactorService = twoFactorService;
    }


    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @PostMapping("/generate-otp")
    public ResponseEntity<Map<String, String>> generateOtp(@RequestBody Map<String, String> requestBody) {
        try{
        String mobileNumber = requestBody.get("mobileNumber");
        String otpResponse = twoFactorService.generateOtp(mobileNumber);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", otpResponse);
        response.put("mobileNumber", mobileNumber);

        System.out.println("Received request: " + requestBody);

        return ResponseEntity.ok(response);
    }catch(Exception e) {

            e.printStackTrace();

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Failed to generate OTP.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @GetMapping("/resend-otp/{mobileNumber}")
    public ResponseEntity<Map<String, String>> resendOtp(@PathVariable String mobileNumber) {
        try {
            String resendResponse = twoFactorService.resendOtp(mobileNumber);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", resendResponse);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Failed to resend OTP.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }


    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @PostMapping("/validate-otp")
    public ResponseEntity<Map<String, String>> validateOtp(@RequestBody Map<String, String> requestBody) {
        try
        {
        String sessionId = requestBody.get("sessionId");
        String enteredOtp = requestBody.get("enteredOtp");
        String mobileNumber = requestBody.get("mobileNumber");


        String validationResponse = twoFactorService.validateOtp(sessionId, enteredOtp, mobileNumber);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", validationResponse);
        response.put("sessionId",sessionId);


        return ResponseEntity.ok(response);
    }catch(Exception e){

            e.printStackTrace();

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "OTP Mismatch.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}


