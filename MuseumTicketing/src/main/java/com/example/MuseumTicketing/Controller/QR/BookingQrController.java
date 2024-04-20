package com.example.MuseumTicketing.Controller.QR;

import com.example.MuseumTicketing.Config.AppConfig;
import com.example.MuseumTicketing.DTO.QR.BookingQrRequest;
import com.example.MuseumTicketing.DTO.QR.QrCodeResponse;
//import com.example.MuseumTicketing.Service.Email.EmailService;
import com.example.MuseumTicketing.Service.Email.EmailService;
import com.example.MuseumTicketing.Service.QR.BookingQrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@RestController
@RequestMapping("/api/qr")
@CrossOrigin
public class BookingQrController {

    @Autowired
    private BookingQrService bookingQrService;

    @Autowired
    private EmailService emailService;
   // @CrossOrigin(origins = AppConfig.BASE_URL)
    @PostMapping("/book")
    public ResponseEntity<?> bookTicket(@RequestBody BookingQrRequest bookingQrRequest) {
        try {
            // Generate and fetch QR code details
           // byte[] qrCodeBytes = bookingQrService.generateAndFetchQrCode(bookingQrRequest);
            QrCodeResponse response = bookingQrService.generateAndFetchQrCode(bookingQrRequest);
//            // Send QR code via email
           emailService.sendQrCodeEmail(bookingQrRequest.getPaymentid(), response);

            // Return QR code data in the response
            return ResponseEntity.ok()
                   // .header("Content-Type", MediaType.IMAGE_PNG_VALUE)
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to process booking.");
        }
    }

    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @PostMapping("/mail")
    public ResponseEntity<?> sendPdfMail(@RequestBody BookingQrRequest bookingQrRequest) {
        try {
            byte[] pdfData = Base64.getDecoder().decode(bookingQrRequest.getPdfData());

            // Create a ByteArrayInputStream from the decoded byte array
            ByteArrayInputStream bis = new ByteArrayInputStream(pdfData);
            // Send the ticket PDF via email
            emailService.sendTicketEmail(bis.readAllBytes(), bookingQrRequest.getPaymentid());
            bis.close();

            return ResponseEntity.ok("Ticket PDF has been sent to the specified email address.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to send ticket PDF via email.");
        }
    }

}